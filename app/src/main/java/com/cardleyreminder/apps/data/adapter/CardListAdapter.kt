package com.cardleyreminder.apps.data.adapter

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.home.HomeData
import com.cardleyreminder.apps.databinding.CardListItemBinding
import com.squareup.picasso.Picasso

class CardListAdapter(private val context: Context, private val noRecordsView: TextView?) : RecyclerView.Adapter<CardListViewHolder>() {
    var cards = mutableListOf<HomeData>()

    var onItemClick: ((HomeData?,Boolean) -> Unit)? = null

    fun setData(addressList: List<HomeData>) {
        this.cards.addAll(addressList.toMutableList())
        updateNoRecordsView()
        notifyDataSetChanged()
    }

    fun clearData() {
        this.cards.clear()
        updateNoRecordsView()
        notifyDataSetChanged()
    }

    private fun updateNoRecordsView() {
        noRecordsView?.visibility = if (cards.isEmpty()) View.VISIBLE else View.GONE
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListViewHolder {
        return CardListViewHolder(CardListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CardListViewHolder, position: Int) {

        cards[position].let {


            var textColor = if (!it.textColor.isNullOrEmpty())
                Color.parseColor(it.textColor)
            else
                context.resources.getColor(R.color.black,null)

            var cardColor = if(!it.colorValue.isNullOrEmpty())
                Color.parseColor(it.colorValue)
            else
                context.resources.getColor(R.color.primary,null)

            holder.binding.tvCardNo.setTextColor(textColor)
            holder.binding.tvBankName.setTextColor(textColor)
            holder.binding.tvStatementClosingDate.setTextColor(textColor)
            holder.binding.tvStatementClosingDayRemaining.setTextColor(textColor)
            holder.binding.tvDueDateRemaining.setTextColor(textColor)
            holder.binding.tvPaymentDueDate.setTextColor(textColor)
            holder.binding.tvDueDateLabel.setTextColor(textColor)
            holder.binding.tvClosingDateLabel.setTextColor(textColor)
            holder.binding.tvDaysRemaining1.setTextColor(textColor)
            holder.binding.tvDaysRemaining2.setTextColor(textColor)
            holder.binding.tvOfEachMonth1.setTextColor(textColor)
            holder.binding.tvOfEachMonth2.setTextColor(textColor)


            holder.binding.tvCardNo.text = it.cardNumber
            holder.binding.tvCardNo2.text = it.cardNumber

            holder.binding.tvBankName.text = it.cardName
            //holder.binding.tvBankName2.text = it.cardName

            holder.binding.tvStatementClosingDate.text = it.statementClosingDate
            holder.binding.tvStatementClosingDayRemaining.text = it.statementClosingLabel

            holder.binding.tvPaymentDueDate.text = it.paymentDueDate
            holder.binding.tvDueDateRemaining.text = it.paymentDueLabel

            holder.binding.tvValidThrough.text = it.cardExpireShort
            holder.binding.tvValidThroughRemaining.text = it.cardExpireLabel

            holder.binding.ivCardImageBack.imageTintList= ColorStateList.valueOf(cardColor)


            holder.binding.icInfo.imageTintList= ColorStateList.valueOf(textColor)
            //holder.binding.icInfo.imageTintList= ColorStateList.valueOf(textColor)

            /*Glide.with(context).load(it.cardImage)
                .into(holder.binding.ivCardImageFront)*/

            holder.itemView.setOnClickListener { _ ->
                holder.binding.flipView.flipTheView()
            }

            holder.binding.backView.setOnClickListener { it2 ->
                holder.binding.flipView.flipTheView()
            }

            holder.binding.icInfo.setOnClickListener { it3 ->
                showEditDeleteDialog(it)
            }
            holder.binding.icInfo2.setOnClickListener { _ ->
                showEditDeleteDialog(it)
            }

            if(it.cardImage!=null) {
                holder.binding.ivCardImageFront.visibility = View.VISIBLE
                //Picasso.get().load(it.cardImage).into(holder.binding.ivCardImageFront)
                //holder.binding.webViewCardImageFront.visibility = View.GONE

                Glide.with(context).load(it.cardImage).into(holder.binding.ivCardImageFront)


            }
            else{
                //holder.binding.webViewCardImageFront.visibility = View.GONE

                holder.binding.ivCardImageFront.visibility = View.VISIBLE
                holder.binding.ivCardImageFront.backgroundTintList= ColorStateList.valueOf(cardColor)
            }




            Picasso.get().load(it.cardNetworkImage).into(holder.binding.ivCardNetwork)
        }





        /*holder.binding.flipView.onFlipListener = object : OnFlipAnimationListener{
            override fun onViewFlipCompleted(
                easyFlipView: EasyFlipView?,
                newCurrentSide: EasyFlipView.FlipState?
            ) {

            }
            override fun onViewFlipStarted(
                easyFlipView: EasyFlipView?,
                newCurrentSide: EasyFlipView.FlipState?
            ) {
                if(pref.isCardFlipSoundOn){
                    playSound()
                }
            }

        }*/
    }

    fun setBase64Image(encodedImage: String, imageView: ImageView) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Base64Error", "Failed to decode Base64 image: ${e.message}")
        }
    }

    private fun getHtmlImageDataFromUrl(imageUrl: String): String {
        return """
            <html>
            <head>
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        background-color: #000;
                        height: 100vh;
                    }
                    img {
                        object-fit: cover;
                        width: 100vw;
                        height: 100vh;
                    }
                </style>
            </head>
            <body>
                <img src="$imageUrl" alt="Image">
            </body>
            </html>
        """
    }

    private fun showEditDeleteDialog(card: HomeData) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_edit_delete_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val tvEdit = dialog.findViewById(R.id.tvEdit) as TextView
        val tvDelete = dialog.findViewById(R.id.tvDelete) as TextView
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView



        tvEdit.setOnClickListener {

            if(onItemClick!=null) {
                onItemClick?.invoke(card,false)
            }


            dialog.dismiss()
        }

        tvDelete.setOnClickListener {
            if(onItemClick!=null) {
                onItemClick?.invoke(card,true)
            }
            dialog.dismiss()
        }

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun getItemCount() = cards.size
}

class CardListViewHolder(val binding: CardListItemBinding) : RecyclerView.ViewHolder(binding.root)