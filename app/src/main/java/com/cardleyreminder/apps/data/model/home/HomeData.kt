package com.cardleyreminder.apps.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeData(
    @SerializedName("card_id"                 ) var cardId                : String?  = null,
    @SerializedName("card_number"             ) var cardNumber            : String?  = null,
    @SerializedName("statement_closing_date"  ) var statementClosingDate  : String?  = null,
    @SerializedName("payment_due_date"        ) var paymentDueDate        : String?  = null,
    @SerializedName("card_expire"             ) var cardExpire            : String?  = null,
    @SerializedName("card_name"               ) var cardName              : String?  = null,
    @SerializedName("card_image"              ) var cardImage             : String?  = null,
    @SerializedName("is_expired"              ) var isExpired             : Boolean? = null,
    @SerializedName("statement_closing_label" ) var statementClosingLabel : String?  = null,
    @SerializedName("payment_due_label"       ) var paymentDueLabel       : String?  = null,
    @SerializedName("card_expire_short"       ) var cardExpireShort       : String?  = null,
    @SerializedName("card_expire_label"       ) var cardExpireLabel       : String?  = null,
    @SerializedName("card_network_name"       ) var cardNetworkName       : String?  = null,
    @SerializedName("card_network_image"      ) var cardNetworkImage      : String?  = null,
    @SerializedName("color_value"             ) var colorValue            : String?  = null,
    @SerializedName("card_text_color"              ) var textColor             : String?  = null,

)