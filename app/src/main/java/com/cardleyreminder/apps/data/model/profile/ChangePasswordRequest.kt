package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest (
    @SerializedName("old_password" ) var oldPassword : String? = null,
    @SerializedName("new_password" ) var newPassword : String? = null

): BaseRequest()