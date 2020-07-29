package com.example.passwordmanager.models

import android.util.Log

class AccountModel {
    companion object {
        val TAG = AccountModel::class.simpleName
    }

    lateinit var title: String
    lateinit var nickname: String
    lateinit var id: String
    lateinit var pw: String

    fun setAll(title: String?, nickname: String?, id: String?, pw: String?) {
        this.title = title.toString()
        this.nickname = nickname.toString()
        this.id = id.toString()
        this.pw = pw.toString()
    }

    fun logger() {
        Log.i(TAG, "title : $title")
        Log.i(TAG, "nickname : $nickname")
        Log.i(TAG, "id : $id")
        Log.i(TAG, "pw : $pw")
    }
}