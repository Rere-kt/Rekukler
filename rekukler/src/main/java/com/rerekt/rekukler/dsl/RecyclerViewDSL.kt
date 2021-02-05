package com.rerekt.rekukler.dsl

import androidx.recyclerview.widget.*
import com.rerekt.rekukler.MultiBindingAdapter
import com.rerekt.rekukler.RecyclerViewConfig

fun RecyclerView.configure(
    adapter: MultiBindingAdapter,
    block: RecyclerViewConfig.() -> Unit = {}
) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
        this.adapter = adapter
        it.itemDecorations.forEach { addItemDecoration(it) }
        it.itemTouchHelper?.attachToRecyclerView(this)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.getItems() = (adapter as? MultiBindingAdapter)?.items as? List<T>