package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class CardNetworkCreditCardListDataRequest (
    val pagination: Int,
    val issuer_id: String,
    val bank_id: String,
    val card_network_id: String,
): BaseRequest()