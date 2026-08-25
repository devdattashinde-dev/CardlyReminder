package com.cardleyreminder.apps.data.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.home.HomeData
import com.cardleyreminder.apps.databinding.CalenderDateItemBinding


class CalenderAdapter(private val context: Context) : RecyclerView.Adapter<CalenderDateViewHolder>() {
    private var statementClosingDateList = mutableListOf<HomeData>()
    private var paymentDueDateList = mutableListOf<HomeData>()
    var onItemClick: ((Int) -> Unit)? = null


    fun setStatementClosingDateList(list: List<HomeData>) {
        this.statementClosingDateList = list.toMutableList()
        notifyDataSetChanged()
    }
    fun setPaymentDueDateList(list: List<HomeData>) {
        this.paymentDueDateList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderDateViewHolder {
        return CalenderDateViewHolder(CalenderDateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CalenderDateViewHolder, position: Int) {

        /*cards.get(position).let {
            *//*holder.binding.tvAddress.text = it.address
            holder.binding.tvCountry.text = it.country
            holder.binding.tvCity.text = it.city
            holder.binding.tvZipCode.text = it.zip_code*//*
        }*/

        /*if(position<9)
            holder.binding.tvDate.setText("0"+(position+1).toString())
        else*/

        holder.binding.tvDate.setText((position+1).toString())


        for(date in statementClosingDateList)
            if(date.statementClosingDate.equals(holder.binding.tvDate.text.toString())||(position<9 && date.statementClosingDate.equals("0"+holder.binding.tvDate.text.toString()))) {

                holder.isStatementClosingDate = true


            }

        for(date in paymentDueDateList)
            if(date.paymentDueDate.equals(holder.binding.tvDate.text.toString())||(position<9 && date.paymentDueDate.equals("0"+holder.binding.tvDate.text.toString())))
            {
                holder.isPaymentDueDate = true

                //holder.binding.tvDate.setTextColor(R.color.black)
            }

        if(holder.isStatementClosingDate && holder.isPaymentDueDate){
            holder.binding.clDate.background = AppCompatResources.getDrawable(context!!,R.drawable.rounded_orange_gradient)
        }else if(holder.isStatementClosingDate){
            holder.binding.clDate.background = AppCompatResources.getDrawable(context!!,R.drawable.rounded_yellow_gradient)
        }else if(holder.isPaymentDueDate){
            holder.binding.clDate.background = AppCompatResources.getDrawable(context!!,R.drawable.rounded_red_gradient)
        }else{
            holder.binding.clDate.background = AppCompatResources.getDrawable(context!!,R.drawable.rounded_green_gradient)
        }


        holder.itemView.setOnClickListener {
            var tempClosingList = statementClosingDateList.filter {
                it.statementClosingDate.equals((position+1).toString()) || it.statementClosingDate.equals("0"+(position+1).toString())

            }
            var tempDueList = paymentDueDateList.filter{
                it.paymentDueDate.equals((position+1).toString()) || it.paymentDueDate.equals("0"+(position+1))

            }

            if(tempClosingList.isNotEmpty() || tempDueList.isNotEmpty()){
                var db : AlertDialog? = null

                val mDialogView = LayoutInflater.from(context).inflate(R.layout.calender_date_dialog_layout, null)

                val ivClose = mDialogView.findViewById<ImageView>(R.id.ivClose)

                val tvDate = mDialogView.findViewById<TextView>(R.id.tvDate)
                tvDate.text = (position+1).toString()

                val tvClosingDate = mDialogView.findViewById<TextView>(R.id.tvClosingDate)
                val rvClosing = mDialogView.findViewById<RecyclerView>(R.id.rvClosing)

                val tvDueDate = mDialogView.findViewById<TextView>(R.id.tvDueDate)



                rvClosing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)


                val rvDue = mDialogView.findViewById<RecyclerView>(R.id.rvDue)
                rvDue.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

                tvClosingDate.text = context.getString(R.string.your_closing_date_is_the_every_month_for_this_cards)
                    .replace("##",(position+1).toString())
                    .replace("**",if(tempClosingList.size>1)"this" else "these")
                tvDueDate.text = context.getString(R.string.your_due_date_is_the_every_month_for_this_cards)
                    .replace("##",(position+1).toString())
                    .replace("**",if(tempClosingList.size>1)"this" else "these")


                if(tempClosingList.isNotEmpty()){
                    tvClosingDate.visibility = View.VISIBLE
                    rvClosing.visibility = View.VISIBLE

                    val adapter = CardListAdapter(context,null);
                    adapter.setData(tempClosingList)
                    rvClosing.adapter = adapter

                }else{
                    tvClosingDate.visibility = View.GONE
                    rvClosing.visibility = View.GONE
                }

                if(tempDueList.isNotEmpty()){
                    tvDueDate.visibility = View.VISIBLE
                    rvDue.visibility = View.VISIBLE

                    val adapter = CardListAdapter(context,null);
                    adapter.setData(tempDueList)
                    rvDue.adapter = adapter

                }else{
                    tvDueDate.visibility = View.GONE
                    rvDue.visibility = View.GONE
                }



                db = AlertDialog.Builder(context).show()
                db.setContentView(mDialogView)
                db.setCancelable(true)
                db.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                ivClose.setOnClickListener {
                    db.cancel()
                }
            }

        }
    }

    override fun getItemCount() = 31//cards.size
}

class CalenderDateViewHolder(val binding: CalenderDateItemBinding) : RecyclerView.ViewHolder(binding.root) {
    var isStatementClosingDate: Boolean = false
    var isPaymentDueDate: Boolean = false
}