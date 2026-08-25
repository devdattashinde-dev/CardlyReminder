package com.cardleyreminder.apps.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cardleyreminder.apps.data.repository.*
import com.cardleyreminder.apps.ui.auth.AuthViewModel
import com.cardleyreminder.apps.ui.calender.CalenderViewModel
import com.cardleyreminder.apps.ui.home.HomeViewModel
import com.cardleyreminder.apps.ui.home.shipment.shipment_list.*
import com.cardleyreminder.apps.ui.insights.InsightsViewModel
import com.cardleyreminder.apps.ui.issuer.IssuerViewModel
import com.cardleyreminder.apps.ui.notification.NotificationViewModel
import com.cardleyreminder.apps.ui.profile.ProfileViewModel
import com.cardleyreminder.apps.ui.settings.SettingsViewModel
import com.cardleyreminder.apps.ui.subscription.SubscriptionViewModel

class ViewModelFactory(
    private val repository: BaseRepository
    ): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository as ProfileRepository) as T
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(repository as NotificationRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(repository as SettingsRepository) as T
            modelClass.isAssignableFrom(CalenderViewModel::class.java) -> CalenderViewModel(repository as CalenderRepository) as T
            modelClass.isAssignableFrom(IssuerViewModel::class.java) -> IssuerViewModel(repository as IssuerRepository) as T
            modelClass.isAssignableFrom(InsightsViewModel::class.java) -> InsightsViewModel(repository as InsightsRepository) as T
            modelClass.isAssignableFrom(SubscriptionViewModel::class.java) -> SubscriptionViewModel(repository as SubscriptionRepository) as T
            else -> throw IllegalArgumentException("ViewModel class not found")
        }
    }
}