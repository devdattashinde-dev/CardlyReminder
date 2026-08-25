package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.network.CalenderApi

class CalenderRepository(
    private val api : CalenderApi
) : BaseRepository() {

    suspend fun getCalenderDateData() = apiCall {
            api.getCalenderDate()
    }
}