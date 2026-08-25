package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.network.SettingsApi

class SettingsRepository(
    private val api : SettingsApi
) : BaseRepository() {

    /*suspend fun getNotificationList(request: NotificationListRequest) = apiCall {
            api.notificationList(request)
    }*/
}