package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class CardType (
    @SerializedName("card_type_id"   ) var cardTypeId : String? = null,
    @SerializedName("card_type_name" ) var cardName   : String? = null,
    @SerializedName("card_type_image" ) var cardImage   : String? = null,
)
