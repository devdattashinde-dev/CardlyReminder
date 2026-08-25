package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.issuer.Issuers
import com.cardleyreminder.apps.databinding.IssuerCategoriesItemBinding

class IssuerCategoriesAdapter(private val context: Context) : RecyclerView.Adapter<IssuerCategoriesViewHolder>() {
    var cards = mutableListOf<Issuers>()

    var onItemClick: ((Issuers?) -> Unit)? = null

    fun setList(addressList: List<Issuers>) {
        this.cards = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuerCategoriesViewHolder {
        return IssuerCategoriesViewHolder(IssuerCategoriesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: IssuerCategoriesViewHolder, position: Int) {

        cards[position].let {
            holder.binding.tvIssuer.setText(it.issuerName)

            if(!it.issuerImage.isNullOrEmpty()){
                Glide.with(context)
                    .load(it.issuerImage)
                    .error(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_card,null))
                    .into(holder.binding.ivIssuerImage)
            }

        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cards.get(position))
        }
    }

    override fun getItemCount() = cards.size
}

class IssuerCategoriesViewHolder(val binding: IssuerCategoriesItemBinding) : RecyclerView.ViewHolder(binding.root)