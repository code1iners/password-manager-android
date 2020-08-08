package com.example.passwordmanager.helpers

import android.app.Activity
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.models.AccountModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber


class AccountInfoManager {
    companion object {
        fun get(activity: Activity): ArrayList<AccountModel>? {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                val accountListAsString = PreferencesManager(activity, Protocol.ACCOUNT_DATA)[Protocol.ACCOUNT_LIST]
                // note. get stored map
                Timber.i("accountListAsString:$accountListAsString")
                if (!accountListAsString.isNullOrEmpty()) {
                    val type = object: TypeToken<HashMap<String, ArrayList<AccountModel>>>(){}.type
                    val map: HashMap<String, ArrayList<AccountModel>> = Gson().fromJson(accountListAsString, type)
                    val userAccountList: ArrayList<AccountModel> = map[MainActivity.clientPw]!!
                    Timber.i("userAccountList:$userAccountList")
                    for ((idx, model) in userAccountList.withIndex()) {
                        Timber.v("idx:$idx, title:${model.title}")
                    }
                    return userAccountList
                }
            } catch (e: Exception) {e.printStackTrace()}
            return null
        }

        fun create(activity: Activity, accountModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("accountModel:$accountModel")
                Timber.i("title:${accountModel.title}")

                var map = HashMap<String, ArrayList<AccountModel>>()
                val list: ArrayList<AccountModel> = ArrayList()

                val accountData = get(activity)
                if (accountData.isNullOrEmpty()) {
                    Timber.w("accountData is null")

                    list.add(accountModel)
                } else {
                    Timber.w("accountData is non-null")
                    for ((idx, storedModel) in accountData.withIndex()) {
                        Timber.v("idx:$idx, title:${storedModel.title}")
                        list.add(storedModel)
                    }
                    Timber.i("newItemTitle:${accountModel.title}")
                    list.add(accountModel)
                }

                // note. merge user info and account data
                map[MainActivity.clientPw!!] = list
                // note. save into device
                PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, Gson().toJson(map))

//            val storedMap: HashMap<String, AccountModel>? = getByUserId(receivedMap)
//
//            if (storedMap.isNullOrEmpty()) {
//                PreferencesManager(activity, Protocol.ACCOUNT_LIST).add(Protocol.ACCOUNT_OBJECT, Gson().toJson(receivedMap))
//            } else {
//                val type = object: TypeToken<HashMap<String, AccountModel>>(){}.type
//                Timber.e("storedMap:$storedMap, key:${storedMap.keys}, value:${storedMap.values}")
//
//                for (newItem in receivedMap) {
//                    Timber.v("newItemTitle:${newItem.key}")
//                    storedMap[newItem.key] = newItem.value
//                }
//                PreferencesManager(activity, Protocol.ACCOUNT_LIST).add(Protocol.ACCOUNT_OBJECT, Gson().toJson(storedMap))
//            }
            } catch (e: Exception) {e.printStackTrace()}
        }
    }
}