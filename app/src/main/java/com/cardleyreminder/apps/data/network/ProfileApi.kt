package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.data.model.auth.AuthResponse
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.profile.ChangePasswordRequest
import com.cardleyreminder.apps.data.model.profile.EditProfileRequest
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPRequest
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPResponse
import com.cardleyreminder.apps.data.model.profile.VerifyEmailOTPRequest
import com.cardleyreminder.apps.data.model.profile.ViewProfileResponse
import com.cardleyreminder.apps.data.model.subscription.GetPlanListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @GET("api/userData")
    suspend fun getProfileData(
    ) : ViewProfileResponse

    @POST("api/editProfile")
    suspend fun editProfileData(
        @Body request: EditProfileRequest
    ) : CommonResponse

    @POST("api/sendEmailChangeOtp")
    suspend fun sendEmailOTP(
        @Body request: SendEmailOTPRequest
    ) : SendEmailOTPResponse

    @POST("api/verifyOtp")
    suspend fun verifyEmailOTP(
        @Body request: VerifyEmailOTPRequest
    ) : CommonResponse

    @POST("api/changeProfilePass")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ) : AuthResponse

    @GET("api/getPlanList")
    suspend fun getPlanList(
    ) : GetPlanListResponse
}