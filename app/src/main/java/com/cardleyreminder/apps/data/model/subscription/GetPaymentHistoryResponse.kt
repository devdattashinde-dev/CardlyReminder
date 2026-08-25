package com.cardleyreminder.apps.data.model.subscription

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetPaymentHistoryResponse (
    @SerializedName("status"       ) override var status      : Boolean = false,
    @SerializedName("message"      ) override var message     : String?  = null,
    override var ws: String?,
    @SerializedName("data"    ) var data    : ArrayList<PaymentHistoryData> = arrayListOf(),
):BaseResponse()

data class PaymentHistoryData (

    @SerializedName("id"             ) var id            : String? = null,
    @SerializedName("payment_status" ) var paymentStatus : String? = null,
    @SerializedName("amount"         ) var amount        : String? = null,
    @SerializedName("currency"       ) var currency      : String? = null,
    @SerializedName("created_date"   ) var createdDate   : String? = null,
    @SerializedName("plan_name"   ) var planName   : String? = null,

)