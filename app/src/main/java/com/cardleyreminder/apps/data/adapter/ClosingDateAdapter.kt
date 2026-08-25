package com.cardleyreminder.apps.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.data.model.home.HomeData
import com.cardleyreminder.apps.databinding.ClosingViewLayoutBinding

class ClosingDateAdapter: RecyclerView.Adapter<ClosingDateViewHolder>() {
    var cards = mutableListOf<HomeData>()

    fun setList(addressList: List<HomeData>) {
        this.cards = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingDateViewHolder {
        return ClosingDateViewHolder(ClosingViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ClosingDateViewHolder, position: Int) {

        cards.get(position).let {
            holder.binding.tvCardNo.text = it.cardId.toString()
            holder.binding.tvBankName.text = it.cardName.toString()
        }
    }

    override fun getItemCount() = cards.size
}

class ClosingDateViewHolder(val binding: ClosingViewLayoutBinding) : RecyclerView.ViewHolder(binding.root)