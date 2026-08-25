package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class CardDetailsResponse (

    @SerializedName("status"  ) override var status  : Boolean = false,
    @SerializedName("message" ) override var message : String?  = null,
    @SerializedName("flag"    ) var flag    : String?  = null,
    @SerializedName("data"    ) var data    : Card?    = null,
    override var ws: String?

):BaseResponse()

data class Card (

    @SerializedName("card_id"                ) var cardId               : String? = null,
    @SerializedName("card_number"            ) var cardNumber           : String? = null,
    @SerializedName("statement_closing_date" ) var statementClosingDate : String? = null,
    @SerializedName("payment_due_date"       ) var paymentDueDate       : String? = null,
    @SerializedName("card_expire"            ) var cardExpire           : String? = null,
    @SerializedName("issuer_id"              ) var issuerId             : String? = null,
    @SerializedName("bank_id"                ) var bankId               : String? = null,
    @SerializedName("card_network_id"        ) var cardNetworkId        : String? = null,
    @SerializedName("card_type_id"           ) var cardTypeId           : String? = null,
    @SerializedName("issuer_card_id"         ) var issuerCardId         : String? = null,
    @SerializedName("card_color_id"          ) var cardColorId          : String? = null,
    @SerializedName("comment"                ) var comment              : String? = null,
    @SerializedName("card_network_name"      ) var cardNetworkName      : String? = null,
    @SerializedName("card_network_image"     ) var cardNetworkImage     : String? = null,
    @SerializedName("card_image"             ) var cardImage            : String? = null,
    @SerializedName("color_value"            ) var colorValue           : String? = null,
    @SerializedName("text_color"             ) var textColor            : String? = null,
    @SerializedName("card_name"              ) var cardName             : String? = null,
    @SerializedName("issuer_name"            ) var issuerName           : String? = null,
    @SerializedName("bank_name"              ) var bankName             : String? = null,
    @SerializedName("card_type_name"         ) var cardTypeName         : String? = null,
    @SerializedName("retail_store_issued"      ) var retailStoreIssued     : String? = null,
    @SerializedName("retail_store_credit_card" ) var retailStoreCreditCard : String? = null,
    @SerializedName("card_text_color"          ) var cardTextColor         : String? = null,
    @SerializedName("card_bank_name"           ) var cardBankName          : String? = null,
    @SerializedName("credit_card_name"         ) var creditCardName        : String? = null,

)