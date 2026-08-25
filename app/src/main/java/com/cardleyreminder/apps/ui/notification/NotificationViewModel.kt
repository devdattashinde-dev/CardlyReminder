package com.cardleyreminder.apps.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.notification.ClearNotificationRequest
import com.cardleyreminder.apps.data.model.notification.NotificationResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    var notificationCount: MutableLiveData<Int> = MutableLiveData()
    var selectedNotificationIndex: Int = -1
    private val _notificationResponse : MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()

    val notificationResponse : LiveData<Resource<NotificationResponse>>
        get() = _notificationResponse

    fun getNotificationData(
    ) = viewModelScope.launch {
        _notificationResponse.value = repository.getNotificationList()
    }


    private val _clearNotificationResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val clearNotificationResponse : LiveData<Resource<CommonResponse>>
        get() = _clearNotificationResponse

    fun removeNotification(request: ClearNotificationRequest) = viewModelScope.launch {
        _clearNotificationResponse.value = repository.removeNotification(request)
    }


    private val _readNotificationResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val readNotificationResponse : LiveData<Resource<CommonResponse>>
        get() = _readNotificationResponse

    fun readNotification(request: ClearNotificationRequest) = viewModelScope.launch {
        _readNotificationResponse.value = repository.readNotification(request)
    }


}