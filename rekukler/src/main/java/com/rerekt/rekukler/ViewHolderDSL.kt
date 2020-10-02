package com.rerekt.rekukler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderBinder<Type: Any> (
    private val layoutResId: Int,
    internal val isFor: (item: Type, adapterPos: Int) -> Boolean = { _, _ -> true },
    private val initBlock: ViewHolderConfig<Type>.() -> Unit
) {

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    fun bindViewHolder() {

    }

}

class ViewHolderConfig<Type>() {

    internal var _bind: (Type, View) -> Unit  = { _, _ -> }

    fun bind(block: (Type, View) -> Unit) {
        this._bind = block
    }

}