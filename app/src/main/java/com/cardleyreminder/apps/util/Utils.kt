package com.cardleyreminder.apps.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.ui.WebViewActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

const val STATUS_NEW = 0
const val STATUS_ACTIVE = 1
const val STATUS_EXPIRED = 2
const val STATUS_TRIAL = 3

var dateArray = arrayOf("01","02","03","04","05","06","07",
    "08","09","10","11","12")

fun<A : Activity> Activity.startNewActivity(activity : Class<A>){
    Intent(this,activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun<A : Activity> Activity.startNewStackActivity(activity : Class<A>){
    Intent(this,activity).also {
        startActivity(it)
    }
}

fun View.visible(isVisible:Boolean){
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled:Boolean){
    isEnabled = enabled
    alpha = if(enabled) 1f else 0.5f
}

fun View.alertDialog(message : String,
                     action : (()-> Unit)?=null,
                     positiveButtonLabel : String?=null,
                     negativeButtonLabel : String?=null){

    var alertDialog: AlertDialog?=null

    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setNegativeButton(negativeButtonLabel ?: "Cancel"){ dialogInterface, which ->
        alertDialog?.cancel()
    }

    action?.let {
        alertDialogBuilder.setPositiveButton(negativeButtonLabel ?: "Ok"){dialogInterface , which ->
            alertDialog?.cancel()
            it()
        }
    }

    alertDialog = alertDialogBuilder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun View.showInfoDialog(context: Context,title:String , message : String) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.info_dialog)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    val ivClose = dialog.findViewById(R.id.ivClose) as ImageView
    val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
    val tvMessage = dialog.findViewById(R.id.tvMessage) as TextView

    tvTitle.text = title
    tvMessage.text = message

    ivClose.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun View.toast(message:String){
    val inflater = LayoutInflater.from(context)
    val layout = inflater.inflate(R.layout.custom_toast, null) // Custom layout

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}

fun Fragment.handleAPIError(
    failure : Resource.Failure,
    retry : (()-> Unit)?=null
){
    when{
        failure.isNetworkError -> {
            requireView().alertDialog("Please check internet connection",retry,"Retry")
        }
        failure.errorCode == 401 ->{
            // TODO: add logout functionality
        }
        else ->{
            var error : String
            if(failure.errorBody!=null)
                error = failure.errorBody?.string().toString()
            else
                error = failure.cause?:""

            requireView().toast(error)
        }
    }
}

fun Fragment.handleError(
    error: BaseResponse,
    action: (()-> Unit)?=null
){
    if(error.message!!.contains("Invalid authentication",true)){
       action?.let {
           it()
       }
    }else {
        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
    }
}


fun Activity.handleAPIError(
    failure : Resource.Failure,
    retry : (()-> Unit)?=null
){
    when{
        failure.isNetworkError -> {
            window.decorView.rootView.alertDialog("Please check internet connection",retry,"Retry")
        }
        failure.errorCode == 401 ->{
            // TODO: add logout functionality
        }
        else ->{
            var error : String
            if(failure.errorBody!=null)
                error = failure.errorBody?.string().toString()
            else
                error = failure.cause?:""

            window.decorView.rootView.toast(error)
        }
    }
}

fun Activity.handleError(
    error: BaseResponse,
    action: (()-> Unit)?=null
){
    if(error.message!!.contains("Invalid authentication",true)){
        action?.let {
            it()
        }
    }else {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }
}

fun covertTimeToText(context:Context?,dataDate: String?): String? {
    var convTime: String? = null
    val prefix = ""
    val suffix = context?.getString(R.string.ago)
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val pasTime: Date = dateFormat.parse(dataDate)
        val nowTime = Date()
        val dateDiff: Long = nowTime.getTime() - pasTime.getTime()
        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
        if (second < 60) {
            convTime = "${EnglishToArabic(second.toString())} ${context?.getString(R.string.seconds)} $suffix"
        } else if (minute < 60) {
            convTime = "${EnglishToArabic(minute.toString())} ${context?.getString(R.string.minutes)} $suffix"
        } else if (hour < 24) {
            convTime = "${EnglishToArabic(hour.toString())} ${context?.getString(R.string.hours)} $suffix"
        } else if (day >= 7) {
            convTime = if (day > 360) {
                EnglishToArabic((day / 360).toString()) + " ${context?.getString(R.string.years)} " + suffix
            } else if (day > 30) {
                EnglishToArabic((day / 30).toString()) + " ${context?.getString(R.string.month)} " + suffix
            } else {
                EnglishToArabic((day / 7).toString()) + " ${context?.getString(R.string.days)} " + suffix
            }
        } else if (day < 7) {
            convTime = "${EnglishToArabic(day.toString())} Days $suffix"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message!!)
    }
    return convTime
}

fun convertDate(context: Context, stringDate: String, fromFormat: String, toFormat: String): String {
    return try {
        val inputFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
        val outputFormat = SimpleDateFormat(toFormat, Locale.getDefault())

        val date = inputFormat.parse(stringDate) // Parse input date
        date?.let { outputFormat.format(it) } ?: stringDate
    } catch (e: Exception) {
        e.printStackTrace()
        stringDate
    }
}

fun EnglishToArabic(str: String):String {
    var result = ""

    var en = '0'
    if(!Locale.getDefault().getLanguage().equals("en",true)){
        for (ch in str) {
            en = ch
            when (ch) {
                '0' -> en = '۰'
                '1' -> en = '۱'
                '2' -> en = '۲'
                '3' -> en = '۳'
                '4' -> en = '۴'
                '5' -> en = '۵'
                '6' -> en = '۶'
                '7' -> en = '۷'
                '8' -> en = '۸'
                '9' -> en = '۹'
            }
            result = "${result}$en"
        }
    }else{
        result=str
    }
    return result


}

fun  setNotificationBadge(context: Context,count : Int){
    val notificationManager = context.getSystemService(NotificationManager::class.java)
// Create a silent notification (it won't be visible)
    val CHANNEL_ID = "cardly_not_callnel_id"

    // Create a silent notification channel (if not already created)
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(CHANNEL_ID, "Badge Notifications", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }


    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.logo_icon)
        .setContentTitle("")
        .setContentText("")
        .setNumber(count) // Sets the badge count
        .setAutoCancel(true)
        .build()

    // Show and immediately cancel the notification to set badge count
    notificationManager.notify(9999, notification)
    notificationManager.cancel(9999)
}

fun removeNotificationBadge(context: Context){
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(999)
}

fun openWebView(context: Context,url: String,title: String) {
    val intent = Intent(context, WebViewActivity::class.java)
    intent.putExtra("URL", url)
    intent.putExtra("title", title)
    context.startActivity(intent)
}

fun isValidPhoneNumber(phone: String): Boolean {
    val regex = Regex("^[+]?[0-9]{9,15}\$") // Accepts 10-15 digit numbers with optional '+'
    return phone.matches(regex)
}

fun getPaymentIdFromUrl(url: String): String? {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("payment_id") // Extract payment_id
}

fun getTimeZone(): String {
    return TimeZone.getDefault().id
}