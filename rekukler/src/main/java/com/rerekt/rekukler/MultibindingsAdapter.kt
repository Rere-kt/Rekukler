package com.rerekt.rekukler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MultiBindingAdapter(
        val bindersSet: List<ViewBinder<Any>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var items: List<Any> = listOf()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            position: Int
    ) = getBinder(position).createViewHolder(parent)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) =
            getBinder(position).bindViewHolder(viewHolder, items[position])

    override fun getItemCount() = items.size

    private fun getBinder(position: Int): ViewBinder<*> {
        val item = items[position]
        return checkNotNull(
                value = bindersSet.find { it.isForItem(item) },
                lazyMessage = { "Unnable to find ViewBinder for ${item::class}" }
        )
    }
}