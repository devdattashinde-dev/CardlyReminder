package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.notification.ClearNotificationRequest
import com.cardleyreminder.apps.data.network.NotificationApi

class NotificationRepository(
    private val api : NotificationApi
) : BaseRepository() {

    suspend fun getNotificationList() = apiCall {
            api.notificationList()
    }

    suspend fun removeNotification(request: ClearNotificationRequest) = apiCall {
            api.removeNotification(request)
    }

    suspend fun readNotification(request: ClearNotificationRequest) = apiCall {
            api.readNotification(request)
    }
}