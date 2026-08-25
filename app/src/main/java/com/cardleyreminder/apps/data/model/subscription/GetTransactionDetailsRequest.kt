package com.cardleyreminder.apps.data.model.subscription

import com.google.gson.annotations.SerializedName

data class GetTransactionDetailsRequest (
    @SerializedName("payment_id" ) var priceId : String
)