package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class GetCardTypeRequest (
    val card_network_id: String
): BaseRequest()