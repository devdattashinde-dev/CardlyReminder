package com.cardleyreminder.apps.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.cardleyreminder.apps.UserPreferences
import com.cardleyreminder.apps.data.network.RemoteDataSource
import com.cardleyreminder.apps.data.repository.BaseRepository
import com.cardleyreminder.apps.ui.auth.AuthActivity
import com.cardleyreminder.apps.util.startNewActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


abstract class BaseFragmentActivity<VM : ViewModel, B : ViewBinding, BR :BaseRepository> : FragmentActivity() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    protected val remoteDataSource = RemoteDataSource()

    protected lateinit var pref: UserPreferences
    protected lateinit var progressBar: Dialog


   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        return binding.root
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = UserPreferences(this)
        binding = getActivityBinding(layoutInflater)
        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        setContentView(binding.root)
    }

    fun logout()= lifecycleScope.launch{
        FirebaseAuth.getInstance().signOut()
        pref.clear()
        startNewActivity(AuthActivity::class.java)
    }


    abstract fun getViewModel(): Class<VM>

    abstract fun getActivityBinding(inflater: LayoutInflater): B

    abstract fun getActivityRepository(): BR
}