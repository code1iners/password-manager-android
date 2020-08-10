package com.example.passwordmanager.models

import android.util.Log
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class AccountModel {
    lateinit var pk: String
    lateinit var title: String
    lateinit var hint: String
    lateinit var id: String
    lateinit var pw: String
    lateinit var created: String
    lateinit var updated: String

    fun logger() {
        Timber.i( "pk : $pk")
        Timber.i( "title : $title")
        Timber.i( "hint : $hint")
        Timber.i( "id : $id")
        Timber.i( "pw : $pw")
        Timber.i( "created : $created")
        Timber.i( "updated : $updated")
    }
}