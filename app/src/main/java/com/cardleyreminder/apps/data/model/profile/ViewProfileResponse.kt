package com.cardleyreminder.apps.data.model.profile

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class ViewProfileResponse(
    @SerializedName("status"  ) override var status  : Boolean        = false,
    @SerializedName("ws"      ) override var ws      : String?         = null,
    @SerializedName("data"    ) var userData    : ArrayList<UserData> = arrayListOf(),
    @SerializedName("message" ) override var message : String?         = null
):BaseResponse()

data class UserData (

    @SerializedName("first_name" ) var firstName : String? = null,
    @SerializedName("phone"      ) var phone     : String? = null,
    @SerializedName("email"      ) var email     : String? = null,
    @SerializedName("social"     ) var social    : String? = null,
    @SerializedName("active"     ) var active    : String? = null,
    @SerializedName("country_code"     ) var countryCode    : String? = null,

    @SerializedName("country_code_name"        ) var countryCodeName       : String? = null,

)