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
import com.cardleyreminder.apps.data.model.insights.FAQData
import com.cardleyreminder.apps.databinding.FaqItemLayoutBinding

class FAQAdapter(val context: Context): RecyclerView.Adapter<FaqViewHolder>() {
    private var address = mutableListOf<FAQData>()

    fun setListData(addressList: ArrayList<FAQData>) {
        this.address = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        return FaqViewHolder(FaqItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {

        address[position].let {
            holder.binding.tvHeading.text = it.heading
            holder.binding.tvDescription.text = it.description
            holder.binding.linLayBody.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#99FF99"))
            holder.binding.tvDescription.setTextColor(Color.BLACK)

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

class FaqViewHolder(val binding: FaqItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)