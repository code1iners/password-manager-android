package com.example.passwordmanager.models

import android.util.Log

class UserModel {
    companion object {
        val TAG = UserModel::class.simpleName
    }

    lateinit var nickname: String
    lateinit var id: String
    lateinit var pw: String

    fun setAll(nickname: String, id: String, pw: String) {
        this.nickname = nickname
        this.id = id
        this.pw = pw
    }

    fun logging() {
        Log.w(TAG, object: Any(){}.javaClass.enclosingMethod!!.name)

        Log.i(TAG, "nickname : $nickname")
        Log.i(TAG, "id : $id")
        Log.i(TAG, "pw : $pw")
    }
}