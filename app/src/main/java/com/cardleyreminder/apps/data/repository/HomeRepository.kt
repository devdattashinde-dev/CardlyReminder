package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.fcmToken.UpdateFCMTokenRequest
import com.cardleyreminder.apps.data.model.home.HomeDataRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.network.HomeApi

class HomeRepository(
    private val api : HomeApi
) : BaseRepository() {

    suspend fun getHomeData(homeDataRequest: HomeDataRequest) = apiCall {
            api.getHomeData(homeDataRequest)
    }

    suspend fun updateToken(fcmToken: UpdateFCMTokenRequest)= apiCall {
        api.updateToken(fcmToken)
    }

    suspend fun deleteCard(request: GetCardDetailsRequest)= apiCall {
        api.deleteCard(request)
    }

    suspend fun logout()= apiCall {
        api.logout()
    }
    suspend fun deleteAccount()= apiCall {
        api.deleteAccount()
    }
}