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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.network.AuthApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.AuthRepository
import com.cardleyreminder.apps.databinding.FragmentLoginBinding
import com.cardleyreminder.apps.ui.base.BaseFragment
import com.cardleyreminder.apps.ui.home.MainActivity
import com.cardleyreminder.apps.util.handleAPIError
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


class AuthFragment : BaseFragment<AuthViewModel,FragmentLoginBinding,AuthRepository>() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private var isPasswordVisible: Boolean = false

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
                            requireActivity().startNewActivity(MainActivity::class.java)
                        }else {
                            Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
        })

        viewModel.forgetPasswordResponse.observe(
            viewLifecycleOwner,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_LONG).show()
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

        binding.btLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(email.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_email_id_or_phone_number),Toast.LENGTH_LONG).show()
            else if(password.isEmpty())
                Toast.makeText(requireContext(),getString(R.string.please_enter_password),Toast.LENGTH_LONG).show()
            else {
                progressBar.show()
                viewModel.login(email, password)
            }
        }




        binding.tvForgotPassword.setOnClickListener {
            activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigate(
                AuthFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        binding.conLayGmailLogin.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                signInGoogle()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            activity?.findNavController(R.id.nav_host_fragment_content_main)?.navigate(
                AuthFragmentDirections.actionLoginFragmentToRegisterFragment())
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


        val text = "By signing in, you indicate that you had read and accept our Terms and Condition"

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
        spannableString.setSpan(ForegroundColorSpan(Color.YELLOW), privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/

        binding.tvTermsPrivacy.text = spannableString
        binding.tvTermsPrivacy.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun getToggleDrawable(isPasswordVisible: Boolean) = ContextCompat.getDrawable(
        requireContext(),
        if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
    )

    // signInGoogle() function
    private  fun signInGoogle(){

        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java,pref.token), pref)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you

        if(requestCode==Req_Code){
            progressBar.show()
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
            }else{
                progressBar.cancel()
            }
        } catch (e:ApiException){
            Log.e("Google Login",e.toString())
            Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
            progressBar.cancel()
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
}