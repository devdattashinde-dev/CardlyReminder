package com.cardleyreminder.apps.data.model.subscription

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetRedirectUrlResponse (
    @SerializedName("status"       ) override var status      : Boolean = false,
    @SerializedName("redirect_url" ) var redirectUrl : String?  = null,
    @SerializedName("message"      ) override var message     : String?  = null,
    override var ws: String?
):BaseResponse()