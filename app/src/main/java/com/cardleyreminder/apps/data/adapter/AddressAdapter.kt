package com.cardleyreminder.apps.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.data.model.profile.UserAddrese
import com.cardleyreminder.apps.databinding.ProfileAddressItemBinding

class AddressAdapter: RecyclerView.Adapter<AddressViewHolder>() {
    var address = mutableListOf<UserAddrese>()

    fun setAddressList(addressList: List<UserAddrese>) {
        this.address = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(ProfileAddressItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        address.get(position).let {
            holder.binding.tvAddress.text = it.address
            holder.binding.tvCountry.text = it.country
            holder.binding.tvCity.text = it.city
            holder.binding.tvZipCode.text = it.zip_code
        }


    }

    override fun getItemCount() = address.size
}

class AddressViewHolder(val binding: ProfileAddressItemBinding) : RecyclerView.ViewHolder(binding.root)