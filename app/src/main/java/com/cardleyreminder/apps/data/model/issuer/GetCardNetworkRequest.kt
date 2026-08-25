package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class GetCardNetworkRequest (
    val bank_id: String,
    val user_card_count : String = "1"
): BaseRequest()