package com.cardleyreminder.apps.data.model.auth

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class AuthRequest (
    @SerializedName("email") val userId: String?,
    @SerializedName("password") val userName: String?,
    @SerializedName("source") val userEmail: String?,
    @SerializedName("installed_version") val installedVersion: String?,
    @SerializedName("time_zone") val timeZone: String?,
): BaseRequest()