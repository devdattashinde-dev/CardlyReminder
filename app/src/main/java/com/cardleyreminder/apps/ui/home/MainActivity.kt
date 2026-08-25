package com.cardleyreminder.apps.ui.home


import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.BuildConfig
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.CardListAdapter
import com.cardleyreminder.apps.data.listner.PaginationScrollListener
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.home.HomeData
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.network.HomeApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.HomeRepository
import com.cardleyreminder.apps.databinding.ActivityMainBinding
import com.cardleyreminder.apps.ui.addCard.AddCardActivity
import com.cardleyreminder.apps.ui.addCard.EditCardActivity
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.ui.calender.CalenderActivity
import com.cardleyreminder.apps.ui.insights.CreditCardFactsActivity
import com.cardleyreminder.apps.ui.insights.CreditCardTipsActivity
import com.cardleyreminder.apps.ui.insights.FaqActivity
import com.cardleyreminder.apps.ui.insights.VideoActivity
import com.cardleyreminder.apps.ui.issuer.IssuerCategoriesActivity
import com.cardleyreminder.apps.ui.issuer.MenuCardOnNetworkActivity
import com.cardleyreminder.apps.ui.notification.NotificationActivity
import com.cardleyreminder.apps.ui.settings.RateCardlyActivity
import com.cardleyreminder.apps.ui.settings.SettingsActivity
import com.cardleyreminder.apps.ui.subscription.PaymentHistoryActivity
import com.cardleyreminder.apps.ui.subscription.SubscriptionActivity
import com.cardleyreminder.apps.ui.subscription.SubscriptionDetailsActivity
import com.cardleyreminder.apps.util.BadgeHelper
import com.cardleyreminder.apps.util.BadgeUtility
import com.cardleyreminder.apps.util.STATUS_ACTIVE
import com.cardleyreminder.apps.util.STATUS_EXPIRED
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.removeNotificationBadge
import com.cardleyreminder.apps.util.setNotificationBadge
import com.cardleyreminder.apps.util.startNewStackActivity
import com.cardleyreminder.apps.util.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import androidx.core.net.toUri
import com.cardleyreminder.apps.data.model.home.HomeResponse
import com.cardleyreminder.apps.util.STATUS_NEW
import com.cardleyreminder.apps.util.STATUS_TRIAL


class MainActivity() : BaseActivity<HomeViewModel, ActivityMainBinding, HomeRepository>() {

