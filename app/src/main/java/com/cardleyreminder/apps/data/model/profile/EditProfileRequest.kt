package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class EditProfileRequest (
    @SerializedName("first_name" ) var firstName : String,
    @SerializedName("email"      ) var email     : String,
    @SerializedName("phone"      ) var phone     : String,
    @SerializedName("country_code"     ) var countryCode     : String,
    @SerializedName("country_code_name"     ) var countryCodeName     : String,
    @SerializedName("source"     ) var source    : String = "android",
    @SerializedName("fcm_no"     ) var fcmNo     : String = "null",

): BaseRequest()