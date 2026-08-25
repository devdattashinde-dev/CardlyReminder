package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.insights.FactsData
import com.cardleyreminder.apps.databinding.FaqItemLayoutBinding

class FactsAdapter(val context: Context): RecyclerView.Adapter<FactsViewHolder>() {
    private var address = mutableListOf<FactsData>()

    fun setListData(addressList: List<FactsData>) {
        this.address = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        return FactsViewHolder(FaqItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {

        address[position].let {
            holder.binding.tvHeading.text = it.heading
            holder.binding.tvDescription.text = it.description
            holder.binding.tvDescription.setTextColor(context.getColor(R.color.black))
            holder.binding.linLayBody.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFF66"))

            holder.binding.linLayHeader.setOnClickListener {

                if(holder.binding.linLayBody.visibility == View.GONE){
                    holder.binding.linLayBody.visibility = View.VISIBLE
                    holder.binding.ivArrow.setImageDrawable(
                        ResourcesCompat.getDrawable(context.resources,
                            R.drawable.ic_arrow_up,null))


                }else{
                    holder.binding.linLayBody.visibility = View.GONE
                    holder.binding.ivArrow.setImageDrawable(
                        ResourcesCompat.getDrawable(context.resources,
                            R.drawable.ic_arrow_doun,null))
                }

            }
        }


    }

    override fun getItemCount() = address.size
}

class FactsViewHolder(val binding: FaqItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)