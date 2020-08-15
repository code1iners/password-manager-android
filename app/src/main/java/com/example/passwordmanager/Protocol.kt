package com.example.passwordmanager

object Protocol {
    // note. request code
    const val REQUEST_CODE_LOGIN_ACTIVITY = 1000
    const val REQUEST_CODE_JOIN_ACTIVITY = 1001
    const val REQUEST_CODE_MY_ACTIVITY = 1002
    const val REQUEST_CODE_EXIT = 1003
    const val REQUEST_CODE_ADD_ACCOUNT = 1004
    const val REQUEST_CODE_GALLERY_PHOTO = 2000

    // note.
    const val NULL = "null"
    const val EMPTY = ""
    const val BACK = "BACK"
    const val CREATE = "CREATE"
    const val MODIFY = "MODIFY"
    const val UNIQUE_KEY_LENGTH = 40
    const val USER_PROFILE = "USER_PROFILE"
    const val USER_NICKNAME = "USER_NICKNAME"
    const val USER_THUMBNAIL = "USER_THUMBNAIL"
    const val CLIENT_PW = "CLIENT_PW"
    const val ACCOUNT_DATA = "ACCOUNT_DATA"
    const val ACCOUNT_LIST = "ACCOUNT_LIST"
    const val ACCESS_KIND = "ACCESS_KIND"
    const val MY_ACTIVITY = "MY_ACTIVITY"
    const val COMMAND = "COMMAND"
    const val ARGUMENTS = "ARGUMENTS"
    const val ACCOUNT_MODEL = "ACCOUNT_MODEL"
    const val POSITION = "POSITION"
    const val SIGN_OUT = "SIGN_OUT"
    const val APP_TERMINATE = "APP_TERMINATE"
    const val SIGN_UP_SUCCESS = "SIGN_UP_SUCCESS"
    const val SUCCESS = "SUCCESS"
    const val WARNING = "WARNING"
    const val ERROR = "ERROR"
}