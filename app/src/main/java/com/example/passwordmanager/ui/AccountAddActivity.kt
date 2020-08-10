package com.example.passwordmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.helpers.Keypad
import com.example.helpers.PasswordGenerator
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.R
import com.example.passwordmanager.helpers.AccountInfoManager
import com.example.passwordmanager.models.AccountModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class AccountAddActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, TextView.OnEditorActionListener {
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var accessKind: String
    private lateinit var accountModel: AccountModel
    private var accountModelPosition: Int? = null
    private lateinit var snack: Snackbar
    // note. widgets
    private lateinit var accountAddActivity__layout: RelativeLayout
    // note. header
    private lateinit var accountAddActivity__header__status_title: TextView
    private lateinit var accountAddActivity__header__status_back: ImageButton
    // note. body
    private lateinit var accountAddActivity__body__account_title_edit: EditText
    private lateinit var accountAddActivity__body__account_id_edit: EditText
    private lateinit var accountAddActivity__body__account_pw_edit: EditText
    private lateinit var accountAddActivity__body__account_hint_edit: EditText
    // note. footer
    private lateinit var accountAddActivity__footer__submit_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        init()
    }

    private fun init() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        // note. init etc
        initVars()
        // note. check args
        checkArgs()
        // note. init widgets
        initWidgets()
    }

    private fun checkArgs() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            if (intent != null) {
                accessKind = intent.getStringExtra(Protocol.ACCESS_KIND)
                Timber.i("accessKind:$accessKind")
                when (accessKind) {
                    Protocol.MODIFY -> {
                        accountModel = Gson().fromJson(intent.getStringExtra(Protocol.ACCOUNT_MODEL), AccountModel::class.java)
                        accountModelPosition = intent.getIntExtra(Protocol.POSITION, -1)
                        accountModel.logger()
                    }
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initVars() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        activity = this
        context = this
    }

    private fun initWidgets() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        // note. assignment
        initWidgetsAssignment()
        // note. listeners
        initWidgetsListener()
        // note. filter
        initWidgetsFilter()
    }

    private fun initWidgetsAssignment() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            accountAddActivity__layout = findViewById(R.id.accountAddActivity__layout)    // note. layout
            // note. header
            accountAddActivity__header__status_title = findViewById(R.id.accountAddActivity__header__status_title)    // note. back
            accountAddActivity__header__status_back = findViewById(R.id.accountAddActivity__header__status_back)    // note. back
            // note. body
            accountAddActivity__body__account_title_edit = findViewById(R.id.accountAddActivity__body__account_title_edit)    // note. back
            accountAddActivity__body__account_id_edit = findViewById(R.id.accountAddActivity__body__account_id_edit)    // note. back
            accountAddActivity__body__account_pw_edit = findViewById(R.id.accountAddActivity__body__account_pw_edit)    // note. back
            accountAddActivity__body__account_hint_edit = findViewById(R.id.accountAddActivity__body__account_hint_edit)    // note. back
            // note. footer
            accountAddActivity__footer__submit_btn = findViewById(R.id.accountAddActivity__footer__submit_btn)    // note. create
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgetsListener() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. header
            accountAddActivity__header__status_back.setOnClickListener(this)
            accountAddActivity__header__status_title.setOnClickListener(this)
            accountAddActivity__header__status_title.setOnLongClickListener(this)
            // note. body
            accountAddActivity__body__account_title_edit.setOnEditorActionListener(this)
            accountAddActivity__body__account_id_edit.setOnEditorActionListener(this)
            accountAddActivity__body__account_pw_edit.setOnEditorActionListener(this)
            accountAddActivity__body__account_hint_edit.setOnEditorActionListener(this)
            // note. footer
            accountAddActivity__footer__submit_btn.setOnClickListener(this)
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgetsFilter() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            when (accessKind) {
                Protocol.MODIFY -> {

                    // note. body text
                    accountAddActivity__body__account_title_edit.setText(accountModel.title)
                    accountAddActivity__body__account_id_edit.setText(accountModel.id)
                    accountAddActivity__body__account_pw_edit.setText(accountModel.pw)
                    accountAddActivity__body__account_hint_edit.setText(accountModel.hint)
                    // note. footer button text
                    accountAddActivity__footer__submit_btn.text = "변경"
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun createAccount() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val title = accountAddActivity__body__account_title_edit.text.toString()
            val id = accountAddActivity__body__account_id_edit.text.toString()
            val pw = accountAddActivity__body__account_pw_edit.text.toString()
            val hint = accountAddActivity__body__account_hint_edit.text.toString()
            val created = LocalDateTime.now().toString()
            if (!accountInfoIsNull(title, id, pw, hint)) return
            val uniqueKey = PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(true).useUpper(true).usePunctuation(true).build()

            Timber.i("title:$title, id:$id, pw:$pw, hint:$hint, created:$created")
            val model = AccountModel()
            model.pk = uniqueKey.generate(Protocol.UNIQUE_KEY_LENGTH)
            model.title = title
            model.id = id
            model.pw = pw
            model.hint = hint
            model.created = created
            model.updated = created

            model.logger()

            // note. save user's device
            AccountInfoManager.create(activity, model)
            // note. activity exit
            val accountAddActivityResult = Intent()
            accountAddActivityResult.putExtra(Protocol.COMMAND, Protocol.SUCCESS)
            setResult(RESULT_OK, accountAddActivityResult)
            finish()
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun updateAccount() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val title = accountAddActivity__body__account_title_edit.text.toString()
            val id = accountAddActivity__body__account_id_edit.text.toString()
            val pw = accountAddActivity__body__account_pw_edit.text.toString()
            val hint = accountAddActivity__body__account_hint_edit.text.toString()
            val updated = LocalDateTime.now().toString()
            if (!accountInfoIsNull(title, id, pw, hint)) return

            Timber.i("title:$title, id:$id, pw:$pw, hint:$hint, updated:$updated")

            accountModel.title = title
            accountModel.id = id
            accountModel.pw = pw
            accountModel.hint = hint
            accountModel.updated = updated

            // note. save user's device
            AccountInfoManager.update(activity, accountModelPosition!!, accountModel)
            // note. model serialize
            // note. activity exit
            val accountAddActivityResult = Intent()
            accountAddActivityResult.putExtra(Protocol.COMMAND, Protocol.SUCCESS)
            setResult(RESULT_OK, accountAddActivityResult)
            finish()
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun accountInfoIsNull(title: String, id: String, pw: String, hint: String): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            if (title.isEmpty()) {
                accountAddActivity__body__account_title_edit.requestFocus()
                Keypad(activity).up(accountAddActivity__body__account_title_edit)
                snack = Snackbar.make(accountAddActivity__layout, "계정 이름을 입력해주세요.", Snackbar.LENGTH_LONG)
                snack.setAction("확인") { snack.dismiss() }
                snack.show()
                return false
            }
            if (id.isEmpty()) {
                accountAddActivity__body__account_id_edit.requestFocus()
                Keypad(activity).up(accountAddActivity__body__account_id_edit)
                snack = Snackbar.make(accountAddActivity__layout, "아이디를 입력해주세요.", Snackbar.LENGTH_LONG)
                snack.setAction("확인") { snack.dismiss() }
                snack.show()
                return false
            }
            if (pw.isEmpty()) {
                accountAddActivity__body__account_pw_edit.requestFocus()
                Keypad(activity).up(accountAddActivity__body__account_pw_edit)
                snack = Snackbar.make(accountAddActivity__layout, "비밀번호를 입력해주세요.", Snackbar.LENGTH_LONG)
                snack.setAction("확인") { snack.dismiss() }
                snack.show()
                return false
            }
            if (hint.isEmpty()) {
                accountAddActivity__body__account_hint_edit.requestFocus()
                Keypad(activity).up(accountAddActivity__body__account_hint_edit)
                snack = Snackbar.make(accountAddActivity__layout, "계정 설명 입력해주세요.", Snackbar.LENGTH_LONG)
                snack.setAction("확인") { snack.dismiss() }
                snack.show()
                return false
            }
        } catch (e: Exception) {e.printStackTrace()}
        return true
    }

    override fun onClick(v: View) {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            when (v.id) {
                // note. header
                // note. back
                R.id.accountAddActivity__header__status_back -> {
                    val accountAddActivityResult = Intent()
                    accountAddActivityResult.putExtra(Protocol.COMMAND, Protocol.BACK)
                    setResult(RESULT_OK)
                    finish()
                }
                R.id.accountAddActivity__header__status_title -> {
                    Timber.i("ACCOUNT_OBJECT Check ${Protocol.SUCCESS}")
                    PreferencesManager(activity, Protocol.ACCOUNT_DATA).check()
                }
                // note. create new account
                R.id.accountAddActivity__footer__submit_btn -> {
                    Timber.w("accountAddActivity__footer__submit_btn_OnClick")
                    if (accessKind == Protocol.CREATE) createAccount()
                    else if (accessKind == Protocol.MODIFY) updateAccount()
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onLongClick(v: View): Boolean {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            when (v.id) {
                R.id.accountAddActivity__header__status_title -> {
                    Timber.i("ACCOUNT_OBJECT Delete ${Protocol.SUCCESS}")
                    PreferencesManager(activity, Protocol.ACCOUNT_DATA).remove(Protocol.ACCOUNT_LIST)
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
        return true
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i("v:${resources.getResourceEntryName(v.id)}_OnAction, actionId:$actionId, event:${event}")
            when (v.id) {
                R.id.accountAddActivity__body__account_title_edit -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        accountAddActivity__body__account_id_edit.requestFocus()
                    }
                }

                R.id.accountAddActivity__body__account_id_edit -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        accountAddActivity__body__account_pw_edit.requestFocus()
                    }
                }

                R.id.accountAddActivity__body__account_pw_edit -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        accountAddActivity__body__account_hint_edit.requestFocus()
                    }
                }

                R.id.accountAddActivity__body__account_hint_edit -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        accountAddActivity__footer__submit_btn.performClick()
                    }
                }

            }
        } catch (e: Exception) {e.printStackTrace()}
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        try {
            Timber.i( "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
                accountAddActivity__header__status_back.performClick()
                return true
            }
        } catch (e: Exception) {e.printStackTrace()}
        return false
    }
}