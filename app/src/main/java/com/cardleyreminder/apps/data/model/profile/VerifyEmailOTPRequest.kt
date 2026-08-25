package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class VerifyEmailOTPRequest (
    @SerializedName("otp"    ) var otp   : String? = null,
    @SerializedName("otp_id" ) var otpId : String? = null
): BaseRequest()