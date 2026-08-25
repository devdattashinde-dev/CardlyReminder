package com.cardleyreminder.apps.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cardleyreminder.apps.data.adapter.NotificationAdapter
import com.cardleyreminder.apps.data.network.NotificationApi
import com.cardleyreminder.apps.data.repository.NotificationRepository
import com.cardleyreminder.apps.databinding.FragmentNotificationBinding
import com.cardleyreminder.apps.ui.base.BaseFragment

class NotificationFragment : BaseFragment<NotificationViewModel, FragmentNotificationBinding, NotificationRepository>() {

    var pagination = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    lateinit var notificationAdapter:NotificationAdapter;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun getViewModel() = NotificationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = NotificationRepository(
    remoteDataSource.buildApi(NotificationApi::class.java,pref.token))
}