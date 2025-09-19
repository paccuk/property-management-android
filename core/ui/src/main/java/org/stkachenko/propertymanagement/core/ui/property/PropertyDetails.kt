package org.stkachenko.propertymanagement.core.ui.property

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import org.stkachenko.propertymanagement.core.designsystem.R.drawable
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme
import org.stkachenko.propertymanagement.core.model.data.image.Image
import org.stkachenko.propertymanagement.core.model.data.payment.Payment
import org.stkachenko.propertymanagement.core.model.data.property.Property
import org.stkachenko.propertymanagement.core.model.data.rental.RentalAgreement
import org.stkachenko.propertymanagement.core.model.data.rental.RentalInvite
import org.stkachenko.propertymanagement.core.model.data.rental.RentalOffer
import org.stkachenko.propertymanagement.core.model.data.user.UserRole
import org.stkachenko.propertymanagement.core.ui.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PropertyContent(
    propertyState: PropertyUiState,
    userRole: UserRoleState,
) {
    // State for displaying menus
    var showEditProperty by remember { mutableStateOf(false) }
    var showCreateAgreement by remember { mutableStateOf(false) }
    var showViewOffers by remember { mutableStateOf(false) }
    var showViewInvites by remember { mutableStateOf(false) }
    var showViewAgreements by remember { mutableStateOf(false) }
    var showManagePayments by remember { mutableStateOf(false) }

    when (propertyState) {
        PropertyUiState.Loading -> Unit
        PropertyUiState.NotFound -> NotFoundState()
        is PropertyUiState.Success -> {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scroll)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                HeaderSection(propertyState.property)
                AttributesSection(propertyState.property)
                RoleActionsSection(
                    userRole = userRole,
                    onEditProperty = { showEditProperty = true },
                    onCreateAgreement = { showCreateAgreement = true },
                    onViewOffers = { showViewOffers = true },
                    onViewInvites = { showViewInvites = true },
                    onViewAgreements = { showViewAgreements = true },
                    onManagePayments = { showManagePayments = true }
                )
                AgreementsInvitesSection(userRole)
                PaymentsSection(userRole)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Display menus when the corresponding state is activated
            if (showEditProperty) {
                EditPropertyDialog(
                    property = propertyState.property,
                    onDismiss = { showEditProperty = false },
                    onSave = { /* TODO: save changes via ViewModel */
                        showEditProperty = false
                    }
                )
            }

            if (showCreateAgreement) {
                CreateAgreementDialog(
                    property = propertyState.property,
                    onDismiss = { showCreateAgreement = false },
                    onCreate = { /* TODO: create agreement via ViewModel */
                        showCreateAgreement = false
                    }
                )
            }

            if (showViewOffers) {
                ViewOffersDialog(
                    offers = emptyList(), // TODO: get from ViewModel
                    onDismiss = { showViewOffers = false }
                )
            }

            if (showViewInvites) {
                ViewInvitesDialog(
                    invites = emptyList(), // TODO: get from ViewModel
                    onDismiss = { showViewInvites = false }
                )
            }

            if (showViewAgreements) {
                ViewAgreementsDialog(
                    agreements = emptyList(), // TODO: get from ViewModel
                    onDismiss = { showViewAgreements = false }
                )
            }

            if (showManagePayments) {
                ManagePaymentsDialog(
                    payments = emptyList(), // TODO: get from ViewModel
                    onDismiss = { showManagePayments = false }
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(
    property: Property,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column {
            Row {
                PropertyDetailsHeaderImages(property.images)
            }
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Text(
                        text = property.address.readableAddress(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${property.price} ${property.currency}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.core_ui_property_details_header_type,
                            property.type
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.core_ui_property_details_header_area,
                            property.area
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.core_ui_property_details_header_available,
                            if (property.isAvailable) stringResource(R.string.core_ui_property_details_header_available_yes) else stringResource(
                                R.string.core_ui_property_details_header_available_no
                            )
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

private fun Map<String, String>.readableAddress(): String {
    val parts = listOfNotNull(
        this["fullAddress"],
        this["street"],
        this["city"],
        this["zip"],
        this["country"]
    ).ifEmpty { values.toList() }
    return parts.filter { it.isNotBlank() }.distinct().joinToString(", ")
}

@Composable
private fun PropertyDetailsHeaderImages(
    images: List<Image>,
) {
    if (images.isEmpty()) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(drawable.core_designsystem_ic_placeholder_default),
            contentDescription = null
        )
        return
    }

    val pagerState = rememberPagerState { images.minByOrNull { it.position }!!.position }
    val imageLoaders = images.map { image ->
        rememberAsyncImagePainter(model = image.url)
    }

    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->

            val imageLoader = imageLoaders[page]
            val state = imageLoader.state

            if (state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop,
                painter = if (state !is AsyncImagePainter.State.Error && !isLocalInspection) {
                    imageLoader
                } else {
                    painterResource(drawable.core_designsystem_ic_placeholder_default)
                },
                contentDescription = null,
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(images.size) { index ->
                val color = if (pagerState.currentPage == index) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)

                )
            }
        }
    }
}

@Composable
private fun AttributesSection(property: Property) {
    if (property.attributes.isEmpty()) return

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.core_ui_property_details_attributes_card),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                property.attributes.forEach { (k, v) ->
                    Text("$k: $v", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun RoleActionsSection(
    userRole: UserRoleState,
    onEditProperty: () -> Unit = {},
    onCreateAgreement: () -> Unit = {},
    onViewOffers: () -> Unit = {},
    onViewInvites: () -> Unit = {},
    onViewAgreements: () -> Unit = {},
    onManagePayments: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                when (userRole) {
                    is UserRoleState.Success -> when (userRole.userRole) {
                        UserRole.OWNER -> OwnerActions(
                            onEditProperty = onEditProperty,
                            onCreateAgreement = onCreateAgreement,
                            onViewOffers = onViewOffers,
                            onViewInvites = onViewInvites,
                            onViewAgreements = onViewAgreements,
                            onManagePayments = onManagePayments
                        )

                        UserRole.TENANT -> TenantActions()
                    }

                    UserRoleState.Loading -> Text(
                        text = "Loading role...",
                        style = MaterialTheme.typography.bodySmall
                    )

                    UserRoleState.Error -> Text(
                        text = "Role error",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun OwnerActions(
    onEditProperty: () -> Unit = {},
    onCreateAgreement: () -> Unit = {},
    onViewOffers: () -> Unit = {},
    onViewInvites: () -> Unit = {},
    onViewAgreements: () -> Unit = {},
    onManagePayments: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onEditProperty,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_edit_property_button))
            }
            Button(
                onClick = onCreateAgreement,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_create_agreement_button))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onViewOffers,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_view_offers_button))
            }
            OutlinedButton(
                onClick = onViewInvites,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_view_invites_button))
            }
        }

        Row {
            OutlinedButton(
                onClick = onViewAgreements,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_view_agreements_button))
            }
        }

        Row {
            OutlinedButton(
                onClick = onManagePayments,
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(R.string.core_ui_property_details_owner_manage_payments_button))
            }
        }
    }
}

