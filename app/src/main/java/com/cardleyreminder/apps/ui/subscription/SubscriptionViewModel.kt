package com.cardleyreminder.apps.ui.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.subscription.CancelSubscriptionResponse
import com.cardleyreminder.apps.data.model.subscription.GetPaymentHistoryResponse
import com.cardleyreminder.apps.data.model.subscription.GetPlanListResponse
import com.cardleyreminder.apps.data.model.subscription.GetRedirectUrlRequest
import com.cardleyreminder.apps.data.model.subscription.GetRedirectUrlResponse
import com.cardleyreminder.apps.data.model.subscription.GetTransactionDetailsRequest
import com.cardleyreminder.apps.data.model.subscription.GetTransactionDetailsResponse
import com.cardleyreminder.apps.data.model.subscription.PlanData
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.SubscriptionRepository
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val repository: SubscriptionRepository
) : ViewModel() {

    var showToggle = false
    lateinit var paymentId: String
    lateinit var selectedPlan: PlanData
    private val _planDataResponse = MutableLiveData<Resource<GetPlanListResponse>>()
    val planDataResponse: LiveData<Resource<GetPlanListResponse>> = _planDataResponse

    fun getPlanList()=viewModelScope.launch{
        _planDataResponse.value=repository.getPlanList()
    }


    private val _getRedirectUrlResponse = MutableLiveData<Resource<GetRedirectUrlResponse>>()
    val getRedirectUrlResponse: LiveData<Resource<GetRedirectUrlResponse>> = _getRedirectUrlResponse

    fun getRedirectUrl(checked: Boolean) =viewModelScope.launch{
        _getRedirectUrlResponse.value=repository.getRedirectUrl(
            GetRedirectUrlRequest(
                selectedPlan.priceId,
                if(checked && showToggle) "yes" else "no"
            )
        )
    }

    private val _getTransactionDetailsResponse = MutableLiveData<Resource<GetTransactionDetailsResponse>>()
    val getTransactionDetailsResponse: LiveData<Resource<GetTransactionDetailsResponse>> = _getTransactionDetailsResponse

    fun getTransactionDetails(paymentID : String)=viewModelScope.launch{
        _getTransactionDetailsResponse.value=repository.getTransactionDetails(
            GetTransactionDetailsRequest(
                paymentID
            )
        )
    }


    private val _getPaymentHistoryResponse = MutableLiveData<Resource<GetPaymentHistoryResponse>>()
    val getPaymentHistoryResponse: LiveData<Resource<GetPaymentHistoryResponse>> = _getPaymentHistoryResponse

    fun getPaymentHistory()=viewModelScope.launch{
        _getPaymentHistoryResponse.value=repository.getPaymentHistory()
    }

    private val _cancelSubscriptionResponse = MutableLiveData<Resource<CancelSubscriptionResponse>>()
    val cancelSubscriptionResponse: LiveData<Resource<CancelSubscriptionResponse>> = _cancelSubscriptionResponse

    fun cancelSubscription(paymentID : String)=viewModelScope.launch{
        _cancelSubscriptionResponse.value=repository.cancelSubscription(
            GetTransactionDetailsRequest(
                paymentID
            )
        )
    }

}