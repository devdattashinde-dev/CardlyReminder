package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.calenderDate.CalenderDateResponse
import retrofit2.http.GET

interface CalenderApi {

    @GET("api/getCalendarDate")
    suspend fun getCalenderDate(
    ) : CalenderDateResponse

    /*@POST("notification_listing")
    suspend fun notificationList(
        @Body request: NotificationListRequest
    ) : NotificationResponse*/
}