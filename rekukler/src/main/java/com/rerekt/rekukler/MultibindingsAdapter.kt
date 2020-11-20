package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class MultiBindingAdapter(
        val bindersSet: List<ViewBinder<Any, ViewBinding>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items: List<Any> = listOf()

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