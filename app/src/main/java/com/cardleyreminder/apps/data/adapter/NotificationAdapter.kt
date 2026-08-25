package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.notification.Notification
import com.cardleyreminder.apps.databinding.NotificationItemBinding
import com.cardleyreminder.apps.util.covertTimeToText

class NotificationAdapter(private val context: Context?) : RecyclerView.Adapter<NotificationViewHolder>() {

    var list = mutableListOf<Notification>()
    var onItemRemoved: ((Notification) -> Unit)? = null
    var onItemClick: ((Int,Notification) -> Unit)? = null
    fun setNotificationList(notificationList: List<Notification>) {
        this.list = notificationList.toMutableList()
        notifyDataSetChanged()
    }

    fun addNotificationList(notificationList: List<Notification>) {
        var size = this.list.size
        this.list.addAll(notificationList)
        var sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(NotificationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        list.get(position).let {


            //holder.binding.tvMessage.text = it.message
            holder.binding.tvTime.text = it.time?:covertTimeToText(context,it.createdDate)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.tvTitle.text = Html.fromHtml(it.title?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"), Html.FROM_HTML_MODE_COMPACT)
            } else {
                holder.binding.tvTitle.text = Html.fromHtml(it.title?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.tvMessage.text = Html.fromHtml(it.message?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"), Html.FROM_HTML_MODE_COMPACT)
            } else {
                holder.binding.tvMessage.text = Html.fromHtml(it.message?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"))
            }

            if(it.isSeen=="1"){
                holder.binding.imageView5.setImageDrawable(ResourcesCompat.getDrawable(context!!.resources,R.drawable.bg_circle_grey,null))
            }else{
                holder.binding.imageView5.setImageDrawable(ResourcesCompat.getDrawable(context!!.resources,R.drawable.bg_circle,null))
            }

            holder.itemView.setOnClickListener {_ ->
                onItemClick?.invoke(position,it)
            }

            /*holder.itemView.setOnClickListener {view ->
                if(it.type.equals("Shippment",true))
                    view.findNavController().navigate(
                        NotificationFragmentDirections.actionNotificationFragmentToShipmentDetailsFragment(
                            it.order_id,"0"
                        )
                    )
                else
                    view.findNavController().navigate(
                        NotificationFragmentDirections.actionNotificationFragmentToConsolidateDetailsFragment(
                            it.order_id
                        )
                    )
            }*/
        }


    }
    // Method to remove an item
    fun removeItem(position: Int) {
        onItemRemoved?.invoke(list[position])
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = list.size
    fun setMessageRead(selectedNotificationIndex: Int) {
        if(selectedNotificationIndex >=0){
            list[selectedNotificationIndex].isSeen = "1"
            notifyItemChanged(selectedNotificationIndex)
        }
    }
}

class NotificationViewHolder(val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)