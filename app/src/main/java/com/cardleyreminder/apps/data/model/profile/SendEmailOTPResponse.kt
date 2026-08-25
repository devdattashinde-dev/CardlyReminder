package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class SendEmailOTPResponse (
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("ws"      ) var ws      : String?  = null,
    @SerializedName("data"    ) var data    : String?  = null,
    @SerializedName("message" ) var message : String?  = null
): BaseRequest()