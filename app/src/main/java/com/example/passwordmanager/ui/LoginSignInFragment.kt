package com.example.passwordmanager.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.passwordmanager.R
import timber.log.Timber

class LoginSignInFragment : Fragment(), View.OnClickListener, TextView.OnEditorActionListener {

    private lateinit var sourceView: LoginActivity

    lateinit var signInFragment_login_password_edit: EditText
    lateinit var signInFragment_login_method_password_container: LinearLayout
    lateinit var signInFragment_login_method_password_image: ImageButton
    lateinit var signInFragment_login_method_password_image_text: TextView
    lateinit var signInFragment_login_method_bio_container: LinearLayout
    lateinit var signInFragment_login_method_bio_image: ImageButton
    lateinit var signInFragment_login_method_bio_text: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_login_sign_in, container, false)

        init(v)

        return v
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        sourceView = (context as LoginActivity)

    }

    private fun initWidgets(v: View) {

        signInFragment_login_password_edit = v.findViewById(R.id.signInFragment_login_password_edit)

        signInFragment_login_method_password_container = v.findViewById(R.id.signInFragment_login_method_password_container)
        signInFragment_login_method_password_image = v.findViewById(R.id.signInFragment_login_method_password_image)
        signInFragment_login_method_password_image_text = v.findViewById(R.id.signInFragment_login_method_password_image_text)

        signInFragment_login_method_bio_container = v.findViewById(R.id.signInFragment_login_method_bio_container)
        signInFragment_login_method_bio_image = v.findViewById(R.id.signInFragment_login_method_bio_image)
        signInFragment_login_method_bio_text = v.findViewById(R.id.signInFragment_login_method_bio_text)

        // note. listener
        signInFragment_login_password_edit.setOnEditorActionListener(this)

        signInFragment_login_method_password_container.setOnClickListener(this)
        signInFragment_login_method_password_image.setOnClickListener(this)
        signInFragment_login_method_password_image_text.setOnClickListener(this)

        signInFragment_login_method_bio_container.setOnClickListener(this)
        signInFragment_login_method_bio_image.setOnClickListener(this)
        signInFragment_login_method_bio_text.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i(resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.signInFragment_login_method_password_container -> {
                sourceView.loginWithPassword()
            }

            R.id.signInFragment_login_method_password_image -> {
                signInFragment_login_method_password_container.performClick()
            }

            R.id.signInFragment_login_method_password_image_text -> {
                signInFragment_login_method_password_container.performClick()
            }

            R.id.signInFragment_login_method_bio_container -> {
                sourceView.loginWithBio()
            }
            R.id.signInFragment_login_method_bio_image -> {
                signInFragment_login_method_bio_container.performClick()
            }

            R.id.signInFragment_login_method_bio_text -> {
                signInFragment_login_method_bio_container.performClick()
            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("v:${resources.getResourceEntryName(v.id)}")
        Timber.v("actionId:$actionId, event:$event")
        when (v.id) {
            R.id.signInFragment_login_password_edit -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signInFragment_login_method_password_container.performClick()
                    return true
                }
            }
        }
        return false
    }
}