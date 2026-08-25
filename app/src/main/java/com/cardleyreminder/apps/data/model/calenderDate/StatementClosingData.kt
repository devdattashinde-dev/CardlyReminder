package com.cardleyreminder.apps.data.model.calenderDate

import com.google.gson.annotations.SerializedName

data class StatementClosingData (

    @SerializedName("card_id"                ) var cardId               : String? = null,
    @SerializedName("statement_closing_date" ) var statementClosingDate : String? = null,
    @SerializedName("card_name"              ) var cardName             : String? = null

)