@Composable
private fun TenantActions() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {

            Button(
                onClick = { /* TODO send offer */ },
                modifier = Modifier.weight(1f),
            ) {
                Text("Send offer")
            }
        }

        Row {
            OutlinedButton(
                onClick = { /* TODO view agreements */ },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.core_ui_property_details_tenant_my_agreements_button))
            }
        }

        Row {
            OutlinedButton(
                onClick = { /* TODO view payments */ },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.core_ui_property_details_tenant_payment_history_button))
            }
        }
    }
}

@Composable
private fun AgreementsInvitesSection(userRole: UserRoleState) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.core_ui_property_details_agreements_invitations_card),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                // TODO bind agreements / offers / invites flows from ViewModel when implemented
                when (userRole) {
                    is UserRoleState.Success -> when (userRole.userRole) {
                        UserRole.OWNER -> Text("Owner agreements/offers/invites placeholder")
                        UserRole.TENANT -> Text("Tenant agreements/offers/invites placeholder")
                    }

                    UserRoleState.Loading -> Text(
                        "Loading...",
                        style = MaterialTheme.typography.bodySmall
                    )

                    UserRoleState.Error -> Text(
                        "Failed to load",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentsSection(userRole: UserRoleState) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.core_ui_property_details_manage_payments_dialog_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                // TODO bind payments flow from ViewModel
                when (userRole) {
                    is UserRoleState.Success -> when (userRole.userRole) {
                        UserRole.OWNER -> Text("Owner payment management placeholder")
                        UserRole.TENANT -> Text("Tenant payment history placeholder")
                        else -> Text("Unsupported role")
                    }

                    UserRoleState.Loading -> Text(
                        "Loading payments...",
                        style = MaterialTheme.typography.bodySmall
                    )

                    UserRoleState.Error -> Text(
                        "Failed to load payments",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

sealed interface PropertyUiState {
    data object Loading : PropertyUiState
    data object NotFound : PropertyUiState
    data class Success(val property: Property) : PropertyUiState
}

@Composable
private fun NotFoundState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Property not found", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Preview(device = Devices.TABLET)
@Composable
private fun PropertyDetailsOwnerContentPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                PropertyContent(
                    propertyState = PropertyUiState.Success(propertiesList.first()),
                    userRole = UserRoleState.Success(UserRole.OWNER),
                )
            }
        }
    }
}

