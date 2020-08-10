package com.example.passwordmanager.helpers

import android.app.Activity
import com.example.helpers.PreferencesManager
import com.example.passwordmanager.MainActivity
import com.example.passwordmanager.Protocol
import com.example.passwordmanager.models.AccountModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import timber.log.Timber


class AccountInfoManager {
    companion object {
        fun create(activity: Activity, receiveModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("receiveModel:$receiveModel")
                Timber.i("title:${receiveModel.title}")

                // note. origin code
                val arr: JSONArray = JSONArray()

                val storedData = get(activity)
                if (storedData.isNullOrEmpty()) {
                    Timber.w("accountData is null")
                    val obj: JSONObject = JSONObject()
                    obj.put("pk", receiveModel.pk)
                    obj.put("title", receiveModel.title)
                    obj.put("hint", receiveModel.hint)
                    obj.put("id", receiveModel.id)
                    obj.put("pw", receiveModel.pw)
                    obj.put("created", receiveModel.created)
                    obj.put("updated", receiveModel.updated)
                    Timber.i("obj:${obj}")

                    arr.put(obj)
                } else {
                    Timber.w("accountData is non-null")

                    val storedList = get(activity)!!
                    for ((idx, storedModel) in storedList.withIndex()) {
                        val obj = JSONObject()
                        obj.put("pk", storedModel.pk)
                        obj.put("title", storedModel.title)
                        obj.put("hint", storedModel.hint)
                        obj.put("id", storedModel.id)
                        obj.put("pw", storedModel.pw)
                        obj.put("created", storedModel.created)
                        obj.put("updated", storedModel.updated)
                        arr.put(obj)
                    }
                    val obj = JSONObject()
                    obj.put("pk", receiveModel.pk)
                    obj.put("title", receiveModel.title)
                    obj.put("hint", receiveModel.hint)
                    obj.put("id", receiveModel.id)
                    obj.put("pw", receiveModel.pw)
                    obj.put("created", receiveModel.created)
                    obj.put("updated", receiveModel.updated)
                    arr.put(obj)
                }
                // note. save into device
                PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())

            } catch (e: Exception) {e.printStackTrace()}
        }

        fun update(activity: Activity, p: Int, receiveModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("${receiveModel.logger()}")
                if (!get(activity).isNullOrEmpty()) {
                    val arr = JSONArray()
                    for ((idx, storedModel) in get(activity)!!.withIndex()) {
                        val obj = JSONObject()
                        if (receiveModel.pk == storedModel.pk) {
                            obj.put("pk", receiveModel.pk)
                            obj.put("title", receiveModel.title)
                            obj.put("hint", receiveModel.hint)
                            obj.put("id", receiveModel.id)
                            obj.put("pw", receiveModel.pw)
                            obj.put("created", receiveModel.created)
                            obj.put("updated", receiveModel.updated)
                            arr.put(obj)
                            continue
                        }
                        obj.put("pk", storedModel.pk)
                        obj.put("title", storedModel.title)
                        obj.put("hint", storedModel.hint)
                        obj.put("id", storedModel.id)
                        obj.put("pw", storedModel.pw)
                        obj.put("created", storedModel.created)
                        obj.put("updated", storedModel.updated)
                        arr.put(obj)
                    }

                    // note. save into device
                    PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())
                }

            } catch (e: Exception) {e.printStackTrace()}
        }

        fun get(activity: Activity): ArrayList<AccountModel>? {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
//                // note. origin code
//                val accountListAsString = PreferencesManager(activity, Protocol.ACCOUNT_DATA)[Protocol.ACCOUNT_LIST]
//                // note. get stored map
//                Timber.i("accountListAsString:$accountListAsString")
//                // note. origin code
//                if (!accountListAsString.isNullOrEmpty()) {
//                    val type = object: TypeToken<HashMap<String, ArrayList<AccountModel>>>(){}.type
//                    val map: HashMap<String, ArrayList<AccountModel>> = Gson().fromJson(accountListAsString, type)
//                    val userAccountList: ArrayList<AccountModel> = map[MainActivity.clientPw]!!
//                    Timber.i("userAccountList:$userAccountList")
//                    for ((idx, model) in userAccountList.withIndex()) {
//                        Timber.v("idx:$idx, title:${model.title}")
//                    }
//                    return userAccountList
//                }

                // note. new code
                val accountList: ArrayList<AccountModel> = ArrayList()
                val accountListAsString = PreferencesManager(activity, Protocol.ACCOUNT_DATA)[Protocol.ACCOUNT_LIST]
                // note. get stored map
                Timber.i("accountListAsString:$accountListAsString")
                // note. origin code
                if (!accountListAsString.isNullOrEmpty()) {
                    val storedList: JSONArray = JSONArray(accountListAsString)
                    for (idx in 0 until storedList.length()) {
                        Timber.v("storedItemTitle:${storedList.getJSONObject(idx).getString("title")}")
                        val model = AccountModel()
                        model.pk = storedList.getJSONObject(idx).getString("pk")
                        model.title = storedList.getJSONObject(idx).getString("title")
                        model.hint = storedList.getJSONObject(idx).getString("hint")
                        model.id = storedList.getJSONObject(idx).getString("id")
                        model.pw = storedList.getJSONObject(idx).getString("pw")
//                        model.created = LocalDateTime.parse(storedList.getJSONObject(idx).getString("created"))
//                        model.updated = LocalDateTime.parse(storedList.getJSONObject(idx).getString("updated"))
                        model.created = storedList.getJSONObject(idx).getString("created")
                        model.updated = storedList.getJSONObject(idx).getString("updated")

                        accountList.add(model)
                    }
                    return accountList
                }
            } catch (e: Exception) {e.printStackTrace()}
            return null
        }
    }
}