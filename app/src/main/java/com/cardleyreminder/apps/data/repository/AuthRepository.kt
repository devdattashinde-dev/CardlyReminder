package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.UserPreferences
import com.cardleyreminder.apps.data.model.auth.AuthRequest
import com.cardleyreminder.apps.data.model.auth.ForgotPasswordRequest
import com.cardleyreminder.apps.data.model.auth.RegisterRequest
import com.cardleyreminder.apps.data.model.auth.SocialAuthRequest
import com.cardleyreminder.apps.data.network.AuthApi
import com.cardleyreminder.apps.data.model.auth.User
import com.cardleyreminder.apps.util.getTimeZone

class AuthRepository(
    private val api : AuthApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun login(
        email: String,
        password: String,
        versionName: String
    )= apiCall {
            var authRequest = AuthRequest(email,password,"android",versionName,getTimeZone())
            api.login(authRequest)
    }

    suspend fun forgotPassword(
        email: String)= apiCall {
        var request = ForgotPasswordRequest(email)
        api.forgotPassword(request)
    }

    suspend fun socialLogin(
        name: String,
        email: String,
        password: String,
        versionName: String
    )= apiCall {
        var socialAuthRequest = SocialAuthRequest(name,email,password,"","android","gmail",versionName,
            getTimeZone()
        )
        api.socialLogin(socialAuthRequest)
    }

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        countryCode: String,
        versionName: String,
        countryCodeName: String
    )= apiCall {
        var regRequest = RegisterRequest(name,email,phone,password,"","android",countryCode,versionName,getTimeZone(),countryCodeName)
        api.signup(regRequest)
    }

    fun saveUser(user: User){
        preferences.saveUser(user)
    }
}