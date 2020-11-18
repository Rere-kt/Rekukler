package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MultiBindingAdapter(
        val bindersSet: List<ViewBinder<Any>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items: List<Any> = listOf()
	private val holders: MutableList<RecyclerView.ViewHolder> = mutableListOf()

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): RecyclerView.ViewHolder =
		checkNotNull(
			value = bindersSet[viewType].createViewHolder(parent),
			lazyMessage = { "Unnable to find ViewBinder for viewType $viewType" }
		)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
            getBinder(position).bindViewHolder(viewHolder, items[position], position)

    override fun getItemCount() = items.size

	// view type must be position of binder in bindersSet
	override fun getItemViewType(position: Int)
		= bindersSet.indexOfFirst { it.isForItem(items[position]) }

    private fun getBinder(position: Int): ViewBinder<*> {
        val item = items[position]
        return checkNotNull(
                value = bindersSet.find { it.isForItem(item) },
                lazyMessage = { "Unnable to find ViewBinder for ${item::class}" }
        )
    }
}