package com.cardleyreminder.apps.data.model.auth

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("first_name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("fcm_no") val fcmNo: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("country_code") val countryCode: String?,
    @SerializedName("installed_version") val installedVersion: String?,
    @SerializedName("time_zone") val timeZone: String?,
    @SerializedName("country_code_name") val countryCodeName: String?,
): BaseRequest()