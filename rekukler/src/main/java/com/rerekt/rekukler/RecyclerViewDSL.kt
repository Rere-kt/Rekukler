package com.rerekt.rekukler

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.*

fun RecyclerView.configure(block: RecyclerViewConfig.() -> Unit) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
        adapter = MultiBindingAdapter(it.bindersSet)
    }
}

fun RecyclerView.updateList(newList: List<Any>) {
    (adapter as? MultiBindingAdapter)?.apply {

        DiffUtil.calculateDiff(object: DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(old: Int, new: Int) =
                bindersSet.find { it.isForItem(items[old]) }?.areItemsSame?.invoke(items[old], newList[new]) ?: false
            override fun areContentsTheSame(old: Int, new: Int) =
                bindersSet.find { it.isForItem(items[old]) }?.areContentsSame?.invoke(items[old], newList[new]) ?: false
        }).dispatchUpdatesTo(this)

        items = newList
    }
}

class RecyclerViewConfig(
    private val context: Context
) {

    internal var bindersSet = listOf<ViewBinder<Any>>()
    internal var _itemDecoration: RecyclerView.ItemDecoration? = null

    internal var layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    fun linearLayout(
        block: LinearLayoutManager.() -> Unit = {}
    ) { layoutManager = LinearLayoutManager(context).apply(block) }

    fun gridLayout(
        spansCount: Int = 1,
        block: GridLayoutManager.() -> Unit = {}
    ) { layoutManager = GridLayoutManager(context, spansCount).apply(block) }

    fun dividerItemDecoration(
        size: Int,
        orientation: Int = DividerItemDecoration.VERTICAL
    ) {
        itemDecoration(
            DividerItemDecoration(context, orientation).apply {
                setDrawable(
                    Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                        .toDrawable(context.resources)
                )
            }
        )
    }

    fun itemDecoration(decoration: RecyclerView.ItemDecoration) {
        _itemDecoration = decoration
    }

    fun viewBinders(vararg viewBinder: ViewBinder<*>) {
        bindersSet = viewBinder.toList() as List<ViewBinder<Any>>
    }

}