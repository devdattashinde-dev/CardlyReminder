package com.cardleyreminder.apps.ui.subscription

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.cardleyreminder.apps.R

class PaymentFailedDialog(private val onRetry: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_payment_failed)

        // Handle Retry Button
        val btnRetry = dialog.findViewById<Button>(R.id.btnRetry)
        btnRetry.setOnClickListener {
            dialog.dismiss()
            onRetry.invoke() // Trigger retry logic
        }

        // Handle Cancel Button
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}