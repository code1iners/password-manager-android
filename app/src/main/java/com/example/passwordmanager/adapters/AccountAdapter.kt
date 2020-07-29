package com.example.passwordmanager.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import com.example.passwordmanager.models.AccountModel

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.CustomViewHolder>() {
    companion object {
        val TAG = AccountAdapter::class.simpleName
    }

    private lateinit var context: Context
    private lateinit var accountList: ArrayList<AccountModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdapter.CustomViewHolder {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        val v = LayoutInflater.from(parent.context).inflate(R.layout.account_list_item, parent, false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(h: AccountAdapter.CustomViewHolder, p: Int) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        val m = accountList[p]

        applyView(h, m)
    }

    override fun getItemCount(): Int { return accountList.size }
    // note. expansion
    fun setList(list: ArrayList<AccountModel>) { this.accountList = list }
    fun setContext(context: Context) { this.context = context }

    private fun applyView(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        applyViewText(h, m)
        applyViewImage(h, m)
    }

    private fun applyViewImage(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)


    }

    private fun applyViewText(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

        h.accountListItem__header__title.text = m.title
        h.accountListItem__body__text__id.text = m.id
        h.accountListItem__body__text__pw.text = m.pw
    }

    inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // note. declared vars
        var accountListItem__header__title: TextView

        var accountListItem__body__image_thumbnail: ImageView
        var accountListItem__body__text__id: TextView
        var accountListItem__body__text__pw: TextView
        var accountListItem__body__option__modify: Button

        init {
            // note. assignment vars
            // note. header
            accountListItem__header__title = v.findViewById(R.id.accountListItem__header__title)
            // note. body
            accountListItem__body__image_thumbnail = v.findViewById(R.id.accountListItem__body__image_thumbnail)
            accountListItem__body__text__id = v.findViewById(R.id.accountListItem__body__text__id)
            accountListItem__body__text__pw = v.findViewById(R.id.accountListItem__body__text__pw)
            accountListItem__body__option__modify = v.findViewById(R.id.accountListItem__body__option__modify)

            // note. init listeners
            accountListItem__body__option__modify.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.accountListItem__body__option__modify -> {
                    Log.w(TAG, "accountListItem__option__modify_OnClick")

                }
            }
        }

    }
}