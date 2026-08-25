package com.cardleyreminder.apps.data.model.auth

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id"                       ) var id          : String? = null,
    @SerializedName("first_name"               ) var firstName   : String? = null,
    @SerializedName("phone"                    ) var phone       : String? = null,
    @SerializedName("email"                    ) var email       : String? = null,
    @SerializedName("logo"                     ) var logo        : String? = null,
    @SerializedName("active"                   ) var active      : String? = null,
    @SerializedName("social"                   ) var social      : String? = null,
    @SerializedName("country_code"             ) var countryCode : String? = null,
    @SerializedName("token"                    ) var token       : String? = null,
    @SerializedName("country_code_name"        ) var countryCodeName       : String? = null,
)