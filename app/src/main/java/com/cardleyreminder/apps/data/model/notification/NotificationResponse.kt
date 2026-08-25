package com.cardleyreminder.apps.data.model.notification

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("ws"      ) override var ws      : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Notification> = arrayListOf(),
    @SerializedName("message" ) override var message : String?         = null,
    @SerializedName("status"  ) override var status  : Boolean,
    @SerializedName("notificaiton_count" ) var notificaitonCount : Int            = 0,
): BaseResponse()