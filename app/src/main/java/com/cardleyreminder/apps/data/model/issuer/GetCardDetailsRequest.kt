package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class GetCardDetailsRequest (
    @SerializedName("card_id" ) val cardId: String
)