package com.cardleyreminder.apps.data.model.calenderDate

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.home.HomeData
import com.google.gson.annotations.SerializedName

data class CalenderDateResponse(
    @SerializedName("statement_closing_data" ) var statementClosingData : ArrayList<HomeData> = arrayListOf(),
    @SerializedName("payment_due_data"       ) var paymentDueData       : ArrayList<HomeData>       = arrayListOf(),
    override var message: String?,
    override var status: Boolean,
    override var ws: String?
): BaseResponse()