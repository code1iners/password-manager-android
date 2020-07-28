package com.example.passwordmanager

object Protocol {
    // note. account using 1000 ~ 1999
    const val REQUEST_CODE_LOGIN = 1000
    const val REQUEST_CODE_JOIN = 1001

    // note.
    const val ACCOUNT = "ACCOUNT"
    const val CLIENT_ID = "CLIENT_ID"
    const val CLIENT_PW = "CLIENT_PW"
    const val ACCESS_KIND = "ACCESS_KIND"
    const val INTENT_RESULT = "INTENT_RESULT"
    const val EXIT = "EXIT"
}