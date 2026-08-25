package com.cardleyreminder.apps.ui.insights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardleyreminder.apps.data.model.insights.FAQDataResponse
import com.cardleyreminder.apps.data.model.insights.FactsDataResponse
import com.cardleyreminder.apps.data.model.insights.PageDataRequest
import com.cardleyreminder.apps.data.model.insights.PageDataResponse
import com.cardleyreminder.apps.data.model.insights.TipDataResponse
import com.cardleyreminder.apps.data.model.insights.VideoData
import com.cardleyreminder.apps.data.model.insights.VideoListResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.InsightsRepository
import kotlinx.coroutines.launch

class InsightsViewModel(
    private val repository: InsightsRepository
) : ViewModel() {

    lateinit var videoListData: VideoData
    private val _pageDataResponse : MutableLiveData<Resource<PageDataResponse>> = MutableLiveData()

    val pageDataResponse : LiveData<Resource<PageDataResponse>>
        get() = _pageDataResponse

    fun getPageData(
        request: PageDataRequest
    ) = viewModelScope.launch {
        _pageDataResponse.value = repository.getPageData(request)
    }

    //Faq data
    private val _fAQDataResponse : MutableLiveData<Resource<FAQDataResponse>> = MutableLiveData()

    val fAQDataResponse : LiveData<Resource<FAQDataResponse>>
        get() = _fAQDataResponse

    fun getFAQData() = viewModelScope.launch {
        _fAQDataResponse.value = repository.getFAQData()
    }



    //Faq data
    private val _videoDataResponse : MutableLiveData<Resource<VideoListResponse>> = MutableLiveData()

    val videoDataResponse : LiveData<Resource<VideoListResponse>>
        get() = _videoDataResponse

    fun getVideoList() = viewModelScope.launch {
        _videoDataResponse.value = repository.getVideoList()
    }



    //Tips data
    private val _tipsDataResponse : MutableLiveData<Resource<TipDataResponse>> = MutableLiveData()

    val tipsDataResponse : LiveData<Resource<TipDataResponse>>
        get() = _tipsDataResponse

    fun getTipsData() = viewModelScope.launch {
        _tipsDataResponse.value = repository.getTipsData()
    }

    //Tips data
    private val _factsDataResponse : MutableLiveData<Resource<FactsDataResponse>> = MutableLiveData()

    val factsDataResponse : LiveData<Resource<FactsDataResponse>>
        get() = _factsDataResponse

    fun getFactsData() = viewModelScope.launch {
        _factsDataResponse.value = repository.getFactsData()
    }
}