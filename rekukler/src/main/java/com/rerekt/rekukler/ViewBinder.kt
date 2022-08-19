package com.rerekt.rekukler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ViewBinder<Type: Any, Binding: ViewBinding> (
        val binder: (layoutInflater: LayoutInflater, parent: ViewGroup) -> Binding,
        val isForItem: (item: Any) -> Boolean,
        val areItemsSame: (Type, Type) -> Boolean,
        val areContentsSame: (Type, Type) -> Boolean,
		val getChangePayload: (Type, Type) -> List<BasePayload<Type>>,
        private val holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit,
		private val layoutInflater: (parent: ViewGroup) -> LayoutInflater
) {

    fun createViewHolder(parent: ViewGroup): RekuklerViewHolder<Type, Binding> {
		val binding = binder(layoutInflater(parent), parent)

		return RekuklerViewHolder(
			binding = binding,
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
	private val binding: Binding,
	private val holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

	private val holder = HolderBinder<Type, Binding>(this)

	fun bind(
		item: Any,
		position: Int,
		payloads: List<List<Any>>
	) {
		val typePayloads = payloads.flatten().filterIsInstance<BasePayload<Type>>()

		holder.apply {
			if (typePayloads.isEmpty()) {
				holderBinder(item as Type)
				bindingBlock.invoke(binding, item)
			} else {
				payloadHandlerBlock.invoke(binding, item as Type, typePayloads)
			}
			itemPosition = position
		}
	}

	fun onDetachedFromWindow() {
		holder.onDetachedFromWindow.invoke(binding)
	}

	fun onAttachedToWindow() {
		holder.onAttachedToWindow.invoke(binding)
	}

}

inline fun <reified Type: Any, Binding: ViewBinding> viewBinder(
    noinline isForItem: (item: Any) -> Boolean = { it is Type },
    noinline areItemsSame: (Type, Type) -> Boolean = { old, new -> old == new },
    noinline areContentsSame: (Type, Type) -> Boolean = { old, new -> old == new },
    noinline getChangePayload: (Type, Type) -> List<BasePayload<Type>> = { old, new -> listOf() },
	noinline binder: (layoutInflater: LayoutInflater, parent: ViewGroup) -> Binding,
	noinline layoutInflater: (parent: ViewGroup) -> LayoutInflater = { parent -> LayoutInflater.from(parent.context) },
	noinline holderBinder: HolderBinder<Type, Binding>.(Type) -> Unit = {},
) = ViewBinder(
	binder = binder,
	isForItem = isForItem,
	areItemsSame = areItemsSame,
	areContentsSame = areContentsSame,
	getChangePayload = getChangePayload,
	holderBinder = holderBinder,
	layoutInflater = layoutInflater
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