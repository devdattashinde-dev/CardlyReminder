package com.cardleyreminder.apps.data.model.auth

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status"  ) override var status  : Boolean = false,
    @SerializedName("ws"      ) override var ws      : String?  = null,
    @SerializedName("data"    ) var data    : User?    = User(),
    @SerializedName("message" ) override var message : String?  = null
): BaseResponse()