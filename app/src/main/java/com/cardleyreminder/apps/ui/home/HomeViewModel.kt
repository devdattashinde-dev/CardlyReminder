package com.cardleyreminder.apps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.BuildConfig
import com.cardleyreminder.apps.data.model.fcmToken.UpdateFCMTokenRequest
import com.cardleyreminder.apps.data.model.home.HomeDataRequest
import com.cardleyreminder.apps.data.model.home.HomeResponse
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    var isFromRegister = false
    var isFirstVideoShown = false

    var activePlanId: String? = null
    var fcmToken: String = ""
    var subStatus: Int = 0

    var subsErrorMessage : String = ""

    private val _homeResponse : MutableLiveData<Resource<HomeResponse>> = MutableLiveData()
    val homeResponse : LiveData<Resource<HomeResponse>>
        get() = _homeResponse

    fun getHomeData(
        pageNo: Int,
        statementClosingDate : String? = null,
        paymentDueDate : String? = null,
        validThru : String? = null,
        createdTime : String? = null,
        fcmToken : String,
        ) = viewModelScope.launch {
        _homeResponse.value = repository.getHomeData(
            HomeDataRequest(
                pagination = pageNo,
                statement_closing_date = statementClosingDate,
                payment_due_date = paymentDueDate,
                valid_thru = validThru,
                created_time = createdTime,
                fcm_no = fcmToken,
                installed_version = BuildConfig.VERSION_CODE.toString()
                )
        )
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

    private val _logoutResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val logoutResponse : LiveData<Resource<CommonResponse>>
        get() = _logoutResponse

    fun logout(
        ) = viewModelScope.launch {
        _logoutResponse.value = repository.logout(

        )
    }

    fun updateToken(fcmToken: UpdateFCMTokenRequest)= viewModelScope.launch {
      repository.updateToken(fcmToken);
    }


    private val _deleteAccountResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()
    val deleteAccountResponse : LiveData<Resource<CommonResponse>>
        get() = _deleteAccountResponse

    fun deleteAccount(
    ) = viewModelScope.launch {
        _deleteAccountResponse.value = repository.deleteAccount(

        )
    }
}