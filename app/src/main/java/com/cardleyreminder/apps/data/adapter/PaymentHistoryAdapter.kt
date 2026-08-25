package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.subscription.PaymentHistoryData
import com.cardleyreminder.apps.databinding.PaymentHistoryItemLayoutBinding
import com.cardleyreminder.apps.util.convertDate

class PaymentHistoryAdapter(val context:Context): RecyclerView.Adapter<PaymentHistoryViewHolder>() {

    private var items = mutableListOf<PaymentHistoryData>()
    var onItemClick: ((PaymentHistoryData) -> Unit)? = null

    fun setData(addressList: List<PaymentHistoryData>) {
        this.items = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        return PaymentHistoryViewHolder(PaymentHistoryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {

        items.get(position).let {paymentData ->
            holder.binding.tvTransactionStatus.text = paymentData.paymentStatus?:""
            holder.binding.tvPaymentAmount.text = "Payment Amount - ${paymentData.currency}${paymentData.amount}"
            holder.binding.tvPaymentDate.text = "Payment Date - ${convertDate(context, paymentData.createdDate ?: "", "yyyy/MM/dd HH:mm:ss", "dd-MMM-yyyy ")}"
            holder.binding.tvPlanName.text = "Plan - ${paymentData.planName}"


            val isPaymentSuccessful = paymentData.paymentStatus == "Payment Successful"
            val iconRes = if (isPaymentSuccessful) R.drawable.ic_success_small else R.drawable.ic_failed_small
            holder.binding.ivStatus.setImageDrawable(ResourcesCompat.getDrawable(context.resources, iconRes, null))


            holder.itemView.setOnClickListener {
                onItemClick?.invoke(paymentData)
            }
        }


    }

    override fun getItemCount() = items.size

}

class PaymentHistoryViewHolder(val binding: PaymentHistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)