package com.cardleyreminder.apps.data.model.fcmToken

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class UpdateFCMTokenRequest (
    val fcm_no: String
): BaseRequest()