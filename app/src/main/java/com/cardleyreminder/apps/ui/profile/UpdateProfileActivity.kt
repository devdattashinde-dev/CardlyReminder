package com.cardleyreminder.apps.ui.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.profile.SendEmailOTPResponse
import com.cardleyreminder.apps.data.model.profile.UserData
import com.cardleyreminder.apps.data.network.ProfileApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.ProfileRepository
import com.cardleyreminder.apps.databinding.ActivityProfileBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.isValidPhoneNumber
import com.cardleyreminder.apps.util.startNewActivity
import com.cardleyreminder.apps.util.toast
import com.mukeshsolanki.OtpView
import java.util.ArrayList

class UpdateProfileActivity : BaseActivity<ProfileViewModel, ActivityProfileBinding, ProfileRepository>() {


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

        viewModel.profileResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            setData(it.value.userData)
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.editProfileResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        progressBar.cancel()
                        binding.root.toast(it.value.message!!)
                        finish()
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.sendOTPResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status == true){
                            progressBar.cancel()
                            binding.root.toast(it.value.message!!)

                            showOTPDialog(it.value)
                        }else{
                            binding.root.toast(it.value.message!!)
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.verifyOTPResponse.observe(
            this,
            Observer {
                progressBar.cancel()
                when(it){
                    is Resource.Success ->{

                        if(it.value.status){
                            dialog.dismiss()
                            progressBar.show()
                            viewModel.editProfile(binding.etName.text.toString().trim(),
                                binding.etEmail.text.toString().trim(),
                                binding.etPhoneNo.text.toString().trim(),
                                binding.ccp.selectedCountryCodeWithPlus,binding.ccp.selectedCountryNameCode)
                        }
                        else{

                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }

            })


        binding.tvChangePassword.setOnClickListener {
            startNewActivity(ChangePasswordActivity::class.java)
        }

        progressBar.show()
        viewModel.getProfileData()
        
        
        binding.btUpdate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhoneNo.text.toString().trim()
            val countryCode = binding.ccp.selectedCountryCodeWithPlus
            val countryCodeName = binding.ccp.selectedCountryNameCode

            if(name.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_name), Toast.LENGTH_LONG).show()
            else if(email.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_email_id), Toast.LENGTH_LONG).show()
            else if(phone.isEmpty())
                Toast.makeText(this,getString(R.string.please_enter_phone_number),
                    Toast.LENGTH_LONG).show()
            else if(!isValidPhoneNumber(phone))
                Toast.makeText(this,getString(R.string.please_enter_valid_phone_number),
                    Toast.LENGTH_LONG).show()
            else if(email!=viewModel.userData.email){
                progressBar.show()
                viewModel.sendEmailChangeOtp(email)
            }else{
                progressBar.show()
                viewModel.editProfile(name,email, phone,countryCode,countryCodeName)
            }
        }

    }

    private fun setData(userData: ArrayList<UserData>) {

        userData[0].let {userData ->
            viewModel.userData = userData
            if(!userData.email.isNullOrBlank()){
                binding.etEmail.setText(userData.email)
            }

            if(!userData.firstName.isNullOrBlank()){
                binding.etName.setText(userData.firstName)
            }

            if(!userData.phone.isNullOrBlank()){
                binding.etPhoneNo.setText(userData.phone)
            }

            if(!userData.countryCodeName.isNullOrBlank()){
                binding.ccp.setCountryForNameCode(userData.countryCodeName!!)
            }else if(!userData.countryCode.isNullOrBlank()){
                binding.ccp.setCountryForPhoneCode(userData.countryCode!!.replace("+", "").toInt())
            }
        }
    }
    private fun showOTPDialog(otpData: SendEmailOTPResponse) {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.verify_email_otp_dialog_layout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        val otpView = dialog.findViewById(R.id.otp_view) as OtpView
        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView
        val btVerify = dialog.findViewById(R.id.btVerify) as ConstraintLayout

        otpView.setOtpCompletionListener {
            Log.d("Actual Value", it)
        }

        btVerify.setOnClickListener {

            if(otpView.text!!.isEmpty()) {
               it.toast("Please enter OTP")
            }else if(otpView.text!!.length<4) {
                it.toast("Please enter valid OTP")
            }else{
                progressBar.show()
                viewModel.verifyEmailChangeOtp(
                    otpView.text.toString(),
                    otpData.data!!
                )
            }
        }


        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getViewModel() = ProfileViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityProfileBinding.inflate(inflater)

    override fun getActivityRepository() = ProfileRepository(
    remoteDataSource.buildApi(ProfileApi::class.java,pref.token))
}