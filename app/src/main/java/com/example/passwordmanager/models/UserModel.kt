package com.example.passwordmanager.models

import android.util.Log
import timber.log.Timber

class UserModel {
    lateinit var nickname: String
    lateinit var id: String
    lateinit var pw: String

    fun setAll(nickname: String, id: String, pw: String) {
        this.nickname = nickname
        this.id = id
        this.pw = pw
    }

    fun logging() {
        Timber.w( object: Any(){}.javaClass.enclosingMethod!!.name)

        Timber.i( "nickname : $nickname")
        Timber.i( "id : $id")
        Timber.i( "pw : $pw")
    }
}