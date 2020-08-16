package com.example.passwordmanager.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpers.GlideOptions
import com.example.passwordmanager.R
import com.example.passwordmanager.helpers.ItemTouchHelperListener
import com.example.passwordmanager.models.AccountModel
import timber.log.Timber
import kotlin.random.Random

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.CustomViewHolder>(), ItemTouchHelperListener {
    private lateinit var context: Context
    private lateinit var accounts: ArrayList<AccountModel>
    private lateinit var clicker: AccountClickListener
    private lateinit var backgrounds:Array<Drawable>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdapter.CustomViewHolder {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        val v = LayoutInflater.from(parent.context).inflate(R.layout.account_list_item, parent, false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(h: AccountAdapter.CustomViewHolder, p: Int) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        val m = accounts[p]

        applyView(h, m)
    }

    override fun getItemCount(): Int { return accounts.size }
    // note. expansion
    fun setList(list: ArrayList<AccountModel>) { this.accounts = list }
    fun setContext(context: Context) { this.context = context }
    fun setBackgrounds(array: Array<Drawable>) { this.backgrounds = array }
    private fun applyView(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)

        applyContainer(h)
        applyViewText(h, m)
        applyViewImage(h, m)
    }

    private fun applyContainer(h: CustomViewHolder) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val idx = Random.nextInt(backgrounds.size - 1)
            h.accountListItem__container.background = backgrounds[idx]
            Timber.i("idx:$idx")
            when (idx) {
                0 -> { }
                1 -> { }
                2 -> { }
                3 -> { }
                4 -> { }
                5 -> { }
                6 -> { }
                7 -> { }
                8 -> { }
                9 -> { }
                else -> {
                    Timber.d("Nothing")
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun applyViewImage(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Timber.e("icon:${m.icon}")
            // note. account thumbnail
            if (m.icon.isNullOrEmpty()) Glide.with(context).load(context.resources.getDrawable(R.drawable.image_006)).apply(GlideOptions.centerCropAndRadius(16)).into(h.accountListItem__body__image_thumbnail)
            else Glide.with(context).load(m.icon).apply(GlideOptions.centerCropAndRadius(16)).into(h.accountListItem__body__image_thumbnail)

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun applyViewText(h: AccountAdapter.CustomViewHolder, m: AccountModel) {
        Timber.w( object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. body
            h.accountListItem__header__title.text = m.title
            h.accountListItem__body__text__id.text = m.id
            h.accountListItem__body__text__pw.text = m.pw
            // note. footer
            h.accountListItem__footer__hint.text = m.hint

        } catch (e: Exception) {e.printStackTrace()}
    }

    inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // note. declared vars
        var accountListItem__container: RelativeLayout
        var accountListItem__header__title: TextView

        var accountListItem__body__image_thumbnail: ImageView
        var accountListItem__body__text__id: TextView
        var accountListItem__body__text__pw: TextView
        var accountListItem__footer__hint: TextView
        var accountListItem__body__option__update: ImageButton
        var accountListItem__body__option__delete: ImageButton

        // note. etc..
        var customViewHolderContext: CustomViewHolder = this

        init {
            // note. assignment vars
            accountListItem__container = v.findViewById(R.id.accountListItem__container)
            // note. header
            accountListItem__header__title = v.findViewById(R.id.accountListItem__header__title)
            // note. body
            accountListItem__body__image_thumbnail = v.findViewById(R.id.accountListItem__body__image_thumbnail)
            accountListItem__body__text__id = v.findViewById(R.id.accountListItem__body__text__id)
            accountListItem__body__text__pw = v.findViewById(R.id.accountListItem__body__text__pw)
            accountListItem__footer__hint = v.findViewById(R.id.accountListItem__footer__hint)
            accountListItem__body__option__update = v.findViewById(R.id.accountListItem__body__option__update)
            accountListItem__body__option__delete = v.findViewById(R.id.accountListItem__body__option__delete)

            // note. init listeners
            accountListItem__body__option__update.setOnClickListener(this)
            accountListItem__body__option__delete.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            try {
                val p = adapterPosition
                val m = accounts[p]
                when (v.id) {
                    R.id.accountListItem__body__option__update -> { clicker.accountUpdate(p, m) }
                    R.id.accountListItem__body__option__delete -> { clicker.accountDelete(customViewHolderContext, p, m) }
                }
            } catch (e: Exception) {e.printStackTrace()}
        }
    }

    fun setAccountClickListener(listener: AccountClickListener) {
        this.clicker = listener
    }

    interface AccountClickListener {
        fun accountUpdate(p: Int, m: AccountModel)
        fun accountDelete(h: CustomViewHolder, p: Int, m: AccountModel)
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("from_position:$from_position, to_position:$to_position")

        val account = accounts[from_position]
        accounts.removeAt(from_position)
        accounts.add(to_position, account)

        notifyItemMoved(from_position, to_position)
        return true
    }

    override fun onItemSwipe(position: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("position:$position")
        accounts.removeAt(position)
        notifyItemRemoved(position)
    }
}