@Preview
@Preview(device = Devices.TABLET)
@Composable
private fun PropertyDetailsTenantContentPreview(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                PropertyContent(
                    propertyState = PropertyUiState.Success(propertiesList.first()),
                    userRole = UserRoleState.Success(UserRole.TENANT),
                )
            }
        }
    }
}

@Preview(heightDp = 1000)
@Composable
private fun PropertyDetailsContentPreviewFullHeight(
    @PreviewParameter(UserPropertyPreviewParameterProvider::class)
    propertiesList: List<Property>
) {
    CompositionLocalProvider(
        LocalInspectionMode provides true
    ) {
        PmTheme {
            Surface {
                PropertyContent(
                    propertyState = PropertyUiState.Success(propertiesList.first()),
                    userRole = UserRoleState.Success(UserRole.OWNER),
                )
            }
        }
    }
}

// Edit property dialog
@Composable
private fun EditPropertyDialog(
    property: Property,
    onDismiss: () -> Unit,
    onSave: (Property) -> Unit
) {
    var price by remember { mutableStateOf(property.price.toString()) }
    var area by remember { mutableStateOf(property.area.toString()) }
    var currency by remember { mutableStateOf(property.currency) }
    var address by remember { mutableStateOf(property.address["fullAddress"] ?: "") }
    var isAvailable by remember { mutableStateOf(property.isAvailable) }

    // Field validation
    val isPriceValid = price.toDoubleOrNull() != null
    val isAreaValid = area.toDoubleOrNull() != null
    val isValid = isPriceValid && isAreaValid && currency.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_edit_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_edit_dialog_price_label)) },
                    isError = price.isNotBlank() && !isPriceValid,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_edit_dialog_area_label)) },
                    isError = area.isNotBlank() && !isAreaValid,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currency,
                    onValueChange = { currency = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_edit_dialog_currency_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_edit_dialog_address_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(stringResource(R.string.core_ui_property_details_edit_dialog_availability_label))
                    Button(
                        onClick = { isAvailable = !isAvailable },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            if (isAvailable) stringResource(R.string.core_ui_property_details_edit_dialog_available) else stringResource(
                                R.string.core_ui_property_details_edit_dialog_not_available
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedProperty = property.copy(
                        price = price.toDoubleOrNull() ?: property.price,
                        area = area.toDoubleOrNull() ?: property.area,
                        currency = currency,
                        isAvailable = isAvailable,
                        address = property.address.toMutableMap().apply {
                            put("fullAddress", address)
                        },
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(updatedProperty)
                },
                enabled = isValid
            ) {
                Text(stringResource(R.string.core_ui_property_details_save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_cancel_button))
            }
        }
    )
}

