package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.issuer.BankDetail
import java.util.ArrayList


class IssuerBankArrayAdapter(context: Context, @LayoutRes private val layoutResource: Int, textviewId: Int, private val items: ArrayList<BankDetail>):
    ArrayAdapter<BankDetail>(context, layoutResource,textviewId ,items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Inflate and return the simple selected item view
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView: TextView = view.findViewById(R.id.textView)
        textView.text = getItem(position)?.bankName
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

       val tvLabel = view.findViewById<TextView>(R.id.tvLabel)
       val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)

        if(position<items.size) {
            tvLabel.text = items[position].bankName

            if(!items[position].bankImage.isNullOrEmpty())
                Glide.with(context).load(items[position].bankImage).placeholder(R.drawable.ic_loading).into(ivIcon)

            if(position==0)
                ivIcon.visibility = View.INVISIBLE
            else
                ivIcon.visibility = View.VISIBLE
        }
        return view
    }

}