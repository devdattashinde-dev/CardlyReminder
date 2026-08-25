package com.cardleyreminder.apps.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.data.model.profile.Document
import com.cardleyreminder.apps.databinding.UserIdItemBinding

class DocumentsAdapter: RecyclerView.Adapter<DocumentsViewHolder>() {
    var documents = mutableListOf<Document>()

    fun setList(list: List<Document>) {
        this.documents = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        return DocumentsViewHolder(UserIdItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {

        documents.get(position).let {
            Glide.with(holder.itemView.context).load(it.url).into(holder.binding.imageView)
        }

    }

    override fun getItemCount() = documents.size
}

class DocumentsViewHolder(val binding: UserIdItemBinding) : RecyclerView.ViewHolder(binding.root)