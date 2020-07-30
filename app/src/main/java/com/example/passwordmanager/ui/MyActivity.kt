package com.example.passwordmanager.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import java.lang.Exception

class MyActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        val TAG = MyActivity::class.simpleName
    }

    // note. widgets
    lateinit var myActivity__header__username: TextView
    lateinit var myActivity__body__nickname_edit: EditText
    lateinit var myActivity__footer__btn_save: Button
    lateinit var myActivity__footer__btn_signOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        init()
    }

    private fun init() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        myActivity__header__username = findViewById(R.id.myActivity__header__username)
        myActivity__body__nickname_edit = findViewById(R.id.myActivity__body__nickname_edit)
        myActivity__footer__btn_save = findViewById(R.id.myActivity__footer__btn_save)
        myActivity__footer__btn_signOut = findViewById(R.id.myActivity__footer__btn_signOut)

        // note. set listeners
        myActivity__footer__btn_signOut.setOnClickListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Log.i(LoginActivity.TAG, "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
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
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        when (v.id) {
            R.id.myActivity__footer__btn_signOut -> {

                val myActivityResult = Intent()
                setResult(RESULT_OK, myActivityResult)
                myActivityResult.putExtra(Protocol.COMMAND, Protocol.SIGN_OUT)
                finish()
            }
        }
    }
}