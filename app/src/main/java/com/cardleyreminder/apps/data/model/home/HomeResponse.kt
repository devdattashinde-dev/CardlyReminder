package com.cardleyreminder.apps.data.model.home

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("ws"                 ) override var ws                : String?         = null,
    @SerializedName("message"            ) override var message           : String?         = null,
    @SerializedName("status"             ) override var status            : Boolean        = false,
    @SerializedName("data"               ) var data                       : ArrayList<HomeData> = arrayListOf(),
    @SerializedName("notificaiton_count" ) var notificaitonCount          : String?         = null,
    @SerializedName("subs_status"        ) var subStatus                  : String?         = null,
    @SerializedName("subs_error_message"        ) var subsErrorMessage                  : String?         = null,
    @SerializedName("active_plan_id"     ) var activePlanId               : String?         = null,
    @SerializedName("version"  ) var version            : String?         = null,
    @SerializedName("first_video"  ) var firstVideo            : String?         = null,
    @SerializedName("market_video"  ) var marketVideo            : String?         = null,
):BaseResponse()