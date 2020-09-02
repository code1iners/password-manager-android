package com.example.passwordmanager.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.example.helpers.*
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.Protocol.EMPTY
import com.example.passwordmanager.Protocol.REQUEST_CODE_GALLERY_PHOTO
import com.example.passwordmanager.R
import com.example.passwordmanager.helpers.AccountInfoManager
import com.example.passwordmanager.models.AccountModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.account_list_item.*
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class AccountAddActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, TextView.OnEditorActionListener {
    // note. vars
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var accessKind: String
    private lateinit var accountModel: AccountModel
    private var accountModelPosition: Int? = null
    private lateinit var snack: Snackbar
    private var thumbnailPath: String? = null
    // note. widgets
    private lateinit var accountAddActivity__layout: RelativeLayout
    private lateinit var accountAddActivity__container: RelativeLayout
    // note. header
    private lateinit var accountAddActivity__header__status_title: TextView
    private lateinit var accountAddActivity__header__status_back: ImageButton
    // note. body
    private lateinit var accountAddActivity__body__account_title_edit: EditText
    private lateinit var accountAddActivity__body__account_id_edit: EditText
    private lateinit var accountAddActivity__body__account_pw_edit: EditText
    private lateinit var accountAddActivity__body__account_hint_edit: EditText
    private lateinit var accountAddActivity__body__account_icon_path_value: TextView
    private lateinit var accountAddActivity__body__account_icon_search: ImageButton
    // note. footer
    private lateinit var accountAddActivity__footer__submit_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        init()
    }

    private fun init() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)

        // note. init variables
        initVars()
        // note. check args
        checkArgs()
        // note. init widgets
        initWidgets()
        // note. display always on
        ScreenManager.alwaysOn(activity)
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
                        thumbnailPath = accountModel.icon
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
            accountAddActivity__container = findViewById(R.id.accountAddActivity__container)    // note. container
            // note. header
            accountAddActivity__header__status_title = findViewById(R.id.accountAddActivity__header__status_title)    // note. title
            accountAddActivity__header__status_back = findViewById(R.id.accountAddActivity__header__status_back)    // note. back
            // note. body
            accountAddActivity__body__account_title_edit = findViewById(R.id.accountAddActivity__body__account_title_edit)
            accountAddActivity__body__account_id_edit = findViewById(R.id.accountAddActivity__body__account_id_edit)
            accountAddActivity__body__account_pw_edit = findViewById(R.id.accountAddActivity__body__account_pw_edit)
            accountAddActivity__body__account_hint_edit = findViewById(R.id.accountAddActivity__body__account_hint_edit)
            accountAddActivity__body__account_icon_path_value = findViewById(R.id.accountAddActivity__body__account_icon_path_value)
            accountAddActivity__body__account_icon_search = findViewById(R.id.accountAddActivity__body__account_icon_search)
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
            accountAddActivity__body__account_icon_search.setOnClickListener(this)
            // note. footer
            accountAddActivity__footer__submit_btn.setOnClickListener(this)
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgetsFilter() {
        Timber.w(object: Any(){}.javaClass.enclosingMethod!!.name)
        try {
            when (accessKind) {
                Protocol.MODIFY -> {
                    try {
                        // note. body text
                        accountAddActivity__header__status_title.text = resources.getString(R.string.modify_account)
                        accountAddActivity__body__account_title_edit.setText(accountModel.title)
                        accountAddActivity__body__account_id_edit.setText(accountModel.id)
                        accountAddActivity__body__account_pw_edit.setText(accountModel.pw)
                        accountAddActivity__body__account_hint_edit.setText(accountModel.hint)
                        if (!accountModel.icon.isBlank()) {

                            accountAddActivity__body__account_icon_path_value.text = (accountModel.icon)
                            Glide.with(context).load(accountModel.icon).apply(GlideOptions.centerCropAndRadius(16)).into(accountAddActivity__body__account_icon_search)
                        }
                        // note. footer button text
                        accountAddActivity__footer__submit_btn.text = "변경"
                    } catch (e: Exception) {e.printStackTrace()}
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
            val icon = if (thumbnailPath.isNullOrEmpty()) EMPTY else thumbnailPath!!
            val created = LocalDateTime.now().toString()
            if (!accountInfoIsNull(title, id, pw, hint, icon)) return
            val uniqueKey = PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(true).useUpper(true).usePunctuation(true).build()

            Timber.i("title:$title, id:$id, pw:$pw, hint:$hint, icon:$icon created:$created")
            val model = AccountModel()
            model.pk = uniqueKey.generate(Protocol.UNIQUE_KEY_LENGTH)
            model.title = title
            model.id = id
            model.pw = pw
            model.hint = hint
            model.icon = icon
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
            val icon = if (thumbnailPath == null) EMPTY else thumbnailPath!!
            val updated = LocalDateTime.now().toString()
            if (!accountInfoIsNull(title, id, pw, hint, icon)) return

            Timber.i("title:$title, id:$id, pw:$pw, hint:$hint, icon:$icon updated:$updated")

            accountModel.title = title
            accountModel.id = id
            accountModel.pw = pw
            accountModel.hint = hint
            if (icon != EMPTY) accountModel.icon = icon
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

    private fun accountInfoIsNull(title: String, id: String, pw: String, hint: String, icon: String): Boolean {
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
            if (icon.isBlank()) {
                accountAddActivity__body__account_icon_search.performClick()
                val toast = Toast.makeText(context, "계정 이미지를 선택해주세요.", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 0)
                toast.show()
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
                // note. search thumbnail
                R.id.accountAddActivity__body__account_icon_search -> {
                    if (PermissionManager.checkPermissionSimply(activity, accountAddActivity__container, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_CODE_GALLERY_PHOTO)
                    }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requetsCode:$requestCode, resultCode:$resultCode, data:$data")
        try {
            if (resultCode == RESULT_OK) {
                when (requestCode) {
                    REQUEST_CODE_GALLERY_PHOTO -> {
                        try {
                            thumbnailPath = FileManager.getImagePathByUri(activity, data?.data!!)
                            Timber.i("thumbnailPath:$thumbnailPath")
                            // note. set thumbnail
                            Glide.with(context).load(data?.data!!).apply(GlideOptions.centerCropAndRadius(16)).into(accountAddActivity__body__account_icon_search)
                            // note. set path value
                            accountAddActivity__body__account_icon_path_value.text = thumbnailPath
                        } catch (e: Exception) {e.printStackTrace()}
                    }
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode:$requestCode")
        for (permission in permissions) Timber.v("permission:$permission")
        for (grantResult in grantResults) Timber.v("grantResult:$grantResult")
        try {
            when (requestCode) {
                PermissionManager.RequestCode.WRITE_EXTERNAL_STORAGE -> {
                    if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        accountAddActivity__body__account_icon_search.performClick()
                    } else {
                        Timber.i("don't ask?:${shouldShowRequestPermissionRationale(permissions[0])}")
                        if (shouldShowRequestPermissionRationale(permissions[0])) {

                        } else {
                            PermissionManager.showDialogPermission(activity, accountAddActivity__container, permissions[0])
                        }
                    }
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }
}