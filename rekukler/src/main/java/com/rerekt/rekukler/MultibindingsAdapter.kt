package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

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
		get() = mutableItems

	private var mutableItems: MutableList<Any> = mutableListOf()

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
            getBinder(position).bindViewHolder(viewHolder, items[position], position)

	private fun getBinder(position: Int): ViewBinder<*, *> {
		val item = items[position]
		return checkNotNull(
			value = bindersSet.find { it.isForItem(item) },
			lazyMessage = { "Unnable to find ViewBinder for ${item::class}" }
		)
	}

    override fun getItemCount() = items.size

	/**
	 * Updating list using DiffUtil, can be called only in [items] setter
	 */
	private fun updateList(newList: List<Any>) {
		mutableItems = newList.toMutableList()
		DiffUtil.calculateDiff(object : DiffUtil.Callback() {
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
		}).dispatchUpdatesTo(this)
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
		if (fromPosition < toPosition) {
			for (i in fromPosition until toPosition) {
				mutableItems[i] = mutableItems.set(i + 1, mutableItems[i]);
			}
		} else {
			for (i in  toPosition + 1 downTo fromPosition) {
				mutableItems[i] = mutableItems.set(i - 1, mutableItems[i]);
			}
		}
		notifyItemMoved(fromPosition, toPosition)
		return true
	}
}