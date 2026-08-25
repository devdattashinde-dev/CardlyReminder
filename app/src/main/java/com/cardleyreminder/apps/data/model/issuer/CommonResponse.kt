package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class CommonResponse(
    override var message: String?,
    override var status: Boolean,
    override var ws: String?,
    @SerializedName("notificaiton_count" ) var notificaitonCount : Int            = 0,
):BaseResponse()
