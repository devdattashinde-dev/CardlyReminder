package com.cardleyreminder.apps.data.model.insights

import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("status"  ) override var status  : Boolean = false,
    @SerializedName("message" ) override var message : String?  = null,
    @SerializedName("flag"    ) var flag    : String?  = null,
    @SerializedName("data"    ) var data    : VideoData?    = VideoData(),
    @SerializedName("ws"      ) override var ws      : String?  = null
): BaseResponse()

data class VideoData (

    @SerializedName("credit_card_literacy_data" ) var creditCardLiteracyData : ArrayList<VideoCardData> = arrayListOf(),
    @SerializedName("promotion_data"            ) var promotionData          : ArrayList<VideoCardData>          = arrayListOf()

)

data class VideoCardData (

    @SerializedName("video_id"   ) var videoId   : String? = null,
    @SerializedName("heading"    ) var heading   : String? = null,
    @SerializedName("video_type" ) var videoType : String? = null,
    @SerializedName("video"      ) var video     : String? = null,
    @SerializedName("ads_type"   ) var adsType   : String? = null,
    @SerializedName("markit_link"   ) var markitLink   : String? = null,

)