// Create agreement dialog
@Composable
private fun CreateAgreementDialog(
    property: Property,
    onDismiss: () -> Unit,
    onCreate: (RentalAgreement) -> Unit
) {
    var offerId by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }
    var startDateText by remember { mutableStateOf("") }
    var endDateText by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("DRAFT") }

    // Field validation
    val isValid = offerId.isNotBlank() && tenantId.isNotBlank() &&
            startDateText.isNotBlank() && endDateText.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = offerId,
                    onValueChange = { offerId = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_offer_id_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tenantId,
                    onValueChange = { tenantId = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_tenant_id_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = startDateText,
                    onValueChange = { startDateText = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_start_date_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = endDateText,
                    onValueChange = { endDateText = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_end_date_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text(stringResource(R.string.core_ui_property_details_create_agreement_dialog_status_label)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(
                        R.string.core_ui_property_details_create_agreement_dialog_property_id,
                        property.id
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Example of date conversion (should be safer in a real application)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val startDate = try {
                        dateFormat.parse(startDateText)
                    } catch (e: Exception) {
                        Date()
                    }
                    val endDate = try {
                        dateFormat.parse(endDateText)
                    } catch (e: Exception) {
                        Date()
                    }

                    val now = System.currentTimeMillis()
                    val agreement = RentalAgreement(
                        id = "", // ID will be assigned by the repository
                        offerId = offerId,
                        tenantId = tenantId,
                        startDate = startDate,
                        endDate = endDate,
                        status = status,
                        createdAt = now,
                        updatedAt = now
                    )
                    onCreate(agreement)
                },
                enabled = isValid
            ) {
                Text(stringResource(R.string.core_ui_property_details_create_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_cancel_button))
            }
        }
    )
}

// View offers dialog
@Composable
private fun ViewOffersDialog(
    offers: List<RentalOffer>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_view_offers_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (offers.isEmpty()) {
                    Text(stringResource(R.string.core_ui_property_details_view_offers_dialog_no_offers))
                } else {
                    offers.forEach { offer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_offer_id,
                                        offer.id
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_price,
                                        offer.pricePerPerson,
                                        offer.currency
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_status,
                                        if (offer.isActive) stringResource(R.string.core_ui_property_details_view_offers_dialog_status_active) else stringResource(
                                            R.string.core_ui_property_details_view_offers_dialog_status_inactive
                                        )
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_max_tenants,
                                        offer.maxTenants
                                    )
                                )

                                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_available_from,
                                        dateFormat.format(offer.availableFrom)
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_offers_dialog_available_to,
                                        dateFormat.format(offer.availableTo)
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_close_button))
            }
        }
    )
}

// View invites dialog
@Composable
private fun ViewInvitesDialog(
    invites: List<RentalInvite>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_view_invites_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (invites.isEmpty()) {
                    Text(stringResource(R.string.core_ui_property_details_view_invites_dialog_no_invites))
                } else {
                    invites.forEach { invite ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_invite_id,
                                        invite.id
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_contact,
                                        invite.targetContact
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_type,
                                        invite.inviteType
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_status,
                                        invite.status
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_offer_id,
                                        invite.offerId
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_close_button))
            }
        }
    )
}

// View agreements dialog
@Composable
private fun ViewAgreementsDialog(
    agreements: List<RentalAgreement>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_view_agreements_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (agreements.isEmpty()) {
                    Text(stringResource(R.string.core_ui_property_details_view_agreements_dialog_no_agreements))
                } else {
                    agreements.forEach { agreement ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.core_ui_property_details_view_agreements_dialog_agreement_id,
                                        agreement.id
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_agreements_dialog_tenant,
                                        agreement.tenantId
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_agreements_dialog_status,
                                        agreement.status
                                    )
                                )

                                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_agreements_dialog_period,
                                        dateFormat.format(agreement.startDate),
                                        dateFormat.format(agreement.endDate)
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_view_invites_dialog_offer_id,
                                        agreement.offerId
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_close_button))
            }
        }
    )
}

// Manage payments dialog
@Composable
private fun ManagePaymentsDialog(
    payments: List<Payment>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.core_ui_property_details_manage_payments_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (payments.isEmpty()) {
                    Text(stringResource(R.string.core_ui_property_details_manage_payments_dialog_no_payments))
                } else {
                    payments.forEach { payment ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_payment_id,
                                        payment.id
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_amount,
                                        payment.amount,
                                        payment.currency
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_status,
                                        payment.status
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_method,
                                        payment.paymentMethod
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_from,
                                        payment.payerId
                                    )
                                )
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_to,
                                        payment.payeeId
                                    )
                                )

                                val dateFormat =
                                    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                val paidDate = Date(payment.paidAt)
                                Text(
                                    stringResource(
                                        R.string.core_ui_property_details_manage_payments_dialog_date,
                                        dateFormat.format(paidDate)
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.core_ui_property_details_close_button))
            }
        }
    )
}

