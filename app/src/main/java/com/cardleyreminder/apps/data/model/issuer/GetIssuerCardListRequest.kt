package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class GetIssuerCardListRequest (
    val card_type_id: String,
    val user_card_count: String
): BaseRequest()