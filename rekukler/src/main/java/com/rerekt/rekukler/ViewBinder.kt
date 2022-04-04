package com.rerekt.rekukler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewBinder<Type: Any, Binding: ViewBinding> (
        private val layoutResId: Int,
        val binder: (View) -> Binding,
        val isForItem: (item: Any) -> Boolean,
        val areItemsSame: (Type, Type) -> Boolean,
        val areContentsSame: (Type, Type) -> Boolean,
		val getChangePayload: (Type, Type) -> List<BasePayload<Type>>,
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
	fun bindViewHolder(
		viewHolder: RekuklerViewHolder<*, *>,
		item: Any,
		position: Int,
		payloads: List<Any>
	) {
		viewHolder.bind(item, position, (payloads as? List<List<Any>>).orEmpty())
    }

}

@Suppress("UNCHECKED_CAST")
open class RekuklerViewHolder<Type: Any, Binding: ViewBinding>(
	itemView: View,
	val binder: (View) -> Binding,
	private val holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

	private val holder = HolderBinder<Type, Binding>(this)
	private var binding: Binding? = null

	fun bind(
		item: Any,
		position: Int,
		payloads: List<List<Any>>
	) {
		val typePayloads = payloads.flatten().filterIsInstance<BasePayload<Type>>()

		holder.apply {
			if (binding == null || typePayloads.isEmpty()) {
				binding = binder.invoke(viewHolder.itemView)
				holderBinder(item as Type)
				bindingBlock.invoke(binding!!, item)
			} else {
				payloadHandlerBlock.invoke(binding!!, item as Type, typePayloads)
			}
			itemPosition = position
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
        noinline getChangePayload: (Type, Type) -> List<BasePayload<Type>> = { old, new -> listOf() },
		noinline binder: (View) -> Binder,
		noinline holderBinder: HolderBinder<Type, Binder>.(Type) -> Unit = {}
) = ViewBinder(
	layoutResId = layoutResId,
	binder = binder,
	isForItem = isForItem,
	areItemsSame = areItemsSame,
	areContentsSame = areContentsSame,
	getChangePayload = getChangePayload,
	holderBinder = holderBinder
)

class HolderBinder<Type: Any, Binding: ViewBinding>(
	val viewHolder: RecyclerView.ViewHolder
): ViewHolderManager {

	internal var bindingBlock: Binding.(Type) -> Unit  = {}
	internal var payloadHandlerBlock: Binding.(Type, List<BasePayload<Type>>) -> Unit  = { _, _ ->}
	internal var onDetachedFromWindow: Binding.() -> Unit = {}
	internal var onAttachedToWindow: Binding.() -> Unit = {}

	internal var itemPosition = 0

    override val itemView: View
        get() = viewHolder.itemView

    val position: Int
		get() = itemPosition

	fun bindView(bindingBlock: Binding.(Type) -> Unit) {
		this.bindingBlock = bindingBlock
	}

	fun handlePayload(payloadHandlerBlock: Binding.(Type, List<BasePayload<Type>>) -> Unit) {
		this.payloadHandlerBlock = payloadHandlerBlock
	}

	fun onDetachedFromWindow(b: Binding.() -> Unit) {
		this.onDetachedFromWindow = b
	}

	fun onAttachedToWindow(b: Binding.() -> Unit) {
		this.onAttachedToWindow = b
	}

}