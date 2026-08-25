package com.cardleyreminder.apps.data.model.subscription

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetTransactionDetailsResponse (
    @SerializedName("status"       ) override var status      : Boolean = false,
    @SerializedName("message"      ) override var message     : String?  = null,
    override var ws: String?,
    @SerializedName("data"    ) var data    : TransactionData?    = TransactionData(),
):BaseResponse()

data class TransactionData (

    @SerializedName("id"                              ) var id                            : String? = null,
    @SerializedName("payment_status"                  ) var paymentStatus                 : String? = null,
    @SerializedName("amount"                          ) var amount                        : String? = null,
    @SerializedName("currency"                        ) var currency                      : String? = null,
    @SerializedName("created_date"                    ) var createdDate                   : String? = null,
    @SerializedName("transaction_id"                  ) var transactionId                 : String? = null,
    @SerializedName("price_id"                        ) var priceId                       : String? = null,
    @SerializedName("subscription"                    ) var subscription                  : String? = null,
    @SerializedName("start"                           ) var start                         : String? = null,
    @SerializedName("end"                             ) var end                           : String? = null,
    @SerializedName("subscription_status"             ) var subscriptionStatus            : String? = null,
    @SerializedName("subscription_cancelled_datetime" ) var subscriptionCancelledDatetime : String? = null,
    @SerializedName("plan_name"                       ) var planName                      : String? = null

)