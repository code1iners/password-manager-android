package com.example.passwordmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.ui.LoginActivity
import java.lang.Exception
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
        lateinit var activity: Activity
        var clientId: String? = null
        var clientPw: String? = null
        var isUser: Boolean = false
    }


    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        checkArgs()
        if (!isUser) displayLoginView()
    }

    private fun checkArgs() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        val manager = PreferencesManager(activity, Protocol.ACCOUNT)
        clientId = manager[Protocol.CLIENT_ID]
        clientPw = manager.get(Protocol.CLIENT_PW)

        Log.i(TAG, "clientId : $clientId, clientPw : $clientPw")
        if (!clientId.isNullOrEmpty() || !clientPw.isNullOrEmpty()) isUser = true
    }

    private fun displayLoginView() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        val loginView = Intent(context, LoginActivity::class.java)
        startActivityForResult(loginView, Protocol.REQUEST_CODE_LOGIN)
    }

    private fun init() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init etc..
        initEtc()
    }

    private fun initEtc() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. activity
        activity = this
        // note. context
        context = applicationContext
        // note. toast
        toast = Toast(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        Log.i(TAG, "requestCode : $requestCode, resultCode : $resultCode, data : $data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Protocol.REQUEST_CODE_LOGIN -> {

                }

                Protocol.REQUEST_CODE_JOIN -> {

                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Log.i(LoginActivity.TAG, "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.getRepeatCount() == 0) {

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
}