package org.stkachenko.propertymanagement.core.network.retrofit

import androidx.tracing.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.stkachenko.propertymanagement.core.network.BuildConfig
import org.stkachenko.propertymanagement.core.network.PmNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.NetworkChangeList
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImage
import org.stkachenko.propertymanagement.core.network.model.image.NetworkImageAttachment
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule
import org.stkachenko.propertymanagement.core.network.model.profile.NetworkProfile
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitPmNetworkApi {
    @GET(value = "properties")
    suspend fun getProperties(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkProperty>>

    @GET(value = "chats")
    suspend fun getChats(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkChat>>

    @GET(value = "chatParticipants")
    suspend fun getChatParticipants(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkChatParticipant>>

    @GET(value = "messages")
    suspend fun getMessages(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkMessage>>

    @GET(value = "users")
    suspend fun getUsers(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkUser>>

    @GET(value = "profiles")
    suspend fun getProfiles(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkProfile>>

    @GET(value = "payments")
    suspend fun getPayments(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkPayment>>

    @GET(value = "invoices")
    suspend fun getInvoices(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkInvoice>>

    @GET(value = "paymentSchedules")
    suspend fun getPaymentSchedules(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkPaymentSchedule>>

    @GET(value = "rentalAgreements")
    suspend fun getRentalAgreements(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalAgreement>>

    @GET(value = "rentalInvites")
    suspend fun getRentalInvites(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalInvite>>

    @GET(value = "rentalOffers")
    suspend fun getRentalOffers(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalOffer>>

    @GET(value = "images")
    suspend fun getImages(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkImage>>

    @GET(value = "changelists/properties")
    suspend fun getPropertyChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/chats")
    suspend fun getChatChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/chatParticipants")
    suspend fun getChatParticipantChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/messages")
    suspend fun getMessageChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/users")
    suspend fun getUserChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/profiles")
    suspend fun getProfileChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/payments")
    suspend fun getPaymentChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/invoices")
    suspend fun getInvoiceChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/paymentSchedules")
    suspend fun getPaymentScheduleChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/rentalAgreements")
    suspend fun getRentalAgreementChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/rentalInvites")
    suspend fun getRentalInviteChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/rentalOffers")
    suspend fun getRentalOfferChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/images")
    suspend fun getImageChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>
}

private const val PM_BASE_URL = BuildConfig.BACKEND_URL

@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

@Singleton
internal class RetrofitPmNetwork @Inject constructor(
    networkJson: Json,
    okHttpFactory: dagger.Lazy<Call.Factory>
) : PmNetworkDataSource {

    private val networkApi = trace("RetrofitPmNetwork") {
        Retrofit.Builder()
            .baseUrl(PM_BASE_URL)
            .callFactory { okHttpFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(RetrofitPmNetworkApi::class.java)
    }

    override suspend fun getUsers(ids: List<String>?): List<NetworkUser> =
        networkApi.getUsers(ids).data

    override suspend fun getProfiles(ids: List<String>?): List<NetworkProfile> =
        networkApi.getProfiles(ids).data

    override suspend fun getProperties(ids: List<String>?): List<NetworkProperty> =
        networkApi.getProperties(ids).data

    override suspend fun getChats(ids: List<String>?): List<NetworkChat> =
        networkApi.getChats(ids).data

    override suspend fun getChatParticipants(ids: List<String>?): List<NetworkChatParticipant> =
        networkApi.getChatParticipants(ids).data

    override suspend fun getMessages(ids: List<String>?): List<NetworkMessage> =
        networkApi.getMessages(ids).data

    override suspend fun getChatChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getChatChangeList(after = after)

    override suspend fun getChatParticipantChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getChatParticipantChangeList(after = after)

    override suspend fun getMessageChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getMessageChangeList(after = after)

    override suspend fun getPayments(ids: List<String>?): List<NetworkPayment> =
        networkApi.getPayments(ids).data

    override suspend fun getInvoices(ids: List<String>?): List<NetworkInvoice> =
        networkApi.getInvoices(ids).data

    override suspend fun getPaymentSchedules(ids: List<String>?): List<NetworkPaymentSchedule> =
        networkApi.getPaymentSchedules(ids).data

    override suspend fun getPaymentChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPaymentChangeList(after = after)

    override suspend fun getInvoiceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getInvoiceChangeList(after = after)

    override suspend fun getPaymentScheduleChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPaymentScheduleChangeList(after = after)

    override suspend fun getRentalAgreements(ids: List<String>?): List<NetworkRentalAgreement> =
        networkApi.getRentalAgreements(ids).data

    override suspend fun getRentalInvites(ids: List<String>?): List<NetworkRentalInvite> =
        networkApi.getRentalInvites(ids).data

    override suspend fun getRentalOffers(ids: List<String>?): List<NetworkRentalOffer> =
        networkApi.getRentalOffers(ids).data

    override suspend fun getRentalAgreementChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalAgreementChangeList(after = after)

    override suspend fun getRentalInviteChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalInviteChangeList(after = after)

    override suspend fun getRentalOfferChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalOfferChangeList(after = after)

    override suspend fun getImages(ids: List<String>?): List<NetworkImage> =
        networkApi.getImages(ids).data

    override suspend fun getImageChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getImageChangeList(after = after)

    override suspend fun getPropertyChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPropertyChangeList(after = after)

    override suspend fun getUserChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getUserChangeList(after = after)

    override suspend fun getProfileChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getProfileChangeList(after = after)
}