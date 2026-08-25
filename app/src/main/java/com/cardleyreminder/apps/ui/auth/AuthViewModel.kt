package com.cardleyreminder.apps.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.BuildConfig
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.AuthRepository
import com.cardleyreminder.apps.data.model.auth.AuthResponse
import com.cardleyreminder.apps.data.model.auth.User
import com.cardleyreminder.apps.data.model.issuer.CommonResponse
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()

    val loginResponse : LiveData<Resource<AuthResponse>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = repository.login(email,password, BuildConfig.VERSION_CODE.toString())
    }

    fun socialLogin(
        name: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        _loginResponse.value = repository.socialLogin(name,email,password,BuildConfig.VERSION_CODE.toString())
    }

    fun saveUser(user: User){
        repository.saveUser(user)
    }


    private val _forgetPasswordResponse : MutableLiveData<Resource<CommonResponse>> = MutableLiveData()

    val forgetPasswordResponse : LiveData<Resource<CommonResponse>>
        get() = _forgetPasswordResponse

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgetPasswordResponse.value = repository.forgotPassword(email)
    }

}