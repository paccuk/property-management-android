package org.stkachenko.propertymanagement.core.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.stkachenko.propertymanagement.core.datastore.auth_tokens.PmTokensDataStore
import org.stkachenko.propertymanagement.core.model.data.auth.Tokens
import org.stkachenko.propertymanagement.core.common.network.di.ApplicationScope
import org.stkachenko.propertymanagement.core.storage.encryptor.Encryptor
import org.stkachenko.propertymanagement.core.storage.mappers.encrypt
import org.stkachenko.propertymanagement.core.storage.mappers.decrypt
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import kotlin.concurrent.Volatile

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val APP_KEY_ALIAS = BuildConfig.APP_KEY_ALIAS
private const val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE

const val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$ENCRYPTION_PADDING"

class KeystoreTokenStorage @Inject constructor(
    private val pmTokensDataStore: PmTokensDataStore,
    private val encryptor: Encryptor,
    @ApplicationScope scope: CoroutineScope,
) : TokenStorage {
    private val updateMutex = Mutex()

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
    }

    private val secretKey: SecretKey by lazy {
        if (!keyStore.containsAlias(APP_KEY_ALIAS)) {
            generateSecretKey()
        }
        keyStore.getKey(APP_KEY_ALIAS, null) as SecretKey
    }

    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Loading)
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    @Volatile
    private var _tokens: Tokens? = null

    init {
        scope.launch {
            loadTokens()
        }
    }

    private fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KEY_ALGORITHM,
            ANDROID_KEY_STORE
        )
        val parameterSpec = KeyGenParameterSpec.Builder(
            APP_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(256)
        }.build()

        keyGenerator.init(parameterSpec)
        keyGenerator.generateKey()
    }

    private suspend fun loadTokens() {
        try {
            val accessToken = loadAccessToken()
            val refreshToken = loadRefreshToken()
            if (accessToken != null && refreshToken != null) {
                val tokens = Tokens(accessToken, refreshToken)
                _tokens = tokens
                _tokenState.update { TokenState.Success(tokens) }
            } else {
                _tokenState.update { TokenState.Empty }
            }
        } catch (e: Exception) {
            _tokenState.update { TokenState.Error(e) }
        }
    }

    override suspend fun getAccessToken(): String? {
        return when (val state = tokenState.first { it !is TokenState.Loading }) {
            is TokenState.Success -> state.tokens.accessToken
            else -> null
        }
    }

    override suspend fun getRefreshToken(): String? {
        return when (val state = tokenState.first { it !is TokenState.Loading }) {
            is TokenState.Success -> state.tokens.refreshToken
            else -> null
        }
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        updateMutex.withLock {
            try {
                val key = secretKey
                val tokens = Tokens(accessToken, refreshToken)
                val encryptedTokens = tokens.encrypt(encryptor, key)
                pmTokensDataStore.setEncryptedTokens(encryptedTokens)
                _tokens = tokens
                _tokenState.value = TokenState.Success(tokens)
            } catch (e: Exception) {
                _tokenState.update { TokenState.Error(e) }
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadAccessToken(): String? {
        val tokenData = pmTokensDataStore.getEncryptedAccessToken()
        return tokenData?.decrypt(encryptor, secretKey)
    }

    private suspend fun loadRefreshToken(): String? {
        val tokenData = pmTokensDataStore.getEncryptedRefreshToken()
        return tokenData?.decrypt(encryptor, secretKey)
    }

    override suspend fun clearTokens() {
        updateMutex.withLock {
            pmTokensDataStore.clearEncryptedTokens()
            _tokens = null
            _tokenState.value = TokenState.Empty
        }
    }

    override suspend fun hasTokens(): Boolean {
        val state = tokenState.value
        return state is TokenState.Success
    }
}

sealed interface TokenState {
    data object Loading : TokenState
    data object Empty : TokenState
    data class Success(val tokens: Tokens) : TokenState
    data class Error(val throwable: Throwable) : TokenState
}

