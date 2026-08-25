package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class Issuers (

    @SerializedName("issuer_id"    ) var issuerId    : String? = null,
    @SerializedName("issuer_name"  ) var issuerName  : String? = null,
    @SerializedName("issuer_image" ) var issuerImage : String? = null

)
