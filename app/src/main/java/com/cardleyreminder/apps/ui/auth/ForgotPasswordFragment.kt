package com.cardleyreminder.apps.ui.auth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.network.AuthApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.AuthRepository
import com.cardleyreminder.apps.databinding.ForgotPasswordDialogLayoutBinding
import com.cardleyreminder.apps.ui.base.BaseFragment
import com.cardleyreminder.apps.util.handleAPIError


class ForgotPasswordFragment : BaseFragment<AuthViewModel,ForgotPasswordDialogLayoutBinding,AuthRepository>() {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.progress_dialog,null)
        progressBar = Dialog(requireContext()).also {
            it.setContentView(dialogView)
            try {
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }



        viewModel.forgetPasswordResponse.observe(
            viewLifecycleOwner,
            Observer {

                when(it){
                    is Resource.Success ->{

                        Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_LONG).show()

                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
        })


        binding.btSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if(email.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_email_id),Toast.LENGTH_LONG).show()
            else {
                progressBar.show()
                viewModel.forgotPassword(email)
            }

        }

    }



    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ForgotPasswordDialogLayoutBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java,pref.token), pref)


}