package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.data.model.issuer.CardNetwork
import com.cardleyreminder.apps.databinding.CardNetworkItemBinding

class CardNetworkAdapter(private val context: Context?) : RecyclerView.Adapter<CardNetworkViewHolder>() {
    var cards = mutableListOf<CardNetwork>()

    var onItemClick: ((CardNetwork?) -> Unit)? = null

    fun setList(data: List<CardNetwork>) {
        this.cards = data.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardNetworkViewHolder {
        return CardNetworkViewHolder(CardNetworkItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CardNetworkViewHolder, position: Int) {

        cards.get(position).let {
            holder.binding.tvCount.text = it.cardCount
            holder.binding.tvNetworkTitle.text = it.name
            Glide.with(context!!).load(it.image).into(holder.binding.ivNetworkIcon)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cards.get(position))
        }
    }

    override fun getItemCount() = cards.size

}

class CardNetworkViewHolder(val binding: CardNetworkItemBinding) : RecyclerView.ViewHolder(binding.root)