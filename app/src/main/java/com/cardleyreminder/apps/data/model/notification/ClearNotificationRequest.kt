package com.cardleyreminder.apps.data.model.notification

import com.google.gson.annotations.SerializedName

data class ClearNotificationRequest(
    @SerializedName("noti_id" ) var notiId : String
)