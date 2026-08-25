package com.cardleyreminder.apps.data.model.auth

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest (
    @SerializedName("string") val userId: String?
): BaseRequest()