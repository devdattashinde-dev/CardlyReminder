package com.cardleyreminder.apps.ui.calender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.calenderDate.CalenderDateResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.CalenderRepository
import kotlinx.coroutines.launch

class CalenderViewModel(
    private val repository: CalenderRepository
) : ViewModel() {

    private val _calenderDateResponse : MutableLiveData<Resource<CalenderDateResponse>> = MutableLiveData()

    val calenderDateResponse : LiveData<Resource<CalenderDateResponse>>
        get() = _calenderDateResponse

    fun getCalenderDateData(
    ) = viewModelScope.launch {
        _calenderDateResponse.value = repository.getCalenderDateData()
    }
}