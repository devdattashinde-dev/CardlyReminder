package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.data.model.calenderDate.PaymentDueData
import com.cardleyreminder.apps.data.model.calenderDate.StatementClosingData
import com.cardleyreminder.apps.databinding.CalenderDateItem2Binding


class SelectDateAdapter(private val context: Context?) : RecyclerView.Adapter<SelectDateViewHolder>() {
    private var statementClosingDateList = mutableListOf<StatementClosingData>()
    private var paymentDueDateList = mutableListOf<PaymentDueData>()
    var onItemClick: ((Int) -> Unit)? = null

    fun setStatementClosingDateList(list: List<StatementClosingData>) {
        this.statementClosingDateList = list.toMutableList()
        notifyDataSetChanged()
    }
    fun setPaymentDueDateList(list: List<PaymentDueData>) {
        this.paymentDueDateList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDateViewHolder {
        return SelectDateViewHolder(CalenderDateItem2Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: SelectDateViewHolder, position: Int) {

        holder.binding.tvDate.setText((position+1).toString())

        holder.itemView.setOnClickListener {
            onItemClick?.let { it1 -> it1(position+1) }
        }
    }

    override fun getItemCount() = 31//cards.size
}

class SelectDateViewHolder(val binding: CalenderDateItem2Binding) : RecyclerView.ViewHolder(binding.root)