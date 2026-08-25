package com.cardleyreminder.apps.data.model.subscription

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class CancelSubscriptionResponse (
    @SerializedName("status"       ) override var status      : Boolean = false,
    @SerializedName("message"      ) override var message     : String?  = null,
    override var ws: String?,
    @SerializedName("subscriptionId" ) var subscriptionId : String?  = null,
):BaseResponse()

