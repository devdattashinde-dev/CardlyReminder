package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.insights.FAQDataResponse
import com.cardleyreminder.apps.data.model.insights.FactsDataResponse
import com.cardleyreminder.apps.data.model.insights.PageDataRequest
import com.cardleyreminder.apps.data.model.insights.PageDataResponse
import com.cardleyreminder.apps.data.model.insights.TipDataResponse
import com.cardleyreminder.apps.data.model.insights.VideoListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InsightsApi {

    @GET("api/getFaqList")
    suspend fun getFAQData(
    ) : FAQDataResponse

    @GET("api/getTipList")
    suspend fun getTipsData(
    ) : TipDataResponse

    @GET("api/getFactList")
    suspend fun getFactsData(
    ) : FactsDataResponse

    @GET("api/getVideoList")
    suspend fun getVideoList(
    ) : VideoListResponse

    @POST("api/getPageData")
    suspend fun getPageData(
        @Body request: PageDataRequest
    ) : PageDataResponse
}