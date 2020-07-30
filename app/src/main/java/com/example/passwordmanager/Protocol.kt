package com.example.passwordmanager

object Protocol {
    // note. account using 1000 ~ 1999
    const val REQUEST_CODE_LOGIN = 1000
    const val REQUEST_CODE_JOIN = 1001
    const val REQUEST_CODE_MY = 1002
    const val REQUEST_CODE_EXIT = 1002

    // note.
    const val NULL = "null"
    const val ACCOUNT = "ACCOUNT"
    const val CLIENT_ID = "CLIENT_ID"
    const val CLIENT_PW = "CLIENT_PW"
    const val ACCESS_KIND = "ACCESS_KIND"
    const val MY_ACTIVITY = "MY_ACTIVITY"
    const val COMMAND = "COMMAND"
    const val SIGN_OUT = "SIGN_OUT"
    const val APP_TERMINATE = "APP_TERMINATE"
    const val NICKNAME = "NICKNAME"
}