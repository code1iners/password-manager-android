package com.example.passwordmanager.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import timber.log.Timber
import java.lang.Exception
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    private lateinit var context: Context

    // note. widgets
    private lateinit var loginActivityBody__input_submit_btn: Button
    private lateinit var loginActivityBody__input_option_btn_signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init context
        context = applicationContext
        // note. init toast
        toast = Toast(context)
        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. assignment
        loginActivityBody__input_submit_btn = findViewById(R.id.loginActivityBody__input_submit_btn)
        loginActivityBody__input_option_btn_signUp = findViewById(R.id.loginActivityBody__input_option_btn_signUp)

        // note. init listeners
        loginActivityBody__input_submit_btn.setOnClickListener(this)
        loginActivityBody__input_option_btn_signUp.setOnClickListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i( "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {

                // note. 2000 milliseconds = 2sec
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis()

                    toast = Toast.makeText(context, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
                    toast.show()
                    return true
                }

                // note. click again
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    val intent = Intent()
                    intent.putExtra(Protocol.COMMAND, Protocol.APP_TERMINATE)
                    setResult(RESULT_OK, intent)
                    finish()
                    toast.cancel()
                }
                return true
            }
        } catch (e: Exception) {e.printStackTrace()}

        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginActivityBody__input_submit_btn -> {
                Timber.w( "loginActivityBody__input_submit_btn_OnClick")
            }

            R.id.loginActivityBody__input_option_btn_signUp -> {
                Timber.w( "loginActivityBody__input_option_btn_signUp")

                val joinView = Intent(context, JoinActivity::class.java)
                startActivityForResult(joinView, Protocol.REQUEST_CODE_JOIN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode : $requestCode, resultCode : $resultCode, data : $data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Protocol.REQUEST_CODE_JOIN -> {
                    val intent = Intent()
                    intent.putExtra(Protocol.COMMAND, Protocol.REQUEST_CODE_JOIN)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}