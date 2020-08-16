package com.example.passwordmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpers.GlideOptions
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.Protocol.ACCOUNT_DATA
import com.example.passwordmanager.Protocol.ACCOUNT_LIST
import com.example.passwordmanager.Protocol.CLIENT_PW
import com.example.passwordmanager.Protocol.USER_NICKNAME
import com.example.passwordmanager.Protocol.USER_PROFILE
import com.example.passwordmanager.Protocol.USER_THUMBNAIL
import com.example.passwordmanager.adapters.AccountAdapter
import com.example.passwordmanager.helpers.AccountInfoManager
import com.example.passwordmanager.models.AccountModel
import com.example.passwordmanager.ui.AccountAddActivity
import com.example.passwordmanager.ui.LoginActivity
import com.example.passwordmanager.ui.MyActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), View.OnClickListener, AccountAdapter.AccountClickListener {
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    private lateinit var context: Context
    private lateinit var backgrounds: Array<Drawable>
    var animDeleteLeft: Animation? = null

    // note. widgets
    // note. user info
    private lateinit var mainActivity__header__item_nickname: TextView

    // note. body
    private lateinit var mainActivity__body__list: RecyclerView
    private lateinit var mainActivity__footer__add_btn: Button
    private lateinit var mainActivity__header__item_mySetting: ImageButton

    // note. item list
    private lateinit var accounts: ArrayList<AccountModel>
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // note. init
        init()
        // note. check has arguments
        checkArgs()
        // note. if no has user info, showing login view
        if (!isUser) displayLoginView()
        // note. apply info
        applyView()
        // note. get user account list
        refreshAccountList()
    }

    private fun applyView() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        // note. user nickname
        mainActivity__header__item_nickname.text = clientNickname
        // note. use thumbnail
        Glide.with(context).load(clientThumbnail).apply(GlideOptions.centerCropAndCircleCrop()).into(mainActivity__header__item_mySetting)
    }

    private fun refreshAccountList() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val storedList: ArrayList<AccountModel>? = AccountInfoManager.read(activity)
            accounts.clear()
            if (!storedList.isNullOrEmpty()) {
                for ((idx, model) in storedList.withIndex()) {
                    accounts.add(model)
                }
            }
            accountAdapter.notifyDataSetChanged()
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun checkArgs() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        val manager = PreferencesManager(activity, Protocol.USER_PROFILE)
        clientPw = manager[CLIENT_PW]
        clientNickname = manager[USER_NICKNAME]
        clientThumbnail = manager[USER_THUMBNAIL]

        Timber.i("clientPw:$clientPw, clientNickname:$clientNickname, clientThumbnail:$clientThumbnail")
        if (clientThumbnail.isNullOrEmpty()) Snackbar.make(mainActivity__container, "프로필 이미지 파일을 찾을 수 없습니다.", Snackbar.LENGTH_LONG).show()
        if (clientPw == null) isUser = false
        Timber.i("isUser:$isUser")
    }

    private fun displayLoginView() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        val loginView = Intent(context, LoginActivity::class.java)
        startActivityForResult(loginView, Protocol.REQUEST_CODE_LOGIN_ACTIVITY)
    }

    private fun init() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init libraries
        initLibraries()
        // note. init etc..
        initEtc()
        // note. init widgets
        initWidgets()
        // note. init adapters
        initAdapters()
        // note. init animations
        initAnimations()
    }

    private fun initAnimations() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            animDeleteLeft = AnimationUtils.loadAnimation(context, R.anim.move_left_001)
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initLibraries() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        // note. timber
        initLibrariesTimber()
        initLibrariesThreeTen()
    }

    private fun initLibrariesTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initLibrariesThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. header
        mainActivity__header__item_mySetting = findViewById(R.id.mainActivity__header__item_mySetting)
        mainActivity__header__item_nickname = findViewById(R.id.mainActivity__header__item_nickname)
        // note. body
        mainActivity__body__list = findViewById(R.id.mainActivity__body__list)
        // note. footer
        mainActivity__footer__add_btn = findViewById(R.id.mainActivity__footer__add_btn)

        // note. set listeners
        // note. header
        mainActivity__header__item_mySetting.setOnClickListener(this)
        // note. footer
        mainActivity__footer__add_btn.setOnClickListener(this)
    }

    private fun initAdapters() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        accounts = ArrayList()
        Timber.i("accounts:$accounts")
        accountAdapter = AccountAdapter()
        accountAdapter.setContext(context)
        accountAdapter.setBackgrounds(backgrounds)
        Timber.i("accountAdapter:$accountAdapter")
        accountAdapter.setList(accounts)
        accountAdapter.setAccountClickListener(this)

        // note. init layout manager
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        // note set recyclerview
        mainActivity__body__list.layoutManager = linearLayoutManager
        mainActivity__body__list.adapter = accountAdapter
    }

    private fun initEtc() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. activity
        activity = this
        // note. context
        context = applicationContext
        // note. toast
        toast = Toast(context)
        // note. account item backgrounds
        backgrounds = arrayOf(
            context.resources.getDrawable(R.drawable.gradation_item01),
            context.resources.getDrawable(R.drawable.gradation_item02),
            context.resources.getDrawable(R.drawable.gradation_item03),
            context.resources.getDrawable(R.drawable.gradation_item04),
            context.resources.getDrawable(R.drawable.gradation_item05),
            context.resources.getDrawable(R.drawable.gradation_item06),
            context.resources.getDrawable(R.drawable.gradation_item07),
            context.resources.getDrawable(R.drawable.gradation_item08),
            context.resources.getDrawable(R.drawable.gradation_item09),
            context.resources.getDrawable(R.drawable.gradation_item10)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode:$requestCode, resultCode:$resultCode, data:$data")
        try {
            if (resultCode == RESULT_OK) {
                when (requestCode) {
                    Protocol.REQUEST_CODE_LOGIN_ACTIVITY -> {
                        val command = data?.getStringExtra(Protocol.COMMAND)
                        Timber.i("command:$command")

                        // note. terminate application
                        when (command) {
                            Protocol.APP_TERMINATE -> {finish()}
                        }

                    }

                    Protocol.REQUEST_CODE_JOIN_ACTIVITY -> {

                    }

                    Protocol.REQUEST_CODE_MY_ACTIVITY -> {
                        val command = data?.getStringExtra(Protocol.COMMAND)
                        when (command) {
                            Protocol.SIGN_OUT -> {
                                val mainActivity = activity

                                val userData = PreferencesManager(mainActivity, USER_PROFILE)
                                userData.remove(USER_NICKNAME)
                                userData.remove(CLIENT_PW)

                                val accountData = PreferencesManager(mainActivity, ACCOUNT_DATA)
                                accountData.remove(ACCOUNT_LIST)

                                // note. update(init) text
                                mainActivity__header__item_nickname.text = Protocol.EMPTY

                                displayLoginView()
                            }

                            Protocol.SUCCESS -> {
                                val command = data.getStringExtra(Protocol.COMMAND)
                                when (command) {
                                    Protocol.SUCCESS -> {
                                        val nickname = data.getStringExtra(USER_NICKNAME)
                                        val thumbnail = data.getStringExtra(USER_THUMBNAIL)
                                        if (!nickname.isNullOrEmpty()) {
                                            // note. set nickname
                                            mainActivity__header__item_nickname.text = nickname
                                            clientNickname = nickname
                                        }

                                        if (!thumbnail.isNullOrEmpty()) {
                                            // note. set thumbnail
                                            Glide.with(context).load(thumbnail).apply(GlideOptions.centerCropAndCircleCrop()).into(mainActivity__header__item_mySetting)
                                            clientThumbnail = thumbnail
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Protocol.REQUEST_CODE_ADD_ACCOUNT -> {
                        val command = data?.getStringExtra(Protocol.COMMAND)
                        Timber.i("command:$command")
                        when (command) {
                            Protocol.SUCCESS -> {
                                try {
                                    refreshAccountList()
                                } catch (e: Exception) {e.printStackTrace()}
                            }

                            else -> {
                                refreshAccountList()
                            }
                        }
                    }

                    Protocol.REQUEST_CODE_EXIT -> finish()
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i( "keyCode:$keyCode, event:$event, repeatCount:${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {

                // note. 2000 milliseconds = 2sec
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis()

                    toast = Toast.makeText(context, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
                    toast.show()
                    return false
                }
                // note. click again
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    exitProcess(0)
                    toast.cancel()
                }

                return true
            }
        } catch (e: Exception) {e.printStackTrace()}

        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        when (v.id) {
            R.id.mainActivity__header__item_mySetting -> {
                val myActivity = Intent(context, MyActivity::class.java)
                startActivityForResult(myActivity, Protocol.REQUEST_CODE_MY_ACTIVITY)
            }

            R.id.mainActivity__footer__add_btn -> {
                Timber.w("mainActivity__footer__add_btn_OnClick")
                val accountAddActivity = Intent(context, AccountAddActivity::class.java)
                accountAddActivity.putExtra(Protocol.ACCESS_KIND, Protocol.CREATE)
                startActivityForResult(accountAddActivity, Protocol.REQUEST_CODE_ADD_ACCOUNT)
            }
        }
    }

    override fun accountUpdate(p: Int, m: AccountModel) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i("p:$p, m:${m.title}")
            val accountAddActivity = Intent(context, AccountAddActivity::class.java)
            accountAddActivity.putExtra(Protocol.ACCESS_KIND, Protocol.MODIFY)
            accountAddActivity.putExtra(Protocol.POSITION, p)
            accountAddActivity.putExtra(Protocol.ACCOUNT_MODEL, Gson().toJson(m))
            startActivityForResult(accountAddActivity, Protocol.REQUEST_CODE_ADD_ACCOUNT)
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun accountDelete(h: AccountAdapter.CustomViewHolder, p: Int, m: AccountModel) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i("p:$p, m:${m.title}")
            runOnUiThread {
                h.accountListItem__container.startAnimation(animDeleteLeft)
                Handler().postDelayed({
                    // note. delete in view
                    accounts.remove(m)
                    // note. delete in device
                    AccountInfoManager.delete(activity, p, m)
                    accountAdapter.notifyDataSetChanged()
                }, animDeleteLeft!!.duration)
            }

        } catch (e: Exception) {e.printStackTrace()}
    }

    companion object {
        lateinit var activity: Activity
        var clientPw: String? = null
        var clientNickname: String? = null
        var clientThumbnail: String? = null
        var isUser: Boolean = true
    }
}