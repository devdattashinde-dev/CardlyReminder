package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.issuer.BankDetail
import com.cardleyreminder.apps.databinding.IssuerBankItemBinding
import com.squareup.picasso.Picasso

class IssuerBankAdapter(private val context: Context?) : RecyclerView.Adapter<IssuerBankViewHolder>() {
    var cards = mutableListOf<BankDetail>()
    var onItemClick: ((BankDetail?) -> Unit)? = null
    fun setList(addressList: List<BankDetail>) {
        this.cards = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuerBankViewHolder {
        return IssuerBankViewHolder(IssuerBankItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: IssuerBankViewHolder, position: Int) {

        cards[position].let {

            if (context != null) {
                Picasso.get().load(it.bankImage).into(holder.binding.ivBankImage)
            }else{
                holder.binding.ivBankImage.setImageResource(R.drawable.no_img)
            }
            holder.binding.tvBankName.text = it.bankName


            holder.itemView.setOnClickListener {_->
                if(it.clickable)
                    onItemClick?.invoke(it)
            }
        }




    }

    override fun getItemCount() = cards.size
}

class IssuerBankViewHolder(val binding: IssuerBankItemBinding) : RecyclerView.ViewHolder(binding.root)