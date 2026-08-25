package com.cardleyreminder.apps.ui.auth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R

import com.cardleyreminder.apps.data.network.AuthApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.AuthRepository
import com.cardleyreminder.apps.databinding.FragmentRegisterBinding
import com.cardleyreminder.apps.ui.home.MainActivity
import com.cardleyreminder.apps.ui.base.BaseFragment
import com.cardleyreminder.apps.ui.home.shipment.shipment_list.RegisterViewModel
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.isValidPhoneNumber
import com.cardleyreminder.apps.util.openWebView
import com.cardleyreminder.apps.util.startNewActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding,AuthRepository>() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private var isPasswordVisible: Boolean = false
    private var isConfPasswordVisible: Boolean = false
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

        viewModel.loginResponse.observe(
            viewLifecycleOwner,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            viewModel.saveUser(it.value.data!!)
                            //requireActivity().startNewActivity(MainActivity::class.java)

                            Intent(requireActivity(),MainActivity::class.java).also {
                                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                it.putExtra("isFromRegister",true)
                                startActivity(it)
                            }
                        }else {
                            Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
        })
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
        binding.btRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhoneNo.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confPassword = binding.etConfPassword.text.toString().trim()
            val countryCode = binding.ccp.selectedCountryCodeWithPlus
            val countryCodeName = binding.ccp.selectedCountryNameCode

            if(name.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_name),Toast.LENGTH_LONG).show()
            else if(email.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_email_id),Toast.LENGTH_LONG).show()
            else if(phone.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_phone_number),Toast.LENGTH_LONG).show()
            else if(!isValidPhoneNumber(phone))
                Toast.makeText(requireContext(),getString(R.string.please_enter_valid_phone_number),Toast.LENGTH_LONG).show()
            else if(password.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_password),Toast.LENGTH_LONG).show()
            else if(confPassword.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_conf_password),Toast.LENGTH_LONG).show()
            else if(password != confPassword)
                Toast.makeText(requireContext(),getString(R.string.your_pass_and_conf_password_not_match),Toast.LENGTH_LONG).show()
            else {
                progressBar.show()
                viewModel.register(name,email, phone,password,countryCode,countryCodeName)
            }
        }

        binding.conLayGmailLogin.setOnClickListener {
            // Sign out first to force account chooser
            mGoogleSignInClient.signOut().addOnCompleteListener {
                signInGoogle()
            }

        }

        binding.tvBackToLogin.setOnClickListener() {
            activity?.onBackPressed()
        }

        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        // initialize the firebaseAuth variable
        firebaseAuth= FirebaseAuth.getInstance()



        val text = "By registering, you indicate that you had read and accept our Terms and Condition"

        val spannableString = SpannableString(text)

        // Set Yellow Color for "Terms of Use"
        val termsStart = text.indexOf("Terms and Condition")
        val termsEnd = termsStart + "Terms and Condition".length
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openWebView(requireActivity(),"https://cardlyreminder.com/terms-of-use/app",getString(R.string.terms_of_use))

            }
        }, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.YELLOW), termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        /*// Set Yellow Color for "Privacy Policy"
        val privacyStart = text.indexOf("Privacy Policy")
        val privacyEnd = privacyStart + "Privacy Policy".length
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openWebView(requireActivity(),"https://cardlyreminder.com/privacy-policy/app")
            }
        }, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.YELLOW), privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
*/
        binding.tvTermsPrivacy.text = spannableString
        binding.tvTermsPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }
    private fun getToggleDrawable(isPasswordVisible: Boolean) = ContextCompat.getDrawable(
        requireContext(),
        if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
    )

    override fun getViewModel() = RegisterViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java,pref.token), pref)

    // signInGoogle() function
    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you

        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                var email = account.email.toString()
                var name = account.displayName.toString()
                var password = account.id.toString()
                firebaseAuth.signOut()
                viewModel.socialLogin(name,email,password)
            }
        }
    }

    /*binding.btLogin.setOnClickListener(){
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }*/
}