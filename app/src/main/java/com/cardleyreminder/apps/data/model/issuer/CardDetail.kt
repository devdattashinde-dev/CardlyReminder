package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class CardDetail (
    @SerializedName("issuer_card_id"    ) var issuerCardId    : String? = null,
    @SerializedName("issuer_card_name"  ) var issuerCardName  : String? = null,
    @SerializedName("issuer_card_image" ) var issuerCardImage : String? = null,
    @SerializedName("card_count"        ) var cardCount       : String? = null,
    @SerializedName("is_other"          ) var isOther   : Boolean = false
)
