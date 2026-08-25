package com.cardleyreminder.apps.data.model.issuer

import com.google.gson.annotations.SerializedName

data class CardColor (
    @SerializedName("card_color_id"     ) var cardColorId     : String? = null,
    @SerializedName("color_name"        ) var colorName       : String? = null,
    @SerializedName("small_color_image" ) var smallColorImage : String? = null,
    @SerializedName("card_image"        ) var cardImage       : String? = null,
    var isSelected : Boolean = false
)
