package com.cardleyreminder.apps.data.model.notification

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class NotificationListRequest(
    var pagination: String?="",
): BaseRequest()