package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.auth.AuthRequest
import com.cardleyreminder.apps.data.model.auth.AuthResponse
import com.cardleyreminder.apps.data.model.auth.ForgotPasswordRequest
import com.cardleyreminder.apps.data.model.auth.RegisterRequest
import com.cardleyreminder.apps.data.model.auth.SocialAuthRequest
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/login")
    suspend fun login(
        @Body authRequest: AuthRequest
    ) : AuthResponse

    @POST("api/forgetPassword")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ) : CommonResponse

    @POST("api/SocialLogin")
    suspend fun socialLogin(
        @Body authRequest: SocialAuthRequest
    ) : AuthResponse

    @POST("api/signup")
    suspend fun signup(
        @Body registerRequest: RegisterRequest
    ) : AuthResponse
}