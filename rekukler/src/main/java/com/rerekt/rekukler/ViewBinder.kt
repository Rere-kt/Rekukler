package com.rerekt.rekukler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class ViewBinder<Type: Any> (
        private val layoutResId: Int,
        val isForItem: (item: Any) -> Boolean,
        val areItemsSame: (Type, Type) -> Boolean,
        val areContentsSame: (Type, Type) -> Boolean,
        private val bind: View.(Type) -> Unit,
) {

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: Any) {
        viewHolder.itemView.bind(item as Type)
    }
}

inline fun <reified Type: Any> viewBinder(
        @LayoutRes layoutResId: Int,
        noinline isForItem: (item: Any) -> Boolean = { it is Type },
        noinline areItemsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline areContentsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline bind: View.(Type) -> Unit = {}
) = ViewBinder<Type>(
        layoutResId = layoutResId,
        isForItem = isForItem,
        areItemsSame = areItemsSame,
        areContentsSame = areContentsSame,
        bind = bind
)