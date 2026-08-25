package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class SendEmailOTPRequest (
    @SerializedName("email"      ) var email     : String

): BaseRequest()