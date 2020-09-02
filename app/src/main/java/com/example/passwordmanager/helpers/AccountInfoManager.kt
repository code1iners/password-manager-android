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
                receiveModel.logger()

                // note. origin code
                val arr: JSONArray = JSONArray()

                val storedList = read(activity)
                if (storedList.isNullOrEmpty()) {
                    Timber.w("accountData is null")
                    val obj: JSONObject = JSONObject()
                    obj.put("pk", receiveModel.pk)
                    obj.put("title", receiveModel.title)
                    obj.put("hint", receiveModel.hint)
                    obj.put("id", receiveModel.id)
                    obj.put("pw", receiveModel.pw)
                    obj.put("icon", receiveModel.icon)
                    obj.put("created", receiveModel.created)
                    obj.put("updated", receiveModel.updated)
                    Timber.i("obj:${obj}")

                    arr.put(obj)
                } else {
                    Timber.w("accountData is non-null")

//                    val storedList = read(activity)!!
                    for ((idx, storedModel) in storedList.withIndex()) {
                        val obj = JSONObject()
                        obj.put("pk", storedModel.pk)
                        obj.put("title", storedModel.title)
                        obj.put("hint", storedModel.hint)
                        obj.put("id", storedModel.id)
                        obj.put("pw", storedModel.pw)
                        obj.put("icon", storedModel.icon)
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
                    obj.put("icon", receiveModel.icon)
                    obj.put("created", receiveModel.created)
                    obj.put("updated", receiveModel.updated)
                    arr.put(obj)
                }
                // note. save into device
                PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())

            } catch (e: Exception) {e.printStackTrace()}
        }

        fun create(activity: Activity, position: Int, receiveModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("position:$position")
                receiveModel.logger()

                val arr: JSONArray = JSONArray()

                val storedList = read(activity)!!
                if (storedList.isNullOrEmpty()) {
                    Timber.w("accountData is null")
                    val obj: JSONObject = JSONObject()
                    obj.put("pk", receiveModel.pk)
                    obj.put("title", receiveModel.title)
                    obj.put("hint", receiveModel.hint)
                    obj.put("id", receiveModel.id)
                    obj.put("pw", receiveModel.pw)
                    obj.put("icon", receiveModel.icon)
                    obj.put("created", receiveModel.created)
                    obj.put("updated", receiveModel.updated)
                    Timber.i("obj:${obj}")

                    arr.put(obj)
                } else {
                    Timber.w("accountData is non-null")

                    // note. origin code
//                    for ((idx, storedModel) in storedList.withIndex()) {
//                        val obj = JSONObject()
//                        obj.put("pk", storedModel.pk)
//                        obj.put("title", storedModel.title)
//                        obj.put("hint", storedModel.hint)
//                        obj.put("id", storedModel.id)
//                        obj.put("pw", storedModel.pw)
//                        obj.put("icon", storedModel.icon)
//                        obj.put("created", storedModel.created)
//                        obj.put("updated", storedModel.updated)
//                        arr.put(obj)
//                    }
//                    val obj = JSONObject()
//                    obj.put("pk", receiveModel.pk)
//                    obj.put("title", receiveModel.title)
//                    obj.put("hint", receiveModel.hint)
//                    obj.put("id", receiveModel.id)
//                    obj.put("pw", receiveModel.pw)
//                    obj.put("icon", receiveModel.icon)
//                    obj.put("created", receiveModel.created)
//                    obj.put("updated", receiveModel.updated)
//                    arr.put(obj)

                    // note. new code

                    storedList.add(position, receiveModel)
                    for ((idx, storedModel) in storedList.withIndex()) {
                        val obj = JSONObject()
                        obj.put("pk", storedModel.pk)
                        obj.put("title", storedModel.title)
                        obj.put("hint", storedModel.hint)
                        obj.put("id", storedModel.id)
                        obj.put("pw", storedModel.pw)
                        obj.put("icon", storedModel.icon)
                        obj.put("created", storedModel.created)
                        obj.put("updated", storedModel.updated)
                        arr.put(obj)
                    }

//                    var listSize = storedList.size + 1
//                    for (idx in 0 until listSize) {
//                        val obj = JSONObject()
//                        if (position == idx) {
//                            obj.put("pk", receiveModel.pk)
//                            obj.put("title", receiveModel.title)
//                            obj.put("hint", receiveModel.hint)
//                            obj.put("id", receiveModel.id)
//                            obj.put("pw", receiveModel.pw)
//                            obj.put("icon", receiveModel.icon)
//                            obj.put("created", receiveModel.created)
//                            obj.put("updated", receiveModel.updated)
//                        } else {
//                            val storedModel = storedList[idx]
//                            obj.put("pk", storedModel.pk)
//                            obj.put("title", storedModel.title)
//                            obj.put("hint", storedModel.hint)
//                            obj.put("id", storedModel.id)
//                            obj.put("pw", storedModel.pw)
//                            obj.put("icon", storedModel.icon)
//                            obj.put("created", storedModel.created)
//                            obj.put("updated", storedModel.updated)
//                        }
//
//                        arr.put(obj)
//                    }
                }

                // note. save into device
                PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())

            } catch (e: Exception) {e.printStackTrace()}
        }

        fun read(activity: Activity): ArrayList<AccountModel>? {
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
                        model.icon = storedList.getJSONObject(idx).getString("icon")
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

        fun update(activity: Activity, p: Int, receiveModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("${receiveModel.logger()}")
                if (!read(activity).isNullOrEmpty()) {
                    val arr = JSONArray()
                    for ((idx, storedModel) in read(activity)!!.withIndex()) {
                        val obj = JSONObject()
                        if (receiveModel.pk == storedModel.pk) {
                            obj.put("pk", receiveModel.pk)
                            obj.put("title", receiveModel.title)
                            obj.put("hint", receiveModel.hint)
                            obj.put("id", receiveModel.id)
                            obj.put("pw", receiveModel.pw)
                            obj.put("icon", receiveModel.icon)
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
                        obj.put("icon", storedModel.icon)
                        obj.put("created", storedModel.created)
                        obj.put("updated", storedModel.updated)
                        arr.put(obj)
                    }

                    // note. save into device
                    PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())
                }

            } catch (e: Exception) {e.printStackTrace()}
        }

        fun delete(activity: Activity, p: Int, receiveModel: AccountModel) {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
            try {
                Timber.i("${receiveModel.logger()}")
                if (!read(activity).isNullOrEmpty()) {
                    val arr = JSONArray()
                    for ((idx, storedModel) in read(activity)!!.withIndex()) {
                        val obj = JSONObject()
                        if (receiveModel.pk == storedModel.pk) continue
                        obj.put("pk", storedModel.pk)
                        obj.put("title", storedModel.title)
                        obj.put("hint", storedModel.hint)
                        obj.put("id", storedModel.id)
                        obj.put("pw", storedModel.pw)
                        obj.put("icon", storedModel.icon)
                        obj.put("created", storedModel.created)
                        obj.put("updated", storedModel.updated)
                        arr.put(obj)
                    }

                    // note. save into device
                    PreferencesManager(activity, Protocol.ACCOUNT_DATA).add(Protocol.ACCOUNT_LIST, arr.toString())
                }
            } catch (e: Exception) {e.printStackTrace()}
        }
    }
}