package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class BankDetail (
    @SerializedName("bank_id"    ) var bankId    : String? = null,
    @SerializedName("bank_name"  ) var bankName  : String? = null,
    @SerializedName("bank_image" ) var bankImage : String? = null,
    @SerializedName("clickable" ) var clickable : Boolean = false,
)
