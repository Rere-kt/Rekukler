package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class MultiBindingAdapter(
	vararg binders: ViewBinder<*, *>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<Any> = listOf()
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