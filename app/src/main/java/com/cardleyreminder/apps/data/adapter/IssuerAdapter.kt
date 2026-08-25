package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.data.model.issuer.CardDetail
import com.cardleyreminder.apps.databinding.IssuerItemBinding

class IssuerAdapter(private val context: Context?) : RecyclerView.Adapter<IssuerViewHolder>() {
    var cards = mutableListOf<CardDetail>()
    var onItemClick: ((CardDetail?) -> Unit)? = null
    fun setList(list: List<CardDetail>) {
        this.cards = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuerViewHolder {
        return IssuerViewHolder(IssuerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: IssuerViewHolder, position: Int) {

        cards[position].let {
            holder.binding.tvCount.text = it.cardCount
            holder.binding.tvTitle.text = it.issuerCardName
            Glide.with(context!!).load(it.issuerCardImage).into(holder.binding.ivCreditCard)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cards[position])
        }
    }

    override fun getItemCount() = cards.size
}

class IssuerViewHolder(val binding: IssuerItemBinding) : RecyclerView.ViewHolder(binding.root)