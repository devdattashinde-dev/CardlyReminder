package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.subscription.PlanData
import com.cardleyreminder.apps.databinding.PricingOptionItemLayoutBinding

class SubscriptionPlanAdapter(val context:Context): RecyclerView.Adapter<SubscriptionItemViewHolder>() {
    // Track the selected position (default to the first item)
    var selectedPosition = 1
        private set
    var items = mutableListOf<PlanData>()

    fun setData(addressList: List<PlanData>) {
        this.items = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionItemViewHolder {
        return SubscriptionItemViewHolder(PricingOptionItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: SubscriptionItemViewHolder, position: Int) {

        items.get(position).let {
            holder.binding.tvPlangLabel.text = it.heading
            holder.binding.tvPricing.text = "$${it.price}"
            holder.binding.tvPlanDuration.text = if(it.duration.equals("12")) "For 1 Year" else "Per Month"
            holder.binding.tvMostPopuler.visibility =  if(it.duration.equals("12")) View.VISIBLE else View.GONE

        }

        holder.binding.llContainer.isSelected = holder.bindingAdapterPosition==selectedPosition

        if(holder.bindingAdapterPosition==selectedPosition){
            holder.binding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_check))
        }else{
            holder.binding.ivCheck.setImageDrawable(context.getDrawable(R.drawable.ic_uncheck))
        }



        holder.binding.llContainer.setOnClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                selectedPosition = holder.bindingAdapterPosition
                notifyDataSetChanged()
                //onItemSelected(option)
            }
        }

    }

    override fun getItemCount() = items.size

    fun getSelectedPlan():PlanData = items.get(selectedPosition)
}

class SubscriptionItemViewHolder(val binding: PricingOptionItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)