package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class CardCount (
    @SerializedName("card_count" ) var cardCount : String? = null
)
