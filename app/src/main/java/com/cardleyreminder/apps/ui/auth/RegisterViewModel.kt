package com.cardleyreminder.apps.ui.home.shipment.shipment_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.BuildConfig
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.AuthRepository
import com.cardleyreminder.apps.data.model.auth.AuthResponse
import com.cardleyreminder.apps.data.model.auth.User
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse : MutableLiveData<Resource<AuthResponse>> = MutableLiveData()

    val loginResponse : LiveData<Resource<AuthResponse>>
        get() = _loginResponse

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        countryCode: String,
        countryCodeName: String,
    ) = viewModelScope.launch {
        _loginResponse.value = repository.register(name,email,phone,password,countryCode,BuildConfig.VERSION_CODE.toString(),countryCodeName)
    }

    fun saveUser(user: User){
        repository.saveUser(user)
    }

    fun socialLogin(
        name: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        _loginResponse.value = repository.socialLogin(name,email,password,BuildConfig.VERSION_CODE.toString())
    }
}