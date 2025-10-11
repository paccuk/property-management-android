package org.stkachenko.propertymanagement.core.network.retrofit

import androidx.tracing.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.stkachenko.propertymanagement.core.network.BuildConfig
import org.stkachenko.propertymanagement.core.network.ProtectedNetworkDataSource
import org.stkachenko.propertymanagement.core.network.model.NetworkChangeList
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChat
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkChatParticipant
import org.stkachenko.propertymanagement.core.network.model.chat.NetworkMessage
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkInvoice
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPayment
import org.stkachenko.propertymanagement.core.network.model.payment.NetworkPaymentSchedule
import org.stkachenko.propertymanagement.core.network.model.property.NetworkProperty
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalAgreement
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalInvite
import org.stkachenko.propertymanagement.core.network.model.rental.NetworkRentalOffer
import org.stkachenko.propertymanagement.core.network.model.user.NetworkUser
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private interface RetrofitPmNetworkApi {
    @GET(value = "properties")
    suspend fun getProperties(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkProperty>>

    @POST(value = "properties")
    suspend fun createProperties(
        @Body properties: List<NetworkProperty>,
    ): NetworkResponse<List<NetworkProperty>>

    @PUT(value = "properties")
    suspend fun updateProperties(
        @Body properties: List<NetworkProperty>,
    ): NetworkResponse<List<NetworkProperty>>

    @HTTP(method = "DELETE", path = "properties", hasBody = true)
    suspend fun deleteProperty(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "users")
    suspend fun getUsers(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkUser>>

    @POST(value = "users")
    suspend fun createUsers(
        @Body users: List<NetworkUser>,
    ): NetworkResponse<List<NetworkUser>>

    @PUT(value = "users")
    suspend fun updateUsers(
        @Body users: List<NetworkUser>,
    ): NetworkResponse<List<NetworkUser>>

    @HTTP(method = "DELETE", path = "users", hasBody = true)
    suspend fun deleteUsers(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "payments")
    suspend fun getPayments(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkPayment>>

    @POST(value = "payments")
    suspend fun createPayments(
        @Body payments: List<NetworkPayment>,
    ): NetworkResponse<List<NetworkPayment>>

    @PUT(value = "payments")
    suspend fun updatePayments(
        @Body payments: List<NetworkPayment>,
    ): NetworkResponse<List<NetworkPayment>>

    @HTTP(method = "DELETE", path = "payments", hasBody = true)
    suspend fun deletePayments(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "invoices")
    suspend fun getInvoices(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkInvoice>>

    @POST(value = "invoices")
    suspend fun createInvoices(
        @Body invoices: List<NetworkInvoice>,
    ): NetworkResponse<List<NetworkInvoice>>

    @PUT(value = "invoices")
    suspend fun updateInvoices(
        @Body invoices: List<NetworkInvoice>,
    ): NetworkResponse<List<NetworkInvoice>>

    @HTTP(method = "DELETE", path = "invoices", hasBody = true)
    suspend fun deleteInvoices(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "paymentSchedules")
    suspend fun getPaymentSchedules(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkPaymentSchedule>>

    @POST(value = "paymentSchedules")
    suspend fun createPaymentSchedules(
        @Body paymentSchedules: List<NetworkPaymentSchedule>,
    ): NetworkResponse<List<NetworkPaymentSchedule>>

    @PUT(value = "paymentSchedules")
    suspend fun updatePaymentSchedules(
        @Body paymentSchedules: List<NetworkPaymentSchedule>,
    ): NetworkResponse<List<NetworkPaymentSchedule>>

    @HTTP(method = "DELETE", path = "paymentSchedules", hasBody = true)
    suspend fun deletePaymentSchedules(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "rentalAgreements")
    suspend fun getRentalAgreements(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalAgreement>>

    @POST(value = "rentalAgreements")
    suspend fun createRentalAgreements(
        @Body rentalAgreements: List<NetworkRentalAgreement>,
    ): NetworkResponse<List<NetworkRentalAgreement>>

    @PUT(value = "rentalAgreements")
    suspend fun updateRentalAgreements(
        @Body rentalAgreements: List<NetworkRentalAgreement>,
    ): NetworkResponse<List<NetworkRentalAgreement>>

    @HTTP(method = "DELETE", path = "rentalAgreements", hasBody = true)
    suspend fun deleteRentalAgreements(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "rentalInvites")
    suspend fun getRentalInvites(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalInvite>>

    @POST(value = "rentalInvites")
    suspend fun createRentalInvites(
        @Body rentalInvites: List<NetworkRentalInvite>,
    ): NetworkResponse<List<NetworkRentalInvite>>

    @PUT(value = "rentalInvites")
    suspend fun updateRentalInvites(
        @Body rentalInvites: List<NetworkRentalInvite>,
    ): NetworkResponse<List<NetworkRentalInvite>>

    @HTTP(method = "DELETE", path = "rentalInvites", hasBody = true)
    suspend fun deleteRentalInvites(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

    @GET(value = "rentalOffers")
    suspend fun getRentalOffers(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkRentalOffer>>

    @POST(value = "rentalOffers")
    suspend fun createRentalOffers(
        @Body rentalOffers: List<NetworkRentalOffer>,
    ): NetworkResponse<List<NetworkRentalOffer>>

    @PUT(value = "rentalOffers")
    suspend fun updateRentalOffers(
        @Body rentalOffers: List<NetworkRentalOffer>,
    ): NetworkResponse<List<NetworkRentalOffer>>

    @HTTP(method = "DELETE", path = "rentalOffers", hasBody = true)
    suspend fun deleteRentalOffers(
        @Body ids: List<String>,
    ): NetworkResponse<Unit>

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
internal class ProtectedRetrofitNetwork @Inject constructor(
    networkJson: Json,
    @Named("protected") okHttpFactory: dagger.Lazy<Call.Factory>,
) : ProtectedNetworkDataSource {

    private val networkApi = trace("ProtectedRetrofitNetwork") {
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

    override suspend fun createUsers(users: List<NetworkUser>): List<NetworkUser> =
        networkApi.createUsers(users).data

    override suspend fun updateUsers(users: List<NetworkUser>): List<NetworkUser> =
        networkApi.updateUsers(users).data

    override suspend fun deleteUsers(ids: List<String>): Unit =
        networkApi.deleteUsers(ids).data

    override suspend fun getProperties(ids: List<String>?): List<NetworkProperty> =
        networkApi.getProperties(ids).data

    override suspend fun createProperties(properties: List<NetworkProperty>): List<NetworkProperty> =
        networkApi.createProperties(properties).data

    override suspend fun updateProperties(properties: List<NetworkProperty>): List<NetworkProperty> =
        networkApi.updateProperties(properties).data

    override suspend fun deleteProperty(ids: List<String>): Unit =
        networkApi.deleteProperty(ids).data

    override suspend fun getPayments(ids: List<String>?): List<NetworkPayment> =
        networkApi.getPayments(ids).data

    override suspend fun createPayments(payments: List<NetworkPayment>): List<NetworkPayment> =
        networkApi.createPayments(payments).data

    override suspend fun updatePayments(payments: List<NetworkPayment>): List<NetworkPayment> =
        networkApi.updatePayments(payments).data

    override suspend fun deletePayments(ids: List<String>): Unit =
        networkApi.deletePayments(ids).data

    override suspend fun getInvoices(ids: List<String>?): List<NetworkInvoice> =
        networkApi.getInvoices(ids).data

    override suspend fun createInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice> =
        networkApi.createInvoices(invoices).data

    override suspend fun updateInvoices(invoices: List<NetworkInvoice>): List<NetworkInvoice> =
        networkApi.updateInvoices(invoices).data

    override suspend fun deleteInvoices(ids: List<String>): Unit =
        networkApi.deleteInvoices(ids).data

    override suspend fun getPaymentSchedules(ids: List<String>?): List<NetworkPaymentSchedule> =
        networkApi.getPaymentSchedules(ids).data

    override suspend fun createPaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule> =
        networkApi.createPaymentSchedules(paymentSchedules).data

    override suspend fun updatePaymentSchedules(paymentSchedules: List<NetworkPaymentSchedule>): List<NetworkPaymentSchedule> =
        networkApi.updatePaymentSchedules(paymentSchedules).data

    override suspend fun deletePaymentSchedules(ids: List<String>): Unit =
        networkApi.deletePaymentSchedules(ids).data

    override suspend fun getRentalAgreements(ids: List<String>?): List<NetworkRentalAgreement> =
        networkApi.getRentalAgreements(ids).data

    override suspend fun createRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement> =
        networkApi.createRentalAgreements(rentalAgreements).data

    override suspend fun updateRentalAgreements(rentalAgreements: List<NetworkRentalAgreement>): List<NetworkRentalAgreement> =
        networkApi.updateRentalAgreements(rentalAgreements).data

    override suspend fun deleteRentalAgreements(ids: List<String>): Unit =
        networkApi.deleteRentalAgreements(ids).data

    override suspend fun getRentalInvites(ids: List<String>?): List<NetworkRentalInvite> =
        networkApi.getRentalInvites(ids).data

    override suspend fun createRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite> =
        networkApi.createRentalInvites(rentalInvites).data

    override suspend fun updateRentalInvites(rentalInvites: List<NetworkRentalInvite>): List<NetworkRentalInvite> =
        networkApi.updateRentalInvites(rentalInvites).data

    override suspend fun deleteRentalInvites(ids: List<String>): Unit =
        networkApi.deleteRentalInvites(ids).data

    override suspend fun getRentalOffers(ids: List<String>?): List<NetworkRentalOffer> =
        networkApi.getRentalOffers(ids).data

    override suspend fun createRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer> =
        networkApi.createRentalOffers(rentalOffers).data

    override suspend fun updateRentalOffers(rentalOffers: List<NetworkRentalOffer>): List<NetworkRentalOffer> =
        networkApi.updateRentalOffers(rentalOffers).data

    override suspend fun deleteRentalOffers(ids: List<String>): Unit =
        networkApi.deleteRentalOffers(ids).data

    override suspend fun getChats(ids: List<String>?): List<NetworkChat> =
        networkApi.getChats(ids).data

    override suspend fun getChatParticipants(ids: List<String>?): List<NetworkChatParticipant> =
        networkApi.getChatParticipants(ids).data

    override suspend fun getMessages(ids: List<String>?): List<NetworkMessage> =
        networkApi.getMessages(ids).data

    override suspend fun getRentalAgreementChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalAgreementChangeList(after = after)

    override suspend fun getRentalInviteChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalInviteChangeList(after = after)

    override suspend fun getRentalOfferChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getRentalOfferChangeList(after = after)

    override suspend fun getPropertyChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPropertyChangeList(after = after)

    override suspend fun getUserChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getUserChangeList(after = after)

    override suspend fun getPaymentChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPaymentChangeList(after = after)

    override suspend fun getInvoiceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getInvoiceChangeList(after = after)

    override suspend fun getPaymentScheduleChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getPaymentScheduleChangeList(after = after)

    override suspend fun getChatChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getChatChangeList(after = after)

    override suspend fun getChatParticipantChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getChatParticipantChangeList(after = after)

    override suspend fun getMessageChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getMessageChangeList(after = after)
}