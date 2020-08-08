package com.example.passwordmanager.models

import android.util.Log
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class AccountModel {
    lateinit var title: String
    lateinit var hint: String
    lateinit var id: String
    lateinit var pw: String
    lateinit var created: LocalDateTime
    lateinit var updated: LocalDateTime

    fun setAll(title: String?, hint: String?, id: String?, pw: String?) {
        this.title = title.toString()
        this.hint = hint.toString()
        this.id = id.toString()
        this.pw = pw.toString()
    }

    fun logger() {
        Timber.i( "title : $title")
        Timber.i( "hint : $hint")
        Timber.i( "id : $id")
        Timber.i( "pw : $pw")
    }
}