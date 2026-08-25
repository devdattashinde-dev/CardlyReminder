package com.cardleyreminder.apps.data.model.subscription

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetPlanListResponse (
    @SerializedName("ws"      ) override var ws      : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<PlanData> = arrayListOf(),
    @SerializedName("message" ) override var message : String?         = null,
    @SerializedName("status"  ) override var status  : Boolean        = false
):BaseResponse()

data class PlanData (

    @SerializedName("sub_id"         ) var subId         : String? = null,
    @SerializedName("price"          ) var price         : String? = null,
    @SerializedName("duration"       ) var duration      : String? = null,
    @SerializedName("heading"        ) var heading       : String? = null,
    @SerializedName("duration_label" ) var durationLabel : String? = null,
    @SerializedName("most_popular"   ) var mostPopular   : String? = null,
    @SerializedName("price_id"       ) var priceId       : String? = null,

)