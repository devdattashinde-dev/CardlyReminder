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
import com.cardleyreminder.apps.data.model.insights.TipData
import com.cardleyreminder.apps.databinding.FaqItemLayoutBinding

class TipsAdapter(val context: Context): RecyclerView.Adapter<TipsViewHolder>() {
    private var address = mutableListOf<TipData>()

    fun setListData(addressList: List<TipData>) {
        this.address = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        return TipsViewHolder(FaqItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {

        address[position].let {
            holder.binding.tvHeading.text = it.heading
            holder.binding.tvDescription.text = it.description

            holder.binding.linLayBody.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#33CCFF"))

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

class TipsViewHolder(val binding: FaqItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)