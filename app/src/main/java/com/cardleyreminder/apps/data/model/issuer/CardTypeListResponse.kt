package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseResponse

data class CardTypeListResponse(
    val `data`: List<CardType>,
    override var message: String?,
    override var status: Boolean,
    override var ws: String?

):BaseResponse()
