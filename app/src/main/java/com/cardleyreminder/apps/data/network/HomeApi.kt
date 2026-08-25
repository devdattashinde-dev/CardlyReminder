package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.fcmToken.UpdateFCMTokenRequest
import com.cardleyreminder.apps.data.model.home.HomeDataRequest
import com.cardleyreminder.apps.data.model.home.HomeResponse
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeApi {

    @POST("api/homePage")
    suspend fun getHomeData(
        @Body request : HomeDataRequest
    ) : HomeResponse

    @POST("api/update_fcm_token")
    suspend fun updateToken(
        @Body request : UpdateFCMTokenRequest
    ) : CommonResponse

    @POST("api/deleteCard")
    suspend fun deleteCard(
        @Body request : GetCardDetailsRequest
    ) : CommonResponse

    @GET("api/logout")
    suspend fun logout(
    ) : CommonResponse

    @GET("api/deleteAccount")
    suspend fun deleteAccount(
    ) : CommonResponse

}