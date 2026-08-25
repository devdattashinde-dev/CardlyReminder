package com.cardleyreminder.apps.data.model.calenderDate

import com.google.gson.annotations.SerializedName

data class PaymentDueData (

    @SerializedName("card_id"          ) var cardId         : String? = null,
    @SerializedName("payment_due_date" ) var paymentDueDate : String? = null,
    @SerializedName("card_name"        ) var cardName       : String? = null

)
