package com.cardleyreminder.apps.data.model.auth

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class SocialAuthRequest (
    @SerializedName("first_name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("fcm_no") val FCMNo: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("social") val social: String?,
    @SerializedName("installed_version") val installedVersion: String?,
    @SerializedName("time_zone") val timeZone: String?,
): BaseRequest()