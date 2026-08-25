package com.cardleyreminder.apps.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.data.model.issuer.CardColor
import com.cardleyreminder.apps.databinding.CardColorItemBinding

class CardColorAdapter: RecyclerView.Adapter<CardColorViewHolder>() {
    var cards = mutableListOf<CardColor>()

    fun setList(addressList: List<CardColor>) {
        this.cards = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardColorViewHolder {
        return CardColorViewHolder(CardColorItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CardColorViewHolder, position: Int) {

        cards.get(position).let {
           Glide.with(holder.itemView.context).load(it.smallColorImage)
               .into(holder.binding.ivColor)

            if(it.isSelected){
                holder.binding.ivCheck.visibility =  View.VISIBLE
            }else{
                holder.binding.ivCheck.visibility =  View.GONE
            }
        }

        holder.itemView.setOnClickListener(
            View.OnClickListener {
                cards.onEach { cardColor -> cardColor.isSelected = false }
                cards.get(position).isSelected = true
                notifyDataSetChanged()
            }
        )

    }

    override fun getItemCount() = cards.size
    fun getSelectedColorId(): String? {
        for(card in cards){
            if(card.isSelected)
                return card.cardColorId
        }

        return null
    }

    fun setSelectedColor(cardColorId: String?) {
        for(card in cards){
            if(card.cardColorId == cardColorId)
                card.cardColorId
        }

        cards.forEachIndexed { index, cardColor ->
            if(cards[index].cardColorId == cardColorId)
                cards[index].isSelected = true

            notifyItemChanged(index)
        }
    }
}

class CardColorViewHolder(val binding: CardColorItemBinding) : RecyclerView.ViewHolder(binding.root)