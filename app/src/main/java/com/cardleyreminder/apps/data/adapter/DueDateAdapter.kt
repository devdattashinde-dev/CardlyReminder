package com.cardleyreminder.apps.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.data.model.issuer.Card
import com.cardleyreminder.apps.databinding.DueDateViewLayoutBinding

class DueDateAdapter: RecyclerView.Adapter<DueDateViewHolder>() {
    var cards = mutableListOf<Card>()

    fun setList(addressList: List<Card>) {
        this.cards = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DueDateViewHolder {
        return DueDateViewHolder(DueDateViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: DueDateViewHolder, position: Int) {

        cards.get(position).let {
            holder.binding.tvCardNo.text = it.cardId.toString()
            holder.binding.tvBankName.text = it.cardName.toString()
        }
    }

    override fun getItemCount() = cards.size
}

class DueDateViewHolder(val binding: DueDateViewLayoutBinding) : RecyclerView.ViewHolder(binding.root)