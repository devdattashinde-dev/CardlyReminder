package com.cardleyreminder.apps.ui.notification

import android.app.Dialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.NotificationAdapter
import com.cardleyreminder.apps.data.listner.PaginationScrollListener
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.notification.ClearNotificationRequest
import com.cardleyreminder.apps.data.model.notification.Notification
import com.cardleyreminder.apps.data.network.NotificationApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.NotificationRepository
import com.cardleyreminder.apps.databinding.ActivityNotificationBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.BadgeHelper
import com.cardleyreminder.apps.util.BadgeUtility
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.removeNotificationBadge
import com.cardleyreminder.apps.util.setNotificationBadge

class NotificationActivity : BaseActivity<NotificationViewModel, ActivityNotificationBinding, NotificationRepository>() {


    private lateinit var notificationAdapter: NotificationAdapter
    var pagination = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog,null)
        progressBar = Dialog(this).also {
            it.setContentView(dialogView)
            try {
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }


        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })


        notificationAdapter = NotificationAdapter(this)
        viewModel.notificationResponse.observe(
            this,
            Observer {
                isLoading = false
                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            viewModel.notificationCount.value = it.value.notificaitonCount

                            isLastPage = it.value.data.size<10

                            if(pagination==1){
                                notificationAdapter.setNotificationList(it.value.data)
                            }else
                                notificationAdapter.addNotificationList(it.value.data)

                            binding.tvClearAll.visibility = if(notificationAdapter.list.size>0) View.VISIBLE else View.GONE


                        }else {
                            binding.tvClearAll.visibility = View.GONE
                            isLastPage = true
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                //binding.swipeRefresh.isRefreshing=false
            })

        viewModel.clearNotificationResponse.observe(
            this,
            Observer {
                isLoading = false
                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            viewModel.notificationCount.value = it.value.notificaitonCount
                            //ainbinding.root.toast(it.value.message!!)
                            progressBar.show()
                            viewModel.getNotificationData()
                        }else {

                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                //binding.swipeRefresh.isRefreshing=false
            })

        viewModel.readNotificationResponse.observe(
            this,
            Observer {
                isLoading = false
                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            viewModel.notificationCount.value = it.value.notificaitonCount
                            //binding.root.toast(it.value.message!!)
                            notificationAdapter.setMessageRead(viewModel.selectedNotificationIndex)
                        }else {

                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                //binding.swipeRefresh.isRefreshing=false
            })

        viewModel.notificationCount.observe(
            this,
            Observer {

                if(it>0){
                    binding.tvNotficationCount.visibility = View.VISIBLE
                    binding.tvNotficationCount.text = it.toString()
                    setNotificationBadge(this,it)
                    BadgeUtility.setBadge(this,it)
                }else{
                    binding.tvNotficationCount.visibility = View.GONE
                    removeNotificationBadge(this)
                    BadgeHelper.clearBadge(this);
                }
            })

        //binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE

        progressBar.show()
        viewModel.getNotificationData()

        /*binding.swipeRefresh.setOnRefreshListener {
            viewModel.getNotificationData(NotificationListRequest(pagination.toString()))
        }*/

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        binding.rvNotification.layoutManager = layoutManager
        binding.rvNotification.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                /*isLoading = true
                pagination++
                viewModel.getNotificationData(NotificationListRequest(pagination.toString()))*/
            }
        })

        binding.rvNotification.adapter =notificationAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            private val deleteIcon: Drawable? = ContextCompat.getDrawable(this@NotificationActivity, R.drawable.ic_delete)
            private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
            private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
            private val background = ColorDrawable()
            private val backgroundColor = Color.parseColor("#FF0000")
            private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                notificationAdapter.removeItem(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCancelled = dX == 0f && !isCurrentlyActive

                if (isCancelled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                // Draw red background
                background.color = backgroundColor
                if (dX > 0) { // Swiping to the right
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                } else { // Swiping to the left
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }
                background.draw(c)

                // Calculate position for delete icon
                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val iconBottom = iconTop + intrinsicHeight

                if (dX > 0) { // Swiping to the right
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = itemView.left + iconMargin + intrinsicWidth
                    deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else if (dX < 0) { // Swiping to the left
                    val iconLeft = itemView.right - iconMargin - intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                }

                // Draw delete icon
                deleteIcon?.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
                c.drawRect(left, top, right, bottom, clearPaint)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvNotification)

        notificationAdapter.onItemRemoved={notification ->
            progressBar.show()
//            if(notification.isSeen !="1"){
//                viewModel.notificationCount.value = viewModel.notificationCount.value!! - 1
//            }

            viewModel.removeNotification(

                ClearNotificationRequest(
                    notiId = notification.notiId!!
                )
            )

        }

        notificationAdapter.onItemClick={index,notification ->
            viewModel.selectedNotificationIndex = index
            showNotificationDialog(notification)
            if(notification.isSeen !="1"){
                //viewModel.notificationCount.value = viewModel.notificationCount.value!! - 1
                viewModel.readNotification(
                    ClearNotificationRequest(
                        notiId = notification.notiId!!
                    )
                )
            }

        }



        binding.tvClearAll.setOnClickListener {

            if(notificationAdapter.list.size>0){
                progressBar.show()
                viewModel.removeNotification(
                    ClearNotificationRequest(
                        notiId = "all"
                    )
                )
            }

        }

    }


    private fun showNotificationDialog(notification: Notification) {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.notification_dialog_layout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView
        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val tvMessage = dialog.findViewById(R.id.tvMessage) as TextView



        //tvMessage.text = notification.message
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.text = Html.fromHtml(notification.title?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"), Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvTitle.text = Html.fromHtml(notification.title?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvMessage.text = Html.fromHtml(notification.message?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"), Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvMessage.text = Html.fromHtml(notification.message?.replace("&#9888;","<font color='#FF0000'>&#9888;</font>"))
        }

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getViewModel() = NotificationViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityNotificationBinding.inflate(inflater)

    override fun getActivityRepository() = NotificationRepository(
    remoteDataSource.buildApi(NotificationApi::class.java,pref.token))
}