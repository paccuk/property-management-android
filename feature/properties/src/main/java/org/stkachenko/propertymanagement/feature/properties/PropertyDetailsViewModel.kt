package org.stkachenko.propertymanagement.feature.properties

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.stkachenko.propertymanagement.core.data.repository.usersession.UserSessionRepository
import org.stkachenko.propertymanagement.core.data.util.SyncManager
import org.stkachenko.propertymanagement.core.domain.property.GetPropertyByIdUseCase
import org.stkachenko.propertymanagement.core.ui.property.PropertyUiState
import org.stkachenko.propertymanagement.core.ui.property.UserRoleState
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    syncManager: SyncManager,
    private val userSessionRepository: UserSessionRepository,
    private val getPropertyById: GetPropertyByIdUseCase,
    // TODO: Inject when implemented
    // private val getRentalAgreementsByProperty: GetRentalAgreementsByProperty,
    // private val getRentalOffersByProperty: GetRentalOffersByProperty,
    // private val getRentalInvitesByProperty: GetRentalInvitesByProperty,
    // private val getPaymentsByProperty: GetPaymentsByProperty,
) : ViewModel() {

    private val propertyId: String = savedStateHandle.get<String>("propertyId").orEmpty()

    val isSyncing: StateFlow<Boolean> = syncManager.isSyncing.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    val propertyState: StateFlow<PropertyUiState> =
        if (propertyId.isBlank()) {
            flowOf(PropertyUiState.NotFound).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PropertyUiState.Loading,
            )
        } else {
            getPropertyById(propertyId).map { property ->
                if (property != null) PropertyUiState.Success(property) else PropertyUiState.NotFound
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PropertyUiState.Loading,
            )
        }

    val userRole: StateFlow<UserRoleState> = userSessionRepository.userSessionData
        .map { UserRoleState.Success(it.userRole) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserRoleState.Loading,
        )

    // TODO: Expose flows for agreements/offers/invites/payments when use cases are added
    // val rentalAgreements: StateFlow<List<RentalAgreement>> = ...
    // val rentalOffers: StateFlow<List<RentalOffer>> = ...
    // val rentalInvites: StateFlow<List<RentalInvite>> = ...
    // val payments: StateFlow<List<Payment>> = ...
}
