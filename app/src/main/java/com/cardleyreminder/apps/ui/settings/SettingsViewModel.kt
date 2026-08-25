package com.cardleyreminder.apps.ui.settings

import androidx.lifecycle.ViewModel
import com.cardleyreminder.apps.data.repository.SettingsRepository

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    /*private val _notificationResponse : MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()

    val notificationResponse : LiveData<Resource<NotificationResponse>>
        get() = _notificationResponse

    fun getNotificationData(request: NotificationListRequest
    ) = viewModelScope.launch {
        _notificationResponse.value = repository.getNotificationList(request)
    }*/
}