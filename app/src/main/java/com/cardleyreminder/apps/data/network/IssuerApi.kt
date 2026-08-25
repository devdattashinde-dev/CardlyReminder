package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.home.HomeResponse
import com.cardleyreminder.apps.data.model.issuer.AddNewCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewIssuerCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.issuer.CardColorListResponse
import com.cardleyreminder.apps.data.model.issuer.CardDetailsResponse
import com.cardleyreminder.apps.data.model.issuer.CardNetworkCreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.CardNetworkListResponse
import com.cardleyreminder.apps.data.model.issuer.IssuerCardListResponse
import com.cardleyreminder.apps.data.model.issuer.CardTypeListResponse
import com.cardleyreminder.apps.data.model.issuer.CreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.EditCardRequest
import com.cardleyreminder.apps.data.model.issuer.EditRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardNetworkRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerCardListRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardTypeRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerBankListRequest
import com.cardleyreminder.apps.data.model.issuer.GetUserCardNetworkCardListRequest
import com.cardleyreminder.apps.data.model.issuer.IssuerBankListResponse
import com.cardleyreminder.apps.data.model.issuer.IssuerListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IssuerApi {

    @GET("api/getIssuerList")
    suspend fun issuerList(
    ) : IssuerListResponse

    @GET("api/getCardNetworkListOfIssuer")
    suspend fun getCardNetworkListOfIssuer(
    ) : CardNetworkListResponse

    @POST("api/getIssuerBankList")
    suspend fun issuerBankList(
        @Body request: GetIssuerBankListRequest
    ) : IssuerBankListResponse

    @POST("api/getIssuerCardNetworkList")
    suspend fun cardNetwork(
        @Body request: GetCardNetworkRequest
    ): CardNetworkListResponse
    @GET("api/getUserCardNetworkList")
    suspend fun cardOnNetwork(
    ): CardNetworkListResponse

    @POST("api/getUserCardNetworkCardList")
    suspend fun getUserCardNetworkCardList(
        @Body request: GetUserCardNetworkCardListRequest
    ) : HomeResponse

    @POST("api/getCardTypeList")
    suspend fun cardTypeList(
        @Body request: GetCardTypeRequest
    ) : CardTypeListResponse


    @POST("api/getIssuerCardList")
    suspend fun issuerCardList(
        @Body request: GetIssuerCardListRequest
    ) : IssuerCardListResponse


    @GET("api/getCardColorList")
    suspend fun cardColorList(
    ) : CardColorListResponse


    @POST("api/addNewCard")
    suspend fun addNewCard(
        @Body request: AddNewCardRequest
    ): CommonResponse

    @POST("api/addNewRetailCard")
    suspend fun addNewRetailCard(
        @Body request: AddNewRetailCardRequest
    ): CommonResponse

    @POST("api/addNewIssuerCard")
    suspend fun addNewIssuerCard(
        @Body request: AddNewIssuerCardRequest
    ): CommonResponse

    @POST("api/editNewCard")
    suspend fun editCard(
        @Body request: EditCardRequest
    ): CommonResponse

    @POST("api/editNewRetailCard")
    suspend fun editRetailCard(
        @Body request: EditRetailCardRequest
    ): CommonResponse


    @POST("api/getMenuIssuerCreditCardList")
    suspend fun getCreditCardData(
        @Body creditCardListDataRequest : CreditCardListDataRequest
    ) : HomeResponse

    @POST("api/getMenuCardNetworkCreditCardList")
    suspend fun getCardNetworkCreditCardData(
        @Body cardNetworkCreditCardListDataRequest : CardNetworkCreditCardListDataRequest
    ) : HomeResponse

    @POST("api/cardDetail")
    suspend fun getCardDetails(
        @Body request: GetCardDetailsRequest
    ) : CardDetailsResponse

    @POST("api/addMenuIssuerCardForm")
    @Multipart
    suspend fun uploadCardDesign(
        @Part cardDesignImageBody: MultipartBody.Part,
        @Part ("card_network_id") cardNetworkId: RequestBody,
        @Part ("issuer_card_name") issuerCardName: RequestBody,
        @Part ("card_type_id") cardTypeId: RequestBody,
        @Part ("issuer_id") issuerId: RequestBody,
        @Part ("bank_id") bankId: RequestBody
    ): CommonResponse


    @POST("api/deleteCard")
    suspend fun deleteCard(
        @Body request : GetCardDetailsRequest
    ) : CommonResponse
}