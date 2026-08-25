package com.cardleyreminder.apps.ui.issuer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.home.HomeResponse
import com.cardleyreminder.apps.data.model.issuer.AddNewCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewIssuerCardRequest
import com.cardleyreminder.apps.data.model.issuer.AddNewRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.BankDetail
import com.cardleyreminder.apps.data.model.issuer.Card
import com.cardleyreminder.apps.data.model.issuer.CardColorListResponse
import com.cardleyreminder.apps.data.model.issuer.CardDetail
import com.cardleyreminder.apps.data.model.issuer.CardDetailsResponse
import com.cardleyreminder.apps.data.model.issuer.CardNetwork
import com.cardleyreminder.apps.data.model.issuer.CardNetworkCreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.CardNetworkListResponse
import com.cardleyreminder.apps.data.model.issuer.CardType
import com.cardleyreminder.apps.data.model.issuer.CardTypeListResponse
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.issuer.CreditCardListDataRequest
import com.cardleyreminder.apps.data.model.issuer.EditCardRequest
import com.cardleyreminder.apps.data.model.issuer.EditRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardNetworkRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardTypeRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerBankListRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerCardListRequest
import com.cardleyreminder.apps.data.model.issuer.GetUserCardNetworkCardListRequest
import com.cardleyreminder.apps.data.model.issuer.IssuerBankListResponse
import com.cardleyreminder.apps.data.model.issuer.IssuerCardListResponse
import com.cardleyreminder.apps.data.model.issuer.IssuerListResponse
import com.cardleyreminder.apps.data.model.issuer.Issuers
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class IssuerViewModel(
    private val repository: IssuerRepository
) : ViewModel() {

    var selectedIssuerId: String? = null
    var selectedIssuerBankId: String? = null
    var selectedCardNetworkId: String? = null
    var selectedCardTypeId: String? = null
    var selectedCardId: String? = null

    //issuerList
    private val _issuerListResponse : MutableLiveData<Resource<IssuerListResponse>> = MutableLiveData()
    val issuerListResponse : LiveData<Resource<IssuerListResponse>>
        get() = _issuerListResponse

    private val _issuerList = MutableLiveData<List<Issuers>>()
    var issuerList: LiveData<List<Issuers>> = _issuerList

    fun getIssuerListData(
    ) = viewModelScope.launch {
        _issuerListResponse.value = repository.getIssuerList()
    }

    fun setSelectIssuer(selectedIssuer: Issuers) {
        selectedIssuerId = selectedIssuer.issuerId
        selectedIssuerId?.let {
            if(selectedIssuer.issuerId=="5") {
                getCardNetworkListOfIssuer()
            }else{
                getIssuerBankListData(GetIssuerBankListRequest(issuer_id = it))
            }
        }
    }

    fun issuerList(data: List<Issuers>) {
        _issuerList.value = data;
    }


    //Bank Details list
    private val _issuerBankListResponse : MutableLiveData<Resource<IssuerBankListResponse>> = MutableLiveData()

    val issuerBankListResponse : LiveData<Resource<IssuerBankListResponse>>
        get() = _issuerBankListResponse

    private val _banksList = MutableLiveData<List<BankDetail>>()
    var bankList: LiveData<List<BankDetail>> = _banksList

    fun getIssuerBankListData(request: GetIssuerBankListRequest
    ) = viewModelScope.launch {
        _issuerBankListResponse.value = repository.getIssuerBankList(request)
    }



    fun issuerBankList(data: List<BankDetail>) {
        _banksList.value = data;
    }

    /*fun setEmptyIssuerBankList(data: List<BankDetail>) {
        _banksList.value = emptyList()
    }*/

    fun setSelectIssuerBank(selectedString: BankDetail) {
        selectedIssuerBankId = selectedString.bankId
        selectedIssuerBankId?.let { getCardNetworkData(GetCardNetworkRequest(bank_id = it)) }
    }

    //card Color
    private val _cardColorListResponse : MutableLiveData<Resource<CardColorListResponse>> = MutableLiveData()

    val cardColorListResponse : LiveData<Resource<CardColorListResponse>>
        get() = _cardColorListResponse



    fun cardNetworkList(data: List<CardNetwork>) {
        _cardNetworkList.value = data
    }

    fun setSelectCardNetwork(selectedCardNetwork: CardNetwork) {
        selectedCardNetworkId = selectedCardNetwork.cardNetworkId
        if(!selectedIssuerId.equals("5"))
            selectedCardNetworkId?.let { getCardTypeListData(GetCardTypeRequest(card_network_id = it)) }
    }

    //card type list
    private val _cardTypeResponse : MutableLiveData<Resource<CardTypeListResponse>> = MutableLiveData()

    val cardTypeResponse : LiveData<Resource<CardTypeListResponse>>
        get() = _cardTypeResponse

    private val _cardTypeList = MutableLiveData<List<CardType>>()
    var cardTypeList: LiveData<List<CardType>> = _cardTypeList

    fun getCardTypeListData(getCardTypeRequest: GetCardTypeRequest) = viewModelScope.launch {
        _cardTypeResponse.value = repository.getCardTypeList(getCardTypeRequest)
    }
    fun setSelectCardType(cardType: CardType) {
        selectedCardTypeId = cardType.cardTypeId
        selectedCardTypeId?.let { getCardListData(GetIssuerCardListRequest(card_type_id = it, user_card_count = "1")) }
    }

    fun setSelectCardType(selectedString: String) {
        selectedCardTypeId = _cardTypeList.value?.filter {issuerDetail ->  issuerDetail.cardName.equals(selectedString, ignoreCase = true)}?.first()?.cardTypeId
        selectedCardTypeId?.let { getCardListData(GetIssuerCardListRequest(card_type_id = it, user_card_count = "1")) }
    }



    //card list
    private val _IssuerCardListResponse : MutableLiveData<Resource<IssuerCardListResponse>> = MutableLiveData()

    val issuerCardListResponse : LiveData<Resource<IssuerCardListResponse>>
        get() = _IssuerCardListResponse
    private val _cardDetailsList = MutableLiveData<List<CardDetail>>()
    var cardDetailsList: LiveData<List<CardDetail>> = _cardDetailsList

    fun getCardListData(request: GetIssuerCardListRequest
    ) = viewModelScope.launch {
        _IssuerCardListResponse.value = repository.getCardList(request)
    }
    fun cardList(data: List<CardDetail>) {
        _cardDetailsList.value = data
    }
    fun setSelectCard(cardDetail: CardDetail) {
        selectedCardId = cardDetail.issuerCardId

    }
    fun isSelectedCardOther(selectedString: String): Boolean? {
        return _cardDetailsList.value?.filter {issuerDetail ->  issuerDetail.issuerCardName.equals(selectedString, ignoreCase = true)}?.first()?.isOther
    }
    //card color list
    private val _cardNetworkResponse : MutableLiveData<Resource<CardNetworkListResponse>> = MutableLiveData()

    val cardNetworkResponse : LiveData<Resource<CardNetworkListResponse>>
        get() = _cardNetworkResponse

    private val _cardNetworkList = MutableLiveData<List<CardNetwork>>()
    var cardNetworkList: LiveData<List<CardNetwork>> = _cardNetworkList

    fun getCardNetworkData(request: GetCardNetworkRequest) = viewModelScope.launch {
        _cardNetworkResponse.value = repository.getCardNetwork(request)
    }
    fun getCardNetworkListOfIssuer() = viewModelScope.launch {
        _cardNetworkResponse.value = repository.getCardNetworkListOfIssuer()
    }


    fun getCardOnNetworkData() = viewModelScope.launch {
        _cardNetworkResponse.value = repository.cardOnNetwork()
    }


    fun getCardColorData(
    ) = viewModelScope.launch {
        _cardColorListResponse.value = repository.getCardColorList()
    }

    fun cardTypeList(data: List<CardType>) {
        _cardTypeList.value = data
    }



    //Add new card
    private val _addCardResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val addCardResponse : LiveData<Resource<CommonResponse>>
        get() = _addCardResponse

    fun addNewCard(addCardRequest: AddNewCardRequest) = viewModelScope.launch {
        _addCardResponse.value = repository.addNewCard(addCardRequest)
    }

    //Add new retail card
    private val _addRetailCardResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val addRetailCardResponse : LiveData<Resource<CommonResponse>>
        get() = _addRetailCardResponse

    fun addNewRetailCard(addCardRequest: AddNewRetailCardRequest) = viewModelScope.launch {
        _addRetailCardResponse.value = repository.addNewRetailCard(addCardRequest)
    }

    fun addNewIssuerCard(addCardRequest: AddNewIssuerCardRequest) = viewModelScope.launch {
        _addRetailCardResponse.value = repository.addNewIssuerCard(addCardRequest)
    }

    // Card network credit card list data
    private val _cardNetworkCreditCardListData : MutableLiveData<Resource<HomeResponse>> = MutableLiveData()

    val cardNetworkCreditCardListData : LiveData<Resource<HomeResponse>>
        get() = _cardNetworkCreditCardListData

    fun getCardNetworkCreditCardListData(
        bankId: String?,
        issuerId: String?,
        cardNetworkId: String?,
        pageNo: Int,
                    )= viewModelScope.launch {
        _cardNetworkCreditCardListData.value = repository.getCardNetworkCreditCardListData(
            CardNetworkCreditCardListDataRequest(bank_id = bankId!!,
                issuer_id = issuerId!!,
                card_network_id = cardNetworkId!!,
                pagination = pageNo
            )
        )
    }


    // User Card On network credit card list data
    private val _cardOnNetworkCreditCardListData : MutableLiveData<Resource<HomeResponse>> = MutableLiveData()

    val cardOnNetworkCreditCardListData : LiveData<Resource<HomeResponse>>
        get() = _cardOnNetworkCreditCardListData

    fun getCardNetworkCreditCardListData(
        cardNetworkName: String,

        )= viewModelScope.launch {
        _cardOnNetworkCreditCardListData.value = repository.getUserCardNetworkCardList(
            GetUserCardNetworkCardListRequest(
                cardNetworkName
            )
        )
    }

    // credit card list data
    private val _creditCardListData : MutableLiveData<Resource<HomeResponse>> = MutableLiveData()

    val creditCardListData : LiveData<Resource<HomeResponse>>
        get() = _creditCardListData

    fun getCreditCardListData(
        bankId: String?,
        issuerId: String?,
        cardNetworkId: String?,
        cardTypeId: String?,
        issuerCardId: String?,
        pageNo: Int,
    )= viewModelScope.launch {
        _creditCardListData.value = repository.getCreditCardListData(
            CreditCardListDataRequest(bank_id = bankId!!,
                issuer_id = issuerId!!,
                issuer_card_id = issuerCardId!!,
                card_network_id = cardNetworkId!!,
                card_type_id = cardTypeId!!,
                pagination = pageNo
            )
        )
    }


    //upload file
    private val imageFile_ = MutableLiveData<File>()
    val imageFile: LiveData<File>
        get() = imageFile_

    private val _addIssuerCardDesignResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val addIssuerCardDesignResponse : LiveData<Resource<CommonResponse>>
        get() = _addIssuerCardDesignResponse

    fun uploadImage(
        file: File,
        bankId: String?,
        issuerId: String?,
        cardNetworkId: String?,
        cardTypeId: String?,
        cardDesignName: String
    ) = viewModelScope.launch {

        val cardDesignImage: RequestBody = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            file
        )

        val cardDesignImageBody: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "issuer_card_image",
                file.getName(), cardDesignImage
            )

        val cardNetworkIdReqBody = getMultiPartFormRequestBody(cardNetworkId)
        val issuerCardNameReqBody = getMultiPartFormRequestBody(cardDesignName)
        val cardTypeIdReqBody = getMultiPartFormRequestBody(cardTypeId)
        val issuerIdReqBody = getMultiPartFormRequestBody(issuerId)
        val bankIdReqBody = getMultiPartFormRequestBody(bankId)

        _addIssuerCardDesignResponse.value = repository.uploadImage(
            cardDesignImageBody,
            cardNetworkIdReqBody,
            issuerCardNameReqBody,
            cardTypeIdReqBody,
            issuerIdReqBody,
            bankIdReqBody
        )
    }

    private fun getMultiPartFormRequestBody(tag: String?): RequestBody {
        return tag!!.toRequestBody(MultipartBody.FORM)

    }



    //card type list
    private val _cardDetailsResponse : MutableLiveData<Resource<CardDetailsResponse>> = MutableLiveData()

    val cardDetailsResponse : LiveData<Resource<CardDetailsResponse>>
        get() = _cardDetailsResponse


    lateinit var card: Card

    fun getCardDetailsData(request: GetCardDetailsRequest) = viewModelScope.launch {
        _cardDetailsResponse.value = repository.getCardDetails(request)
    }

    //Edite card
    private val _editCardResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val editCardResponse : LiveData<Resource<CommonResponse>>
        get() = _editCardResponse

    fun editCard(request: EditCardRequest) = viewModelScope.launch {
        _editCardResponse.value = repository.editCard(request)
    }

    //Edite card
    private val _editRetailCardResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val editRetailCardResponse : LiveData<Resource<CommonResponse>>
        get() = _editRetailCardResponse

    fun editRetailCard(request: EditRetailCardRequest) = viewModelScope.launch {
        _editRetailCardResponse.value = repository.editRetailCard(request)
    }


    private val _deleteCardResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val deleteCardResponse : LiveData<Resource<CommonResponse>>
        get() = _deleteCardResponse


    fun deleteCard(
        request: GetCardDetailsRequest,
    ) = viewModelScope.launch {
        _deleteCardResponse.value = repository.deleteCard(
            request
        )
    }


    fun isValidDates(paymentDueDate: Int, statementClosingDate: Int): Boolean {
        // Optional: validate the input range (should be between 1 and 31)
        require(paymentDueDate in 1..31) { "Due Date must be between 1 and 31" }
        require(statementClosingDate in 1..31) { "Statement Closing Date must be between 1 and 31" }

        return if (paymentDueDate in 26..31) {
            // When due date is at the end of the month, the statement date must be in the beginning (next month)
            statementClosingDate in 1..5
        } else {
            // When due date is in the first part of the month, the statement date should be later in the same month.
            // Also, avoid dates 1 to 5 because those are reserved for next month.
            statementClosingDate > paymentDueDate
        }
    }
}