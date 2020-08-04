package com.example.passwordmanager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.lang.Exception

class MyActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {
    // note. widgets
    lateinit var myActivity__header__status_back: ImageButton
    lateinit var myActivity__header__username: TextView
    lateinit var myActivity__body__nickname_edit: EditText
    lateinit var myActivity__footer__btn_save: Button
    lateinit var myActivity__footer__btn_signOut: Button
    // note. notifications
    lateinit var snack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        init()
        applyView()
    }

    private fun applyView() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        val savedName: String? = PreferencesManager(MainActivity.activity, Protocol.ACCOUNT).get(Protocol.NICKNAME)
        Timber.i( "savedName : $savedName")
        if (savedName != null) {
            // note. apply username
            myActivity__header__username.text = savedName
            // note. apply edit
            myActivity__body__nickname_edit.setText(savedName)
        }
    }

    private fun init() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        myActivity__header__status_back = findViewById(R.id.myActivity__header__status_back)
        myActivity__header__username = findViewById(R.id.myActivity__header__username)
        myActivity__body__nickname_edit = findViewById(R.id.myActivity__body__nickname_edit)
        myActivity__footer__btn_save = findViewById(R.id.myActivity__footer__btn_save)
        myActivity__footer__btn_signOut = findViewById(R.id.myActivity__footer__btn_signOut)

        // note. set listeners
        myActivity__header__status_back.setOnClickListener(this)
        myActivity__footer__btn_signOut.setOnClickListener(this)
        myActivity__footer__btn_save.setOnClickListener(this)

        myActivity__body__nickname_edit.setOnEditorActionListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i( "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {

//                val myActivityResult = Intent()
//                myActivityResult.putExtra(Protocol.INTENT_RESULT, Protocol.MY_ACTIVITY)
//                setResult(RESULT_OK, myActivityResult)
                finish()

                return true
            }
        } catch (e: Exception) {e.printStackTrace()}

        return super.onKeyDown(keyCode, event)
    }



    override fun onClick(v: View) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        when (v.id) {
            R.id.myActivity__header__status_back -> {
                finish()
            }

            R.id.myActivity__footer__btn_signOut -> {

                val myActivityResult = Intent()
                setResult(RESULT_OK, myActivityResult)
                myActivityResult.putExtra(Protocol.COMMAND, Protocol.SIGN_OUT)
                finish()
            }

            R.id.myActivity__footer__btn_save -> {
                // note. declared and assignment in var
                val nickname = myActivity__body__nickname_edit.text.toString()
                // note. apply nickname field
                myActivity__header__username.text = nickname
                // note. stored nickname user's device
                PreferencesManager(MainActivity.activity, Protocol.ACCOUNT).add(Protocol.NICKNAME, nickname)
                // note. show snack bar
                snack = Snackbar.make(findViewById(R.id.myActivity__container),
                    R.string.nickname_saved, Snackbar.LENGTH_LONG)
                snack.show()
                snack.setAction(R.string.confirm) { snack.dismiss() }

            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("v : ${v.id}, actionId : $actionId, event : $event")
        try {
            when (v.id) {
                R.id.myActivity__body__nickname_edit -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) myActivity__footer__btn_save.performClick()
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
        return false
    }
}