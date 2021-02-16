package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.util.*

/**
 * Main adapter class, which implements androidx.RecyclerView.
 * It's open so we can inherit and create own custom MultibindingAdapter.
 * @param binders must contains vararg of viewBinders with various viewType
*/

open class MultiBindingAdapter(
	vararg binders: ViewBinder<*, *>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	/**
	 * Items which would be in RV list.
	 * Should be used to update [MultiBindingAdapter] items
	 */
    var items: List<Any> = listOf()
		set(value) {
			updateList(value)
			field = value
		}

	@Suppress("UNCHECKED_CAST")
	val bindersSet = binders.toList() as List<ViewBinder<Any, ViewBinding>>

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder =
		checkNotNull(
			value = bindersSet.getOrNull(viewType)?.createViewHolder(parent),
			lazyMessage = { "Unnable to find ViewBinder for viewType $viewType" }
		)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
    	getBinder(position).bindViewHolder(
			viewHolder = viewHolder as RekuklerViewHolder<*, *>,
			position = position,
			item = items[position]
    	)

	private fun getBinder(position: Int): ViewBinder<*, *> {
		val item = items[position]
		return checkNotNull(
			value = bindersSet.find { it.isForItem(item) },
			lazyMessage = { "Unnable to find ViewBinder for ${item::class}" }
		)
	}

	override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
		(holder as RekuklerViewHolder<*, *>).onDetachedFromWindow()
		super.onViewDetachedFromWindow(holder)
	}

	override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
		(holder as RekuklerViewHolder<*, *>).onAttachedToWindow()
		super.onViewAttachedToWindow(holder)
	}

    override fun getItemCount() = items.size

	/**
	 * Updating list using DiffUtil, can be called only in [items] setter
	 */
	private fun updateList(newList: List<Any>) {
		DiffUtil.calculateDiff(
			object : DiffUtil.Callback() {
				override fun getOldListSize() = items.size
				override fun getNewListSize() = newList.size
				override fun areItemsTheSame(old: Int, new: Int) =
					kotlin.runCatching {
						bindersSet.find { it.isForItem(items[old]) }?.areItemsSame?.invoke(
							items[old],
							newList[new]
						) ?: false
					}.getOrElse { false }

				override fun areContentsTheSame(old: Int, new: Int) =
					kotlin.runCatching {
						bindersSet.find { it.isForItem(items[old]) }?.areContentsSame?.invoke(
							items[old],
							newList[new]
						) ?: false
					}.getOrElse { false }
			}
		).dispatchUpdatesTo(this)
	}

	// view type must be position of binder in bindersSet
	override fun getItemViewType(position: Int)
		= bindersSet.indexOfFirst { it.isForItem(items[position]) }

	/**
	 * Function for moving items by their positions
	 * @param fromPosition - current position of item
	 * @param toPosition - target position
	 */
	fun moveItem(fromPosition: Int, toPosition: Int): Boolean {
		val swappedList = mutableListOf<Any>().apply { items.forEach(::add) }
		if (fromPosition < toPosition) {
			for (i in fromPosition until toPosition) {
				Collections.swap(swappedList, i, i + 1)
			}
		} else {
			for (i in fromPosition downTo toPosition + 1) {
				Collections.swap(swappedList, i, i - 1)
			}
		}
		items = swappedList
		return true
	}
}