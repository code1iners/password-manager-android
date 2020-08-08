package com.example.passwordmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import timber.log.Timber
import java.lang.Exception
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {
    private lateinit var activity: Activity
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    private lateinit var context: Context

    // note. widgets
    private lateinit var loginActivity__header_title: TextView
    private lateinit var loginActivity__body__input_pw_edit: EditText
    private lateinit var loginActivity__footer__btn1_signIn: Button
    private lateinit var loginActivity__footer__btn1_signUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init context
        activity = this
        context = applicationContext
        // note. init toast
        toast = Toast(context)
        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        loginActivity__header_title = findViewById(R.id.loginActivity__header_title)
        // note. assignment
        loginActivity__body__input_pw_edit = findViewById(R.id.loginActivity__body__input_pw_edit)
        loginActivity__footer__btn1_signIn = findViewById(R.id.loginActivity__footer__btn1_signIn)
        loginActivity__footer__btn1_signUp = findViewById(R.id.loginActivity__footer__btn1_signUp)

        // note. init listeners
        loginActivity__header_title.setOnClickListener(this)
        loginActivity__body__input_pw_edit.setOnEditorActionListener(this)
        loginActivity__footer__btn1_signIn.setOnClickListener(this)
        loginActivity__footer__btn1_signUp.setOnClickListener(this)
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
            R.id.loginActivity__header_title -> {
                PreferencesManager(activity, Protocol.ACCOUNT).check()
            }

            R.id.loginActivity__footer__btn1_signIn -> {
                Timber.w( "loginActivity__footer__btn1_signIn_OnClick")
                val pw = PreferencesManager(activity, Protocol.ACCOUNT)[Protocol.CLIENT_PW]
                if (!pw.isNullOrEmpty()) {
                    Timber.i("Password is exist! (pw:$pw)")
                    val inputPassword = loginActivity__body__input_pw_edit.text.toString()

                    if (pw == inputPassword) {
                        finish()
                    }
                } else {
                    Timber.w("Password is not exist!")
                }
            }

            R.id.loginActivity__footer__btn1_signUp -> {
                Timber.w( "loginActivity__footer__btn1_signUp")

                val joinView = Intent(context, JoinActivity::class.java)

                startActivityForResult(joinView, Protocol.REQUEST_CODE_JOIN_ACTIVITY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode : $requestCode, resultCode : $resultCode, data : $data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Protocol.REQUEST_CODE_JOIN_ACTIVITY -> {
                    val intent = Intent()
                    intent.putExtra(Protocol.COMMAND, Protocol.SIGN_UP_SUCCESS)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        when (v.id) {
            R.id.loginActivity__body__input_pw_edit -> {
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loginActivity__footer__btn1_signIn.performClick()
                    }
                }
                return true
            }
        }
        return false
    }
}