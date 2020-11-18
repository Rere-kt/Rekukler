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
        private val holderBinder: Holder<Type>.(Type) -> Unit,
) {

    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: Any, position: Int) {
		Holder<Type>()
			.apply {
				holderBinder(item as Type)
				_viewHolder = viewHolder
				itemPosition = position
				viewHolder.itemView.bindingBlock(item)
			}
    }
}

inline fun <reified Type: Any> viewBinder(
        @LayoutRes layoutResId: Int,
        noinline isForItem: (item: Any) -> Boolean = { it is Type },
        noinline areItemsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline areContentsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline holder: Holder<Type>.(Type) -> Unit = {}
) = ViewBinder(
        layoutResId = layoutResId,
        isForItem = isForItem,
        areItemsSame = areItemsSame,
        areContentsSame = areContentsSame,
		holderBinder = holder
)

class Holder<Type: Any> {

	internal var bindingBlock: View.(Type) -> Unit  = {}
	internal var itemPosition = 0
	internal var _viewHolder: RecyclerView.ViewHolder? = null

	val position: Int
		get() = itemPosition

	val viewHolder: RecyclerView.ViewHolder?
		get() = _viewHolder

	fun bindView(bindingBlock: View.(Type) -> Unit) {
		this.bindingBlock = bindingBlock
	}


}