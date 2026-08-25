package com.cardleyreminder.apps.data.model.insights

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class TipDataResponse (

    @SerializedName("status"  ) override var status  : Boolean        = false,
    @SerializedName("message" ) override var message : String?         = null,
    @SerializedName("flag"    ) var flag    : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<TipData> = arrayListOf(),
    @SerializedName("ws"      ) override var ws      : String?         = null

): BaseResponse()

data class TipData (

    @SerializedName("tip_id"      ) var tipId       : String? = null,
    @SerializedName("heading"     ) var heading     : String? = null,
    @SerializedName("description" ) var description : String? = null

)