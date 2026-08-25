package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest
import com.google.gson.annotations.SerializedName

data class AddNewRetailCardRequest (

    @SerializedName("card_number"              ) var cardNumber            : String? = null,
    @SerializedName("issuer_id"                ) var issuerId              : String? = null,
    @SerializedName("retail_store_issued"      ) var retailStoreIssued     : String? = null,
    @SerializedName("retail_store_credit_card" ) var retailStoreCreditCard : String? = null,
    @SerializedName("statement_closing_date"   ) var statementClosingDate  : String? = null,
    @SerializedName("payment_due_date"         ) var paymentDueDate        : String? = null,
    @SerializedName("card_expire"              ) var cardExpire            : String? = null,
    @SerializedName("comment"                  ) var comment               : String? = null,
    @SerializedName("card_color_id"            ) var cardColorId           : String? = null
): BaseRequest()