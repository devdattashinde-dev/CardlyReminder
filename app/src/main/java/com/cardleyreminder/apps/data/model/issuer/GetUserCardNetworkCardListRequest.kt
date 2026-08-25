package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class GetUserCardNetworkCardListRequest (
    val card_network_name: String
): BaseRequest()