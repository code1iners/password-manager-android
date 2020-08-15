package com.example.passwordmanager.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.example.helpers.FileManager
import com.example.helpers.GlideOptions
import com.example.helpers.PermissionManager
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.Protocol.COMMAND
import com.example.passwordmanager.Protocol.REQUEST_CODE_GALLERY_PHOTO
import com.example.passwordmanager.Protocol.SUCCESS
import com.example.passwordmanager.Protocol.USER_NICKNAME
import com.example.passwordmanager.Protocol.USER_PROFILE
import com.example.passwordmanager.Protocol.USER_THUMBNAIL
import com.example.passwordmanager.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.lang.Exception

class MyActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {
    // note. other varse
    lateinit var context: Context
    lateinit var activity: Activity
    var thumbnailFilePath: String? = null
    var isGrantedWriteExternalStorage = false
    var isGrantedReadExternalStorage = false
    var isUpdated = false
    // note. widgets
    // note. widgets-header
    lateinit var myActivity__container: RelativeLayout
    lateinit var myActivity__header__status_back: ImageButton
    lateinit var myActivity__header__avatar_thumbnail: ImageButton
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
        Timber.i( "savedName:${MainActivity.clientNickname}, savedThubmanil:${MainActivity.clientThumbnail}")
        if (!MainActivity.clientNickname.isNullOrEmpty()) {
            // note. apply username
            myActivity__header__username.text = MainActivity.clientNickname
            // note. apply edit
            myActivity__body__nickname_edit.setText(MainActivity.clientNickname)
        }
        if (!MainActivity.clientThumbnail.isNullOrEmpty()) {
            Glide.with(context).load(MainActivity.clientThumbnail).apply(GlideOptions.centerCropAndCircleCrop()).into(myActivity__header__avatar_thumbnail)
        }
    }

    private fun init() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        context = applicationContext
        activity = this
        // note. init widgets
        initWidgets()
    }

    private fun initWidgets() {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        myActivity__container = findViewById(R.id.myActivity__container)
        myActivity__header__status_back = findViewById(R.id.myActivity__header__status_back)
        myActivity__header__avatar_thumbnail = findViewById(R.id.myActivity__header__avatar_thumbnail)
        myActivity__header__username = findViewById(R.id.myActivity__header__username)
        myActivity__body__nickname_edit = findViewById(R.id.myActivity__body__nickname_edit)
        myActivity__footer__btn_save = findViewById(R.id.myActivity__footer__btn_save)
        myActivity__footer__btn_signOut = findViewById(R.id.myActivity__footer__btn_signOut)

        // note. set listeners
        myActivity__header__status_back.setOnClickListener(this)
        myActivity__header__avatar_thumbnail.setOnClickListener(this)
        myActivity__footer__btn_signOut.setOnClickListener(this)
        myActivity__footer__btn_save.setOnClickListener(this)

        myActivity__body__nickname_edit.setOnEditorActionListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.i( "keyCode : $keyCode, event : $event, repeatCount : ${event?.repeatCount}")
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {

                myActivity__header__status_back.performClick()

                return true
            }
        } catch (e: Exception) {e.printStackTrace()}

        return super.onKeyDown(keyCode, event)
    }



    override fun onClick(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("${resources.getResourceEntryName(v.id)}_OnClick")
        when (v.id) {
            R.id.myActivity__header__status_back -> {
                try {
                    if (isUpdated) {
                        val myActivityResult = Intent()
                        myActivityResult.putExtra(COMMAND, SUCCESS)
                        myActivityResult.putExtra(USER_NICKNAME, myActivity__body__nickname_edit.text.toString())
                        myActivityResult.putExtra(USER_THUMBNAIL, thumbnailFilePath)
                        setResult(RESULT_OK, myActivityResult)
                    }
                    finish()
                } catch (e: Exception) {e.printStackTrace()}
            }

            R.id.myActivity__header__avatar_thumbnail -> {
                try {
                    Timber.i("isGrantedWriteExternalStorage:$isGrantedWriteExternalStorage, isGrantedReadExternalStorage:$isGrantedReadExternalStorage")
                    if (PermissionManager.checkPermissionSimply(activity, myActivity__container, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), REQUEST_CODE_GALLERY_PHOTO)
                    }

                } catch (e: Exception) {e.printStackTrace()}
            }

            R.id.myActivity__footer__btn_signOut -> {
                try {
                    val builder = AlertDialog.Builder(activity)
                    builder
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 시 기존 저장된 데이터는 모두 소실됩니다. 진행하시겠습니까?")
                        .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            Timber.w("YES, interface:$dialogInterface, i:$i")
                            val myActivityResult = Intent()
                            setResult(RESULT_OK, myActivityResult)
                            myActivityResult.putExtra(Protocol.COMMAND, Protocol.SIGN_OUT)
                            finish()
                        }
                        .setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
                            Timber.w("NO, interface:$dialogInterface, i:$i")
                        }

                    val dialog = builder.create()
                    dialog.show()
                } catch (e: Exception) {e.printStackTrace()}
            }

            R.id.myActivity__footer__btn_save -> {
                try {
                    // note. declared and assignment in var
                    val nickname = myActivity__body__nickname_edit.text.toString()
                    val thumbnail = thumbnailFilePath!!
                    // note. apply nickname field
                    myActivity__header__username.text = nickname
                    // note. stored nickname user's device
                    PreferencesManager(MainActivity.activity, USER_PROFILE).add(USER_NICKNAME, nickname)
                    PreferencesManager(MainActivity.activity, USER_PROFILE).add(USER_THUMBNAIL, thumbnail)
                    // note. show snack bar
                    snack = Snackbar.make(findViewById(R.id.myActivity__container),
                        R.string.nickname_saved, Snackbar.LENGTH_LONG)
                    snack.show()
                    snack.setAction(R.string.confirm) { snack.dismiss() }
                    // note. change updated status
                    isUpdated = true
                } catch (e: Exception) {e.printStackTrace()}
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode:$requestCode, resultCode:$resultCode, data:$data")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY_PHOTO -> {
                    try {
                        thumbnailFilePath = FileManager.getImagePathByUri(activity, data?.data!!)!!
                        Glide.with(activity).load(thumbnailFilePath).circleCrop().into(myActivity__header__avatar_thumbnail)
                    } catch (e: Exception) {e.printStackTrace()}
                }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("requestCode:$requestCode")
        for (permission in permissions) Timber.v("permission:$permission")
        for (grantResult in grantResults) Timber.v("grantResult:$grantResult")
        try {
            when (requestCode) {
                PermissionManager.RequestCode.WRITE_EXTERNAL_STORAGE -> {
                    if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        myActivity__header__avatar_thumbnail.performClick()
                    } else {
                        Timber.i("don't ask?:${shouldShowRequestPermissionRationale(permissions[0])}")
                        if (shouldShowRequestPermissionRationale(permissions[0])) {

                        } else {
                            PermissionManager.showDialogPermission(activity, myActivity__container, permissions[0])
                        }
                    }
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }
}