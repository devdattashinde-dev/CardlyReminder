package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class CardNetwork (
    @SerializedName("card_network_id"   ) var cardNetworkId : String? = null,
    @SerializedName("card_network_name" ) var name          : String? = null,
    @SerializedName("card_network_image") var image         : String? = null,
    @SerializedName("card_count"        ) var cardCount     : String? = null
)
