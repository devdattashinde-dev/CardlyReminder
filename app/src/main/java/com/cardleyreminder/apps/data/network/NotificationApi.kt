package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.notification.ClearNotificationRequest
import com.cardleyreminder.apps.data.model.notification.NotificationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationApi {

    @GET("api/getNotificaiton")
    suspend fun notificationList(
    ) : NotificationResponse

    @POST("api/clearNotificaiton")
    suspend fun removeNotification(
        @Body request : ClearNotificationRequest
    ) : CommonResponse

    @POST("api/readNotificaiton")
    suspend fun readNotification(
        @Body request : ClearNotificationRequest
    ) : CommonResponse
}