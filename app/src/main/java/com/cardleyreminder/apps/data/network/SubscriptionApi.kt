package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.subscription.CancelSubscriptionResponse
import com.cardleyreminder.apps.data.model.subscription.GetPaymentHistoryResponse
import com.cardleyreminder.apps.data.model.subscription.GetPlanListResponse
import com.cardleyreminder.apps.data.model.subscription.GetRedirectUrlRequest
import com.cardleyreminder.apps.data.model.subscription.GetRedirectUrlResponse
import com.cardleyreminder.apps.data.model.subscription.GetTransactionDetailsRequest
import com.cardleyreminder.apps.data.model.subscription.GetTransactionDetailsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionApi {



    @GET("api/getPlanList")
    suspend fun getPlanList(
    ) : GetPlanListResponse

    @POST("payment/genrate_url_to_redirect")
    suspend fun getRedirectUrl(
        @Body request: GetRedirectUrlRequest
    ) : GetRedirectUrlResponse

    @POST("payment/detail")
    suspend fun getTransactionDetails(
        @Body request: GetTransactionDetailsRequest
    ) : GetTransactionDetailsResponse

    @GET("payment/history")
    suspend fun getPaymentHistory(
    ) : GetPaymentHistoryResponse


    @POST("payment/cancel_subscription")
    suspend fun cancelSubscription(
        @Body request: GetTransactionDetailsRequest
    ) : CancelSubscriptionResponse
}