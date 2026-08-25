package com.cardleyreminder.apps.ui.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.network.ProfileApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.ProfileRepository
import com.cardleyreminder.apps.databinding.ActivityChangePasswordBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError

class ChangePasswordActivity : BaseActivity<ProfileViewModel, ActivityChangePasswordBinding, ProfileRepository>() {

    private var isOldPasswordVisible: Boolean = false
    private var isPasswordVisible: Boolean = false
    private var isConfPasswordVisible: Boolean = false

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog,null)
        progressBar = Dialog(this).also {
            it.setContentView(dialogView)
            try {
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }


        binding.icBack.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })


        viewModel.changePasswordResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            Toast.makeText(this, it.value.message, Toast.LENGTH_LONG).show()
                            pref.saveUser(it.value.data!!)

                            logout()
                        }else {
                            Toast.makeText(this, it.value.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })



        binding.ivOldPassVisibility.setOnClickListener {
            isOldPasswordVisible=!isOldPasswordVisible
            if (isOldPasswordVisible) {
                binding.etOldPassword.inputType = InputType.TYPE_CLASS_TEXT

            } else {
                binding.etOldPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            binding.ivOldPassVisibility.setImageDrawable(getToggleDrawable(isOldPasswordVisible))
            binding.etOldPassword.setSelection( binding.etOldPassword.text?.length ?: 0)
        }

        binding.ivPassVisibility.setOnClickListener {
            isPasswordVisible=!isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT

            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            binding.ivPassVisibility.setImageDrawable(getToggleDrawable(isPasswordVisible))
            binding.etPassword.setSelection( binding.etPassword.text?.length ?: 0)
        }

        binding.ivConfPassVisibility.setOnClickListener {
            isConfPasswordVisible=!isConfPasswordVisible
            if (isConfPasswordVisible) {
                binding.etConfPassword.inputType = InputType.TYPE_CLASS_TEXT

            } else {
                binding.etConfPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            binding.ivConfPassVisibility.setImageDrawable(getToggleDrawable(isConfPasswordVisible))
            binding.etConfPassword.setSelection( binding.etConfPassword.text?.length ?: 0)
        }

        
        binding.btUpdate.setOnClickListener {

            val oldPassword = binding.etOldPassword.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confPassword = binding.etConfPassword.text.toString().trim()

            if(oldPassword.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_your_old_password), Toast.LENGTH_LONG).show()
            else if(password.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_new_password),Toast.LENGTH_LONG).show()
            else if(confPassword.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_conf_password),Toast.LENGTH_LONG).show()
            else if(password != confPassword)
                Toast.makeText(this,getString(R.string.your_pass_and_conf_password_not_match),Toast.LENGTH_LONG).show()
            else{
                progressBar.show()
                viewModel.changePassword(oldPassword,password)
            }
        }

    }

    private fun getToggleDrawable(isPasswordVisible: Boolean) = ContextCompat.getDrawable(
        this,
        if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
    )


    override fun getViewModel() = ProfileViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityChangePasswordBinding.inflate(inflater)

    override fun getActivityRepository() = ProfileRepository(
    remoteDataSource.buildApi(ProfileApi::class.java,pref.token))
}