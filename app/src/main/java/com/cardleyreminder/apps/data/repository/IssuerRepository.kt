package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.issuer.AddNewCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewIssuerCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.CardNetworkCreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.CreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.EditCardRequest
import com.cardleyreminder.apps.data.model.issuer.EditRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardNetworkRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerCardListRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardTypeRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerBankListRequest
import com.cardleyreminder.apps.data.model.issuer.GetUserCardNetworkCardListRequest
import com.cardleyreminder.apps.data.network.IssuerApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class IssuerRepository(
    private val api : IssuerApi
) : BaseRepository() {

    suspend fun getIssuerList() = apiCall {
            api.issuerList()
    }

    suspend fun getIssuerBankList(request: GetIssuerBankListRequest) = apiCall {
            api.issuerBankList(request)
    }
    suspend fun getCardNetworkListOfIssuer() = apiCall {
            api.getCardNetworkListOfIssuer()
    }

    suspend fun getCardTypeList(getCardTypeRequest: GetCardTypeRequest) = apiCall {
        api.cardTypeList(getCardTypeRequest)
    }

    suspend fun getCardList(request: GetIssuerCardListRequest) = apiCall {
        api.issuerCardList(request)
    }

    suspend fun getCardColorList() = apiCall {
        api.cardColorList()
    }

    suspend fun getCardNetwork(getCardNetworkRequest: GetCardNetworkRequest) = apiCall {
        api.cardNetwork(getCardNetworkRequest)
    }
    suspend fun cardOnNetwork() = apiCall {
        api.cardOnNetwork()
    }
    suspend fun getUserCardNetworkCardList(
        request: GetUserCardNetworkCardListRequest
    ) = apiCall {
        api.getUserCardNetworkCardList(
            request
        )
    }

    suspend fun addNewCard(addCardRequest: AddNewCardRequest) = apiCall{
        api.addNewCard(addCardRequest)
    }

    suspend fun addNewRetailCard(addCardRequest: AddNewRetailCardRequest) = apiCall{
        api.addNewRetailCard(addCardRequest)
    }
    suspend fun addNewIssuerCard(addCardRequest: AddNewIssuerCardRequest) = apiCall{
        api.addNewIssuerCard(addCardRequest)
    }

    suspend fun editCard(request: EditCardRequest) = apiCall{
        api.editCard(request)
    }
    suspend fun editRetailCard(request: EditRetailCardRequest) = apiCall{
        api.editRetailCard(request)
    }

    suspend fun getCreditCardListData(creditCardListDataRequest: CreditCardListDataRequest) = apiCall {
        api.getCreditCardData(creditCardListDataRequest)
    }

    suspend fun getCardNetworkCreditCardListData(cardNetworkCreditCardListDataRequest: CardNetworkCreditCardListDataRequest) = apiCall {
        api.getCardNetworkCreditCardData(cardNetworkCreditCardListDataRequest)
    }

    suspend fun getCardDetails(request: GetCardDetailsRequest) = apiCall {
        api.getCardDetails(request)
    }

    suspend fun uploadImage(
        cardDesignImageBody: MultipartBody.Part,
        cardNetworkId: RequestBody,
        issuerCardName: RequestBody,
        cardTypeId: RequestBody,
        issuerId: RequestBody,
        bankId: RequestBody
    ) = apiCall {
        api.uploadCardDesign(cardDesignImageBody,
            cardNetworkId,
            issuerCardName,
            cardTypeId,
            issuerId,
            bankId)
    }

    suspend fun deleteCard(request: GetCardDetailsRequest)= apiCall {
        api.deleteCard(request)
    }
}