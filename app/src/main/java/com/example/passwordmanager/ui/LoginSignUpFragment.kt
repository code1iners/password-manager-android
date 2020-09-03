package com.example.passwordmanager.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.helpers.BooleanVariable
import com.example.helpers.Keypad
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import com.example.passwordmanager.ui.LoginActivity.Companion.isUser
import timber.log.Timber

class LoginSignUpFragment : Fragment(), View.OnClickListener, TextView.OnEditorActionListener {

    private lateinit var sourceView: LoginActivity
    private lateinit var handler: Handler
    
    // note. duplication sign up
    private var choiceResult: Boolean = false
    private lateinit var choiceIsDone: BooleanVariable

    lateinit var signUpFragment_login_password_edit: EditText
    lateinit var signUpFragment_login_password_edit_again: EditText
    lateinit var signUpFragment_login_method_password_container: LinearLayout
    lateinit var signUpFragment_login_method_password_image: ImageButton
    lateinit var signUpFragment_login_method_password_image_text: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_login_sign_up, container, false)

        init(v)

        return v
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
        initListeners()
    }

    private fun initListeners() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        choiceIsDone.setListener {
            if (choiceResult) {
                createNewAccount()
            } else {
                Toast.makeText(mContext, "취소하셨습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            mContext = context!!
            mActivity = activity!!
            mFragmentManager = fragmentManager!!

            sourceView = (mContext as LoginActivity)
            handler = Handler()

            choiceIsDone = BooleanVariable()
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgets(v: View) {

        signUpFragment_login_password_edit = v.findViewById(R.id.signUpFragment_login_password_edit)
        signUpFragment_login_password_edit_again = v.findViewById(R.id.signUpFragment_login_password_edit_again)
        signUpFragment_login_method_password_container = v.findViewById(R.id.signUpFragment_login_method_password_container)
        signUpFragment_login_method_password_image = v.findViewById(R.id.signUpFragment_login_method_password_image)
        signUpFragment_login_method_password_image_text = v.findViewById(R.id.signUpFragment_login_method_password_image_text)

        // note. listener
        signUpFragment_login_password_edit.setOnEditorActionListener(this)
        signUpFragment_login_password_edit_again.setOnEditorActionListener(this)

        signUpFragment_login_method_password_container.setOnClickListener(this)
        signUpFragment_login_method_password_image.setOnClickListener(this)
        signUpFragment_login_method_password_image_text.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i(resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.signUpFragment_login_method_password_container -> {

                if (isUser) requestDeletePreviousPw()   // note. check already has other pw(account) in device
                else createNewAccount() // note. create new account and next process

            }

            R.id.signUpFragment_login_method_password_image -> {
                signUpFragment_login_method_password_container.performClick()
            }

            R.id.signUpFragment_login_method_password_image_text -> {
                signUpFragment_login_method_password_container.performClick()
            }

        }
    }

    private fun requestDeletePreviousPw() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val b = AlertDialog.Builder(mActivity)
            b.setTitle("이전 계정 삭제")
            b.setMessage("확인을 누르시면 이전에 저장된 모든 데이터가 소실됩니다. 진행하시겠습니까?")
            b.setNegativeButton("취소") { _, _ ->
                choiceResult = false
                choiceIsDone.isBoo = true
            }
            b.setPositiveButton("확인") { _, _ ->
                PreferencesManager(mActivity, Protocol.USER_PROFILE).remove(Protocol.CLIENT_PW)
                choiceResult = true
                choiceIsDone.isBoo = true
            }

            val d = b.create()
            d.show()

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun submitDataValidation(): Boolean {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

//        val id : String = joinActivityBody__input_id_edit.text.toString()
        val pw : String = signUpFragment_login_password_edit.text.toString()
        val pw2 : String = signUpFragment_login_password_edit_again.text.toString()
        val delayTime : Long = 500
        Timber.i( "pw : $pw, pw2 : $pw2")

        when {
            pw.isEmpty() -> {
                signUpFragment_login_password_edit.requestFocus()
                handler.postDelayed({ Keypad(mActivity).up(signUpFragment_login_password_edit)}, delayTime)
                toasting("\'비밀번호\' 를 입력해주세요.")
                return true
            }
            pw.length < 6 || pw.length > 17 -> {
                signUpFragment_login_password_edit.requestFocus()
                handler.postDelayed({ Keypad(mActivity).up(signUpFragment_login_password_edit)}, delayTime)
                toasting("\'비밀번호\' 는 6자리 이상 18자리 이하로 입력해주세요.")
                return true
            }
            pw != pw2 -> {
                signUpFragment_login_password_edit_again.requestFocus()
                handler.postDelayed({ Keypad(mActivity).up(signUpFragment_login_password_edit_again)}, delayTime)
                toasting("\'비밀번호\' 를 확인해주세요.")
                return true
            }
            else -> return false
        }
    }

    private fun createNewAccount() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. check data validation
        if (submitDataValidation()) return

        val mainActivity = MainActivity.activity

        val manager = PreferencesManager(mainActivity, Protocol.USER_PROFILE)
        manager.add(Protocol.CLIENT_PW, signUpFragment_login_password_edit.text.toString())

        val intent = Intent()
        intent.putExtra(Protocol.COMMAND, Protocol.SIGN_UP_SUCCESS)
        mActivity.setResult(AppCompatActivity.RESULT_OK, intent)
        mActivity.finish()
    }

    private fun toasting(message: String) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("v:${resources.getResourceEntryName(v.id)}")
        Timber.v("actionId:$actionId, event:$event")
        when (v.id) {
            R.id.signUpFragment_login_password_edit -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signUpFragment_login_password_edit_again.requestFocus()
                    return true
                }
            }

            R.id.signUpFragment_login_password_edit_again -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signUpFragment_login_method_password_container.performClick()
                    return true
                }
            }

        }
        return false
    }

    companion object {
        lateinit var mContext: Context
        lateinit var mActivity: Activity
        lateinit var mFragmentManager: FragmentManager
    }
}