    var updateDialog: AlertDialog? = null
    var pagination = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    lateinit var cardListAdapter: CardListAdapter;

    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {

        }
    }

    private fun isActiveUser(): Boolean {
        return when (viewModel.subStatus) {
            STATUS_NEW -> {
                showSubscriptionDiloag()
                false
            }
            STATUS_ACTIVE -> true
            STATUS_EXPIRED -> {
                binding.root.toast("Your subscription has expired. Please renew to unlock all the amazing features!")
                false
            }
            else -> {
                binding.root.toast("You're using the free version. Subscribe to unlock all the amazing features!")
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/

        //setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_content_main)

        //navView.setupWithNavController(navController)

        viewModel.isFromRegister = intent.getBooleanExtra("isFromRegister",false)

        binding.appBarMain.icAddCard.setOnClickListener {
            closeDrawer();
            gotoAddCart()
        }
        binding.appBarMain.ivNotification.setOnClickListener {
            startNewStackActivity(NotificationActivity::class.java)
        }


        binding.appBarMain.icSortBy.setOnClickListener {
            closeDrawer();
            var db : Dialog? = Dialog(this)
            db?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            db?.setCancelable(false)
            db?.setContentView(R.layout.sortby_dialog_layout)

            val sortByClosingDateAsc = db?.findViewById<LinearLayout>(R.id.sortByClosingDateAsc)
            val sortByClosingDateDsc = db?.findViewById<LinearLayout>(R.id.sortByClosingDateDsc)
            val sortByDueDateAsc = db?.findViewById<LinearLayout>(R.id.sortByDueDateAsc)
            val sortByDueDateDsc = db?.findViewById<LinearLayout>(R.id.sortByDueDateDsc)
            val sortByValidThruAsc = db?.findViewById<LinearLayout>(R.id.sortByValidThruAsc)
            val sortByValidThruDsc = db?.findViewById<LinearLayout>(R.id.sortByValidThruDsc)
            val sortByCreatedTimeAsc = db?.findViewById<LinearLayout>(R.id.sortByCreatedTimeAsc)
            val sortByCreatedTimeDsc = db?.findViewById<LinearLayout>(R.id.sortByCreatedTimeDsc)

            sortByClosingDateAsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination,statementClosingDate = "asc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })
            sortByClosingDateDsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination,statementClosingDate = "dsc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })

            sortByDueDateAsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination, paymentDueDate = "asc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })
            sortByDueDateDsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination,paymentDueDate = "dsc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })

            sortByValidThruAsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination, validThru = "asc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })
            sortByValidThruDsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                viewModel.getHomeData(pagination,validThru = "dsc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })

            sortByCreatedTimeAsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination, createdTime = "asc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })
            sortByCreatedTimeDsc?.setOnClickListener(View.OnClickListener {
                pagination = 1
                progressBar.show()
                viewModel.getHomeData(pagination,createdTime = "dsc", fcmToken = pref.fcmToken!!)
                db.cancel()
            })

            /*db = Dialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dark).setView(mDialogView).setTitle("")
                .show()*/
            db?.show()

            val window: Window? = db?.getWindow()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(R.color.gray2);

        }


        binding.menuHome.setOnClickListener {

            closeDrawer();
        }
        binding.menuCalender.setOnClickListener {
            if(isActiveUser()) {
                closeDrawer();
                startActivity(Intent(this, CalenderActivity::class.java))
            }
        }
        binding.menuIssuers.setOnClickListener {
            if(isActiveUser()) {
                closeDrawer();
                val intent = Intent(this, IssuerCategoriesActivity::class.java)
                intent.putExtra("isCardNetwork",false)
                startActivity(intent)
            }

        }
        binding.menuCardNetwork.setOnClickListener {
            if(isActiveUser()) {
                closeDrawer();
                startActivity(Intent(this, MenuCardOnNetworkActivity::class.java))
            }

        }
        binding.menuInsightsHeader.setOnClickListener {

            if(binding.menuInsightsBody.visibility == View.GONE){
                binding.menuInsightsBody.visibility = View.VISIBLE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_up,null))


            }else{
                binding.menuInsightsBody.visibility = View.GONE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))
            }

        }



        binding.menuCreditCardFacts.setOnClickListener{
            if(isActiveUser()) {
                binding.menuInsightsBody.visibility = View.GONE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))

                startNewStackActivity(CreditCardFactsActivity::class.java)
                closeDrawer()
            }

        }

        binding.menuCreditCardTips.setOnClickListener{
            if(isActiveUser()) {
                binding.menuInsightsBody.visibility = View.GONE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))
                startNewStackActivity(CreditCardTipsActivity::class.java)
                closeDrawer()
            }
        }

        binding.menuRateCardly.setOnClickListener{
            startNewStackActivity(RateCardlyActivity::class.java)
            closeDrawer()
        }

        binding.menuFaq.setOnClickListener{

            if(viewModel.subStatus==STATUS_NEW || viewModel.subStatus==STATUS_EXPIRED){
                showSubscriptionDiloag()
            }else{
                binding.menuInsightsBody.visibility = View.GONE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))

                startNewStackActivity(FaqActivity::class.java)
            }

            closeDrawer()
        }

        binding.menuEducationalVideo.setOnClickListener{
            if(isActiveUser()) {
                binding.menuInsightsBody.visibility = View.GONE
                binding.ivInsightsArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))

                startNewStackActivity(VideoActivity::class.java)
                closeDrawer()
            }
        }


        binding.menuSubscriptionHeader.setOnClickListener {

            if(binding.menuSubscriptionBody.visibility == View.GONE){
                binding.menuSubscriptionBody.visibility = View.VISIBLE
                binding.ivSubscriptionArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_up,null))


            }else{
                binding.menuSubscriptionBody.visibility = View.GONE
                binding.ivSubscriptionArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))
            }

        }

        binding.menuSubscription.setOnClickListener{
            if(viewModel.subStatus == STATUS_ACTIVE || viewModel.subStatus == STATUS_TRIAL) {
                binding.menuSubscriptionBody.visibility = View.GONE
                binding.ivSubscriptionArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))

                val intent = Intent(this, SubscriptionDetailsActivity::class.java)
                intent.putExtra("paymentID", viewModel.activePlanId!!)
                startActivity(intent)
            }else {
//                startNewStackActivity(SubscriptionActivity::class.java)
                val intent = Intent(this, SubscriptionActivity::class.java)
                intent.putExtra("showToggle", viewModel.subStatus==0)

                startActivity(intent)
            }
            closeDrawer()
        }

        binding.menuPaymentHistory.setOnClickListener{

            binding.menuSubscriptionBody.visibility = View.GONE
            binding.ivSubscriptionArrow.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_arrow_doun,null))

            startNewStackActivity(PaymentHistoryActivity::class.java)
            closeDrawer()
        }


        /*binding.menuDataBackup.setOnClickListener {
            //navController.navigate(R.id.menuDataBackup)
            closeDrawer();
        }*/

        binding.menuLogout.setOnClickListener {
            closeDrawer();
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                }
                .setNegativeButton("Cancel", null) // Dismiss the dialog
                .show()


        }


        binding.menuSettings.setOnClickListener {
            /*navController.navigate(R.id.menuSettings)*/
            closeDrawer();

            startActivity(Intent(this,SettingsActivity::class.java))

            //binding.appBarMain.appBar.visibility= View.GONE
        }
        askNotificationPermission()

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
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            viewModel.updateToken(UpdateFCMTokenRequest(token))
        })*/

        cardListAdapter = CardListAdapter(this,binding.appBarMain.tvNoRecords)

        cardListAdapter.onItemClick = { card, isDelete ->

            if(isDelete){
                showDeleteCardDialog(card)
            }else{
                val intent = Intent(this, EditCardActivity::class.java)
                intent.putExtra("cardId",card?.cardId)
                startActivity(intent)
            }
        }


        viewModel.homeResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        progressBar.cancel()
                        if(it.value.status) {
                            if(!it.value.subsErrorMessage.isNullOrEmpty()){
                                viewModel.subsErrorMessage = it.value.subsErrorMessage!!
                                if(!it.value.subStatus.isNullOrEmpty())
                                    viewModel.subStatus = it.value.subStatus!!.toInt()

                                showSubscriptionDiloag()

                            }else{
                                viewModel.activePlanId =it.value.activePlanId
                                if(it.value.data.size>0) {
                                    if(pagination==1)
                                        cardListAdapter.clearData()

                                    cardListAdapter.setData(it.value.data)


                                }else if(pagination==1){
                                    cardListAdapter.clearData()

                                }

                                if(!it.value.notificaitonCount.isNullOrEmpty()){
                                    binding.appBarMain.tvNotficationCount.text = it.value.notificaitonCount
                                    setNotificationBadge(this,it.value.notificaitonCount!!.toInt())
                                    BadgeUtility.setBadge(this,it.value.notificaitonCount!!.toInt())
                                }else{
                                    binding.appBarMain.tvNotficationCount.text = it.value.notificaitonCount
                                    removeNotificationBadge(this)
                                    BadgeHelper.clearBadge(this);
                                }

                                if(!it.value.subStatus.isNullOrEmpty())
                                    viewModel.subStatus = it.value.subStatus!!.toInt()
                                if( BuildConfig.VERSION_CODE < it.value.version!!.toInt()){
                                    showUpdateDialog()
                                }
                            }

                            if(viewModel.isFromRegister && !viewModel.isFirstVideoShown){
                                viewModel.isFirstVideoShown = true
                                showVideoDialog(it.value.firstVideo!!,true)

                            }

                            if(!it.value.marketVideo.isNullOrEmpty()){

                                binding.appBarMain.icVideo.setOnClickListener {_->
                                    showVideoDialog(it.value.marketVideo!!,false)
                                }

                            }




                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                binding.appBarMain.swipeRefresh.isRefreshing=false
            })

        viewModel.logoutResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        progressBar.cancel()
                        logout()
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                binding.appBarMain.swipeRefresh.isRefreshing=false
            })

        viewModel.deleteAccountResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        progressBar.cancel()
                        logout()
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                binding.appBarMain.swipeRefresh.isRefreshing=false
            })

        viewModel.deleteCardResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        progressBar.hide()
                        if(it.value.status) {
                            progressBar.show()
                            pagination=1
                            viewModel.getHomeData(pagination, fcmToken = pref.fcmToken!!)
                        }

                        binding.root.toast(it.value.message!!)
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
                binding.appBarMain.swipeRefresh.isRefreshing=false
            })

        binding.appBarMain.swipeRefresh.setOnRefreshListener {
            pagination=1
            viewModel.getHomeData(pagination, fcmToken = pref.fcmToken!!)
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        binding.appBarMain.rvNotification.layoutManager = layoutManager
        binding.appBarMain.rvNotification.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                pagination++
                //viewModel.getHomeData(NotificationListRequest(pagination.toString()))
            }
        })

        binding.appBarMain.rvNotification.adapter =cardListAdapter

        binding.appBarMain.btAddNewCard.setOnClickListener(View.OnClickListener {
            gotoAddCart()
        })

        binding.menuDeleteAccount.setOnClickListener {
            closeDrawer();
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteAccount()
                }
                .setNegativeButton("Cancel", null) // Dismiss the dialog
                .show()


        }


    }

    private fun showVideoDialog(videoUrl: String, isFirstVideo: Boolean) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_video) // Your custom layout with VideoView
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val videoView = dialog.findViewById<VideoView>(R.id.videoView)
        val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar) // optional loading indicator
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose) // optional loading indicator

        if (isFirstVideo) {
            dialog.setCancelable(false) // disables back press
            dialog.setCanceledOnTouchOutside(false) // disables outside touch


        }else{
            ivClose.visibility = View.VISIBLE
        }

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(videoUrl))

        videoView.setOnPreparedListener { mp ->
            progressBar?.visibility = View.GONE
            mp.start()
        }

        videoView.setOnCompletionListener {
            if (isFirstVideo) {
                dialog.dismiss() // auto close only for first video
            }
        }

        ivClose.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()


    }


    private fun showSubscriptionDiloag() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.subscription_error_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val btOk = dialog.findViewById(R.id.btSubscribe) as ConstraintLayout
        val tvMessage = dialog.findViewById(R.id.tvMessage) as TextView

        tvMessage.text = viewModel.subsErrorMessage

        btOk.setOnClickListener {

            val intent = Intent(this, SubscriptionActivity::class.java)
            intent.putExtra("showToggle", viewModel.subStatus==0)

            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Please update to continue.")

        builder.setPositiveButton("Update Now") { dialog, _ ->
            // Replace with your Play Store URL or direct download link
            val appPackageName = packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                    "market://details?id=$appPackageName".toUri()))
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$appPackageName".toUri()))
            }
            dialog.dismiss()
        }

        builder.setCancelable(false)
        updateDialog = builder.show()
    }

    override fun onResume() {
        super.onResume()

        if(updateDialog==null || !updateDialog!!.isShowing){

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Main Activity", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                viewModel.fcmToken = token
                pref.saveFcmToken(token)

                progressBar.show()
                pagination=1
                viewModel.getHomeData(pagination, fcmToken = pref.fcmToken!!)
            })

        }

    }
    private fun gotoAddCart() {
        if(viewModel.subStatus==STATUS_NEW || viewModel.subStatus==STATUS_EXPIRED){
            showSubscriptionDiloag()
        }else if(cardListAdapter.cards.size==0 || isActiveUser()){
            startActivity(Intent(this, AddCardActivity::class.java))
        }

    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START, true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.appBarMain.icMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        /*binding.appBarMain.icProfile.setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(MobileNavigationDirections.actionGlobalMenuMyProfile())
        }

        binding.appBarMain.ivNotification.setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).navigate(MobileNavigationDirections.actionGlobalNotificationFragment())
        }

        binding.appBarMain.ivHome.setOnClickListener {
            findNavController(R.id.nav_host_fragment_content_main).popBackStack(R.id.menuDashboard,false)
        }

        binding.appBarMain.ivBottomLogout.setOnClickListener {
            logout()
        }

        navView.getHeaderView(0).findViewById<ImageView>(R.id.icCloseDrawer).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }*/

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        /*if(item.itemId == R.id.menuLogout){
            logout(navView)
        }*/

        return super.onOptionsItemSelected(item)
    }

    /*private fun logout() {
        navView.alertDialog(getString(R.string.logout_message), {
            lifecycleScope.launch {
                var pref = UserPreferences(this@MainActivity)
                pref.clear()
                startNewActivity(AuthActivity::class.java)
            }
        },"Logout")
    }*/
    private fun showDeleteCardDialog(card: HomeData?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_delete_confirmation_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val btOk = dialog.findViewById(R.id.btOk) as TextView
        val btCancel = dialog.findViewById(R.id.btCancel) as TextView


        btOk.setOnClickListener {
            progressBar.show()
            viewModel.deleteCard(
                GetCardDetailsRequest(
                    cardId = card?.cardId!!
                )
            )

            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun getViewModel() = HomeViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityMainBinding.inflate(inflater)

    override fun getActivityRepository() = HomeRepository(
        remoteDataSource.buildApi(HomeApi::class.java,pref.token))
}