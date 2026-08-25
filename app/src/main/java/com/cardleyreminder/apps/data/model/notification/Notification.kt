package com.cardleyreminder.apps.data.model.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("noti_id"      ) var notiId      : String? = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("message"      ) var message     : String? = null,
    @SerializedName("created_date" ) var createdDate : String? = null,
    @SerializedName("is_seen"      ) var isSeen      : String? = null,
    @SerializedName("time"         ) var time        : String? = null
)