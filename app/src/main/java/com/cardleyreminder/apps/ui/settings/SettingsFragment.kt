package com.cardleyreminder.apps.ui.settings

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.network.SettingsApi
import com.cardleyreminder.apps.data.repository.SettingsRepository
import com.cardleyreminder.apps.databinding.FragmentSettingsBinding
import com.cardleyreminder.apps.ui.WebViewActivity
import com.cardleyreminder.apps.ui.base.BaseFragment
import com.cardleyreminder.apps.ui.profile.UpdateProfileActivity
import com.suke.widget.SwitchButton
import java.lang.Exception

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding, SettingsRepository>() {



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

        binding.keyboardClickSwitchButton.isChecked = pref.isKeyboardSoundOn
        binding.cardFlipSwitchButton.isChecked = pref.isCardFlipSoundOn

        binding.keyboardClickSwitchButton.setOnCheckedChangeListener(SwitchButton.OnCheckedChangeListener {
                view, isChecked ->
            pref.saveKeybaordSound(isChecked)
        })

        binding.cardFlipSwitchButton.setOnCheckedChangeListener(SwitchButton.OnCheckedChangeListener {
                view, isChecked ->
            pref.saveCardFlipSound(isChecked)
        })

        binding.linLayContactUs.setOnClickListener(View.OnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),ContactUsActivity::class.java))
        })

        binding.linLayCardlyMessage.setOnClickListener(View.OnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),OurCardlyMessageActivity::class.java))
        })

        binding.linLayOurStory.setOnClickListener(View.OnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),OurStoryActivity::class.java))
        })

        binding.linLayUpdateProfile.setOnClickListener(View.OnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), UpdateProfileActivity::class.java))
        })

        binding.linLayTermsOfUse.setOnClickListener {

            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("URL", "https://cardlyreminder.com/terms-of-use/app")
            intent.putExtra("title", getString(R.string.terms_of_use))
            startActivity(intent)
        }

        binding.linLayPrivacyPolicy.setOnClickListener {

            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("URL", "https://cardlyreminder.com/privacy-policy/app")
            intent.putExtra("title", getString(R.string.privacy_policy))
            startActivity(intent)
        }
    }

    override fun getViewModel() = SettingsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = SettingsRepository(
    remoteDataSource.buildApi(SettingsApi::class.java,pref.token))
}