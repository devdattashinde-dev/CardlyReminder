package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.subscription.GetRedirectUrlRequest
import com.cardleyreminder.apps.data.model.subscription.GetTransactionDetailsRequest
import com.cardleyreminder.apps.data.network.SubscriptionApi

class SubscriptionRepository(
    private val api : SubscriptionApi
) : BaseRepository() {

    suspend fun getPlanList()= apiCall {
            api.getPlanList()
    }

    suspend fun getRedirectUrl(request:GetRedirectUrlRequest)= apiCall {
        api.getRedirectUrl(request)
    }

    suspend fun getTransactionDetails(request:GetTransactionDetailsRequest)= apiCall {
        api.getTransactionDetails(request)
    }
    suspend fun cancelSubscription(request:GetTransactionDetailsRequest)= apiCall {
        api.cancelSubscription(request)
    }


    suspend fun getPaymentHistory()= apiCall {
        api.getPaymentHistory()
    }
}