package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class GetIssuerBankListRequest (
    val issuer_id: String
): BaseRequest()