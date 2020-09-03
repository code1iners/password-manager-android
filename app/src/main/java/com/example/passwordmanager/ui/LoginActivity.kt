package com.example.passwordmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.helpers.FragmentChanger
import com.example.helpers.PreferencesManager
import com.example.helpers.ScreenManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.Protocol.COMMAND
import com.example.passwordmanager.Protocol.IS_USER
import com.example.passwordmanager.Protocol.SIGN_UP_SUCCESS
import com.example.passwordmanager.R
import com.google.android.material.tabs.TabLayout
import timber.log.Timber
import java.lang.Exception
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener, TabLayout.OnTabSelectedListener {
    private var backKeyPressedTime: Long = 0
    private lateinit var toast: Toast
    private val SIGN_IN = 0
    private val SIGN_UP = 1

    // note. fragment
    private val bodyContainer = R.id.loginActivityLoginView_tab_item_container
    lateinit var loginSignInFragment: LoginSignInFragment
    lateinit var loginSignUpFragment: LoginSignUpFragment

    // note. bio
    private var KEY_NAME = "KEY_NAME"
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // note. widgets
    private lateinit var loginActivityLoginView_tab_conatiner: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkArgs()
        init()
        filter()
    }

    private fun filter() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        if (isUser) {
            tabSelect(SIGN_IN)
            loginWithBio()
        }
        else {
            tabDisable(SIGN_IN)
            tabSelect(SIGN_UP)
        }
    }

    private fun tabDisable(tab: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val tabStrip = loginActivityLoginView_tab_conatiner.getChildAt(tab) as LinearLayout

            tabStrip.getChildAt(tab).isClickable = false
            tabStrip.getChildAt(tab).isEnabled = false

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun tabEnable(tab: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val tabStrip = loginActivityLoginView_tab_conatiner.getChildAt(tab) as LinearLayout

            tabStrip.getChildAt(tab).isClickable = true
            tabStrip.getChildAt(tab).isEnabled = true

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun tabSelect(tab: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            loginActivityLoginView_tab_conatiner.getTabAt(tab)!!.select()

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun checkArgs() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        try {
            if (intent != null) {
                isUser = intent.getStringExtra(IS_USER)!!.toBoolean()
                Timber.i("isUser:$isUser")
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun init() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. init variables
        initVars()
        // note. init widgets
        initWidgets()
        // note. init bio metric
        initBioMetric()
        // note. init fragments
        initFragments()
        // note. display always on
        ScreenManager.alwaysOn(activity)
    }

    private fun initFragments() {
        loginSignInFragment = LoginSignInFragment()
        loginSignUpFragment = LoginSignUpFragment()
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        activity = this
        context = applicationContext
        // note. init toast
        toast = Toast(context)
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        loginActivityLoginView_tab_conatiner = findViewById(R.id.loginActivityLoginView_tab_conatiner)

        initWidgetsTabLayout()
    }

    private fun initWidgetsTabLayout() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        loginActivityLoginView_tab_conatiner.setOnTabSelectedListener(this)

        // setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    }

    private fun initBioMetric() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    Timber.d("App can authenticate using biometrics.")
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Timber.e("No biometric features available on this device.")
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Timber.e("Biometric features are currently unavailable.")
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                    Timber.e("The user hasn't associated any biometric credentials with their account.")
            }

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display(status: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("status:$status")
        when (status) {
            SIGN_IN -> {
                FragmentChanger.replace(supportFragmentManager, bodyContainer, loginSignInFragment, false, null)

            }

            SIGN_UP -> {
                FragmentChanger.replace(supportFragmentManager, bodyContainer, loginSignUpFragment, false, null)

            }
        }
    }

    fun loginWithBio() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. origin code
            executor = ContextCompat.getMainExecutor(this)

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
//                        Toast.makeText(applicationContext,
//                            "Authentication error: $errString", Toast.LENGTH_SHORT)
//                            .show()

                        Timber.e("errorCode:$errorCode, errString:$errString")
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
//                        Toast.makeText(applicationContext,
//                            "Authentication succeeded!", Toast.LENGTH_SHORT)
//                            .show()

                        Timber.i("result:$result")

                        loginSuccess()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
//                        Toast.makeText(applicationContext, "Authentication failed",
//                            Toast.LENGTH_SHORT)
//                            .show()

                        Timber.e("Authentication failed!")
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for App")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo)

        } catch (e: Exception) {e.printStackTrace()}
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
//            R.id.loginActivity__header_title -> {
//                PreferencesManager(activity, Protocol.USER_PROFILE).check()
//            }
//
//            R.id.loginActivity__footer__btn1_signIn -> {
//                Timber.w( "loginActivity__footer__btn1_signIn_OnClick")
//                val pw = PreferencesManager(activity, Protocol.USER_PROFILE)[Protocol.CLIENT_PW]
//                if (!pw.isNullOrEmpty()) {
//                    Timber.i("Password is exist! (pw:$pw)")
//                    val inputPassword = loginActivity__body__input_pw_edit.text.toString()
//
//                    if (pw == inputPassword) {
//                        finish()
//                    }
//                } else {
//                    Timber.w("Password is not exist!")
//                }
//            }
//
//            R.id.loginActivity__footer__btn1_signUp -> {
//                Timber.w( "loginActivity__footer__btn1_signUp")
//
//                val joinView = Intent(context, JoinActivity::class.java)
//
//                startActivityForResult(joinView, Protocol.REQUEST_CODE_JOIN_ACTIVITY)
//            }
        }
    }

    fun loginWithPassword() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val pw = PreferencesManager(activity, Protocol.USER_PROFILE)[Protocol.CLIENT_PW]
            if (!pw.isNullOrEmpty()) {
                Timber.i("Password does exist!")
                val inputPassword = loginSignInFragment.signInFragment_login_password_edit.text.toString()

                if (pw == inputPassword) loginSuccess() else Toast.makeText(context, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            } else Timber.w("Password does not exist!")
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun loginSuccess() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        val intent = Intent()
        intent.putExtra(COMMAND, SIGN_UP_SUCCESS)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode : $requestCode, resultCode : $resultCode, data : $data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Protocol.REQUEST_CODE_JOIN_ACTIVITY -> {
                    loginSuccess()
                }
            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        when (v.id) {
//            R.id.loginActivity__body__input_pw_edit -> {
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE -> {
//                        loginActivity__footer__btn1_signIn.performClick()
//                    }
//                }
//                return true
//            }
        }
        return false
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("tab:${tab.text}")
        when (tab.text) {
            "SIGN IN" -> {
                display(SIGN_IN)

            }

            "SIGN UP" -> {
                display(SIGN_UP)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("tab:${tab.text}")
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("tab:${tab.text}")
        when (tab.text) {
            "SIGN IN" -> {
                display(SIGN_IN)

            }

            "SIGN UP" -> {
                display(SIGN_UP)
            }
        }
    }

    companion object {
        lateinit var activity: Activity
        lateinit var context: Context
        var isUser = false
    }
}