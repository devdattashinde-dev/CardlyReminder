package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.insights.PageDataRequest
import com.cardleyreminder.apps.data.network.InsightsApi

class InsightsRepository(
    private val api : InsightsApi
) : BaseRepository() {

    suspend fun getPageData(
        request: PageDataRequest
    ) = apiCall {
            api.getPageData(request)
    }

    suspend fun getFAQData() = apiCall {
            api.getFAQData()
    }
    suspend fun getTipsData() = apiCall {
            api.getTipsData()
    }
    suspend fun getFactsData() = apiCall {
            api.getFactsData()
    }

    suspend fun getVideoList() = apiCall {
            api.getVideoList()
    }
}