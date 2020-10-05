package com.rerekt.rekukler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.configure(block: RecyclerViewConfig.() -> Unit) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
        adapter = MultiBindingAdapter(it.bindersSet)
    }
}

fun RecyclerView.updateList(newList: List<Any>) {
    (adapter as? MultiBindingAdapter)?.apply {
        items = newList
        notifyDataSetChanged()
    }
}

class RecyclerViewConfig(
    private val context: Context
) {

    internal var bindersSet = listOf<ViewBinder<*>>()

    internal var layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    fun linearLayout(
        block: LinearLayoutManager.() -> Unit = {}
    ) { layoutManager = LinearLayoutManager(context).apply(block) }

    fun gridLayout(
        spansCount: Int = 1,
        block: GridLayoutManager.() -> Unit = {}
    ) { layoutManager = GridLayoutManager(context, spansCount).apply(block) }

    fun viewBinders(vararg viewBinder: ViewBinder<*>) {
        bindersSet = viewBinder.toList()
    }
}