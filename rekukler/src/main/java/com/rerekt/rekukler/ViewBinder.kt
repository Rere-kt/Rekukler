package com.rerekt.rekukler

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewBinder<Type: Any, Binding: ViewBinding> (
        private val layoutResId: Int,
        val binder: (View) -> Binding,
        val isForItem: (item: Any) -> Boolean,
        val areItemsSame: (Type, Type) -> Boolean,
        val areContentsSame: (Type, Type) -> Boolean,
        private val holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit
) {

    fun createViewHolder(parent: ViewGroup): RekuklerViewHolder<Type, Binding> {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
		return RekuklerViewHolder(
			itemView = itemView,
			binder = binder,
			holderBinder = holderBinder
		)
    }

    @Suppress("UNCHECKED_CAST")
	fun bindViewHolder(viewHolder: RekuklerViewHolder<*, *>, item: Any, position: Int) {
		viewHolder.bind(item, position)
    }
}

@Suppress("UNCHECKED_CAST")
open class RekuklerViewHolder<Type: Any, Binding: ViewBinding>(
	itemView: View,
	val binder: (View) -> Binding,
	private val holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit
) : RecyclerView.ViewHolder(itemView) {

	internal val holder = HolderBinder<Type, Binding>(this)
	private var binding: Binding? = null

	fun bind(item: Any, position: Int) {
		holder.apply {
			binding = binder.invoke(viewHolder.itemView)
			holderBinder(item as Type)
			itemPosition = position
			bindingBlock.invoke(binding!!, item)
		}
	}

	fun onDetachedFromWindow() {
		binding?.let {
			holder.onDetachedFromWindow.invoke(it)
		}
	}

	fun onAttachedToWindow() {
		binding?.let {
			holder.onAttachedToWindow.invoke(it)
		}
	}

}

inline fun <reified Type: Any, Binder: ViewBinding> viewBinder(
        @LayoutRes layoutResId: Int,
        noinline isForItem: (item: Any) -> Boolean = { it is Type },
        noinline areItemsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline areContentsSame: (Type, Type) -> Boolean = { old, new -> old == new },
        noinline binder: (View) -> Binder,
        noinline holderBinder: HolderBinder<Type, Binder>.(Type) -> Unit = {}
) = ViewBinder(
        layoutResId = layoutResId,
        binder = binder,
        isForItem = isForItem,
        areItemsSame = areItemsSame,
        areContentsSame = areContentsSame,
		holderBinder = holderBinder
)

class HolderBinder<Type: Any, Binding: ViewBinding>(
	val viewHolder: RecyclerView.ViewHolder
) {

	internal var bindingBlock: Binding.(Type) -> Unit  = {}
	internal var onDetachedFromWindow: Binding.() -> Unit = {}
	internal var onAttachedToWindow: Binding.() -> Unit = {}

	internal var itemPosition = 0

    val itemView: View
        get() = viewHolder.itemView

    val position: Int
		get() = itemPosition

	fun bindView(bindingBlock: Binding.(Type) -> Unit) {
		this.bindingBlock = bindingBlock
	}

	fun onDetachedFromWindow(b: Binding.() -> Unit) {
		this.onDetachedFromWindow = b
	}

	fun onAttachedToWindow(b: Binding.() -> Unit) {
		this.onAttachedToWindow = b
	}

	fun getString(@StringRes resId: Int): String = itemView.context.getString(resId)
	fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = itemView.context.getString(resId, *formatArgs)
	fun getDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(itemView.context, resId)
    fun getColor(@ColorRes resId: Int): Int = itemView.context.getColor(resId)
	fun getColorStateList(@ColorRes resId: Int): ColorStateList? = ContextCompat.getColorStateList(itemView.context, resId)
	fun <V: View> findViewById(@IdRes resId: Int): V = itemView.findViewById(resId)
	fun setOnClickListener(l: (View) -> Unit) {
		itemView.setOnClickListener { l.invoke(it) }
	}

}