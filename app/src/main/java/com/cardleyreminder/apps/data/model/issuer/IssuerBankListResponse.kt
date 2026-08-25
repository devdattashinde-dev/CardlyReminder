package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseResponse

data class IssuerBankListResponse(
    val `data`: List<BankDetail>,
    override var message: String?,
    override var status: Boolean,
    override var ws: String?

):BaseResponse()
