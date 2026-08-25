package com.cardleyreminder.apps.data.model.subscription

import com.google.gson.annotations.SerializedName

data class GetRedirectUrlRequest (
    @SerializedName("price_id" ) var priceId : String? = null,
    @SerializedName("plan_with_trial" ) var planWithTrial : String = "no",
)