package com.cardleyreminder.apps.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.auth.AuthResponse
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import com.cardleyreminder.apps.data.model.profile.ChangePasswordRequest
import com.cardleyreminder.apps.data.model.profile.EditProfileRequest
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPRequest
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPResponse
import com.cardleyreminder.apps.data.model.profile.UserData
import com.cardleyreminder.apps.data.model.profile.VerifyEmailOTPRequest
import com.cardleyreminder.apps.data.model.profile.ViewProfileResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    lateinit var userData: UserData
    private val _profileResponse = MutableLiveData<Resource<ViewProfileResponse>>()
    val profileResponse: LiveData<Resource<ViewProfileResponse>> = _profileResponse

    fun getProfileData()=viewModelScope.launch{
        _profileResponse.value=repository.getProfileData()
    }

    private val _editProfileResponse = MutableLiveData<Resource<CommonResponse>>()
    val editProfileResponse: LiveData<Resource<CommonResponse>> = _editProfileResponse

    fun editProfile(
        name: String,
        email: String,
        phone: String,
        countryCode: String,
        countryCodeName: String,
    )=viewModelScope.launch{
        _editProfileResponse.value=repository.editProfileData(
            EditProfileRequest(
                name,email, phone,countryCode,countryCodeName
            )
        )
    }

    private val _sendOTPResponse = MutableLiveData<Resource<SendEmailOTPResponse>>()
    val sendOTPResponse: LiveData<Resource<SendEmailOTPResponse>> = _sendOTPResponse

    fun sendEmailChangeOtp(
        email: String,
    )=viewModelScope.launch{
        _sendOTPResponse.value=repository.sendEmailOTP(
            SendEmailOTPRequest(
                email
            )
        )
    }

    private val _verifyOTPResponse = MutableLiveData<Resource<CommonResponse>>()
    val verifyOTPResponse: LiveData<Resource<CommonResponse>> = _verifyOTPResponse

    fun verifyEmailChangeOtp(
        otp: String,
        otpId: String,
    )=viewModelScope.launch{
        _verifyOTPResponse.value=repository.verifyEmailOTP(
            VerifyEmailOTPRequest(
                otp,
                otpId
            )
        )
    }


    private val _changePasswordResponse = MutableLiveData<Resource<AuthResponse>>()
    val changePasswordResponse: LiveData<Resource<AuthResponse>> = _changePasswordResponse

    fun changePassword(
        oldPassword: String,
        newPassword: String,

    )=viewModelScope.launch{
        _changePasswordResponse.value=repository.changePassword(
            ChangePasswordRequest(
                oldPassword,newPassword
            )
        )
    }
}