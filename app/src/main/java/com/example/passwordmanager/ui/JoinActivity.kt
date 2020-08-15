package com.example.passwordmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.helpers.Keypad
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class JoinActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {
    // note. widgets
    // note. body
//    private lateinit var joinActivityBody__input_id_edit: EditText
    private lateinit var joinActivityBody__input_pw_edit: EditText
    private lateinit var joinActivityBody__input_pw2_edit: EditText
    // note. footer
    private lateinit var joinActivityFooter__input_submit_btn: Button
    // note. etc..
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var handler: Handler
    private lateinit var toast: Toast


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        init()
        notification()
    }

    private fun notification() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        val snack = Snackbar.make(findViewById(R.id.joinActivity__container),
            R.string.join_alarm, Snackbar.LENGTH_INDEFINITE)
        snack.show()
        snack.setAction(R.string.confirm) { snack.dismiss() }
    }

    private fun init() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. etc..
        initEtcs()
        // note. init widgets
        initWidgets()
    }

    private fun initEtcs() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. activity
        activity = this
        // note. context
        context = applicationContext
        // note. handler
        handler = Handler()
        // note. toast
        toast = Toast(context)
    }

    private fun initWidgets() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. assignment
        joinActivityFooter__input_submit_btn = findViewById(R.id.joinActivityFooter__input_submit_btn)
//        joinActivityBody__input_id_edit = findViewById(R.id.joinActivityBody__input_id_edit)
        joinActivityBody__input_pw_edit = findViewById(R.id.joinActivityBody__input_pw_edit)
        joinActivityBody__input_pw2_edit = findViewById(R.id.joinActivityBody__input_pw2_edit)

        // note. init listeners
        joinActivityFooter__input_submit_btn.setOnClickListener(this)
        joinActivityBody__input_pw2_edit.setOnEditorActionListener(this)
    }

    override fun onClick(v: View) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        when (v.id) {
            R.id.joinActivityFooter__input_submit_btn -> {
                Timber.w( "joinActivityFooter__input_submit_btn_OnClick")

                // note. check data validation
                if (submitDataValidation()) return
                // note. create new account and next process
                createNewAccount()
            }
        }
    }

    private fun createNewAccount() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        val mainActivity = MainActivity.activity

        val manager = PreferencesManager(mainActivity, Protocol.USER_PROFILE)
        manager.add(Protocol.CLIENT_PW, joinActivityBody__input_pw_edit.text.toString())

        val intent = Intent()
        intent.putExtra(Protocol.COMMAND, Protocol.SIGN_UP_SUCCESS)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun submitDataValidation(): Boolean {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

//        val id : String = joinActivityBody__input_id_edit.text.toString()
        val pw : String = joinActivityBody__input_pw_edit.text.toString()
        val pw2 : String = joinActivityBody__input_pw2_edit.text.toString()
        val delayTime : Long = 500
        Timber.i( "pw : $pw, pw2 : $pw2")

        when {
//            id.isEmpty() -> {
//                joinActivityBody__input_id_edit.requestFocus()
//                toast = Toast.makeText(context, "\'아이디\' 를 입력해주세요.", Toast.LENGTH_SHORT)
//                handler.postDelayed({Keypad(this).up(joinActivityBody__input_id_edit)}, delayTime)
//                toast.show()
//                return true
//            }
//            !id.contains("@") -> {
//                joinActivityBody__input_id_edit.requestFocus()
//                toast = Toast.makeText(context, "\'아이디\' 는 이메일 형식으로 입력해주세요.", Toast.LENGTH_SHORT)
//                handler.postDelayed({Keypad(this).up(joinActivityBody__input_id_edit)}, delayTime)
//                toast.show()
//                return true
//            }
            pw.isEmpty() -> {
                joinActivityBody__input_pw_edit.requestFocus()
                toast = Toast.makeText(context, "\'비밀번호\' 를 입력해주세요.", Toast.LENGTH_SHORT)
                handler.postDelayed({Keypad(this).up(joinActivityBody__input_pw_edit)}, delayTime)
                toast.show()
                return true
            }
            pw.length < 6 || pw.length > 17 -> {
                joinActivityBody__input_pw_edit.requestFocus()
                toast = Toast.makeText(context, "\'비밀번호\' 는 6자리 이상 18자리 이하로 입력해주세요.", Toast.LENGTH_SHORT)
                handler.postDelayed({Keypad(this).up(joinActivityBody__input_pw_edit)}, delayTime)
                toast.show()
                return true
            }
            pw != pw2 -> {
                joinActivityBody__input_pw2_edit.requestFocus()
                toast = Toast.makeText(context, "\'비밀번호\' 를 확인해주세요.", Toast.LENGTH_SHORT)
                handler.postDelayed({Keypad(this).up(joinActivityBody__input_pw2_edit)}, delayTime)
                toast.show()
                return true
            }
            else -> return false
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i( "v : ${v.id}, actionId : $actionId, event : $event")

        // note. key down
        Keypad(activity).down(v)

        when (v.id) {
            R.id.joinActivityBody__input_pw2_edit -> {
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        joinActivityFooter__input_submit_btn.performClick()
                    }
                }
            }
        }
        return false
    }
}