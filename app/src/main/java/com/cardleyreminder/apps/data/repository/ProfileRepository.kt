package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.model.profile.ChangePasswordRequest
import com.cardleyreminder.apps.data.model.profile.EditProfileRequest
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPRequest
import com.cardleyreminder.apps.data.model.profile.VerifyEmailOTPRequest
import com.cardleyreminder.apps.data.network.ProfileApi

class ProfileRepository(
    private val api : ProfileApi
) : BaseRepository() {

    suspend fun getProfileData()= apiCall {
            api.getProfileData()
    }

    suspend fun editProfileData(request: EditProfileRequest)= apiCall {
            api.editProfileData(request)
    }
    suspend fun sendEmailOTP(request: SendEmailOTPRequest)= apiCall {
            api.sendEmailOTP(request)
    }

    suspend fun verifyEmailOTP(request: VerifyEmailOTPRequest)= apiCall {
            api.verifyEmailOTP(request)
    }

    suspend fun changePassword(request: ChangePasswordRequest)= apiCall {
            api.changePassword(request)
    }
    suspend fun getPlanList()= apiCall {
            api.getPlanList()
    }


}