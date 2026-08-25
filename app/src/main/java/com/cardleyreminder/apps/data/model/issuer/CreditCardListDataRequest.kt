package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class CreditCardListDataRequest (
    val pagination: Int,
    val issuer_id: String,
    val bank_id: String,
    val card_network_id: String,
    val card_type_id: String,
    val issuer_card_id: String,
): BaseRequest()