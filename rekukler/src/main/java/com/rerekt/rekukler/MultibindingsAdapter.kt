package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class MultiBindingAdapter(
	vararg binders: ViewBinder<*, *>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Any> = listOf()
		set(value) {
			updateList(items)
			field = value
		}
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

    override fun getItemCount() = items.size

	internal fun updateList(newList: List<Any>) {
		DiffUtil.calculateDiff(object: DiffUtil.Callback() {
			override fun getOldListSize() = items.size
			override fun getNewListSize() = newList.size
			override fun areItemsTheSame(old: Int, new: Int) =
				kotlin.runCatching { bindersSet.find { it.isForItem(items[old]) }?.areItemsSame?.invoke(items[old], newList[new]) ?: false }.getOrElse { false }
			override fun areContentsTheSame(old: Int, new: Int) =
				kotlin.runCatching { bindersSet.find { it.isForItem(items[old]) }?.areContentsSame?.invoke(items[old], newList[new]) ?: false }.getOrElse { false }
		}).dispatchUpdatesTo(this)

		items = newList
	}

	// view type must be position of binder in bindersSet
	override fun getItemViewType(position: Int)
		= bindersSet.indexOfFirst { it.isForItem(items[position]) }

    private fun getBinder(position: Int): ViewBinder<*, *> {
        val item = items[position]
        return checkNotNull(
                value = bindersSet.find { it.isForItem(item) },
                lazyMessage = { "Unnable to find ViewBinder for ${item::class}" }
        )
    }
}