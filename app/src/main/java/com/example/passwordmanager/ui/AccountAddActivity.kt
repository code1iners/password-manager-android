package com.example.passwordmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.os.bundleOf
import com.example.passwordmanager.R
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class AccountAddActivity : AppCompatActivity(), View.OnClickListener {
    // note. widgets
    lateinit var accountAddActivity__header__status_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        init()
    }

    private fun init() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        // note. header
        accountAddActivity__header__status_back = findViewById(R.id.accountAddActivity__header__status_back)    // note. back
        initWidgetsListener()
    }

    private fun initWidgetsListener() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            accountAddActivity__header__status_back.setOnClickListener(this)
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onClick(v: View) {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            when (v.id) {
                // note. header
                // note. back
                R.id.accountAddActivity__header__status_back -> { finish() }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }
}