package com.rerekt.rekukler

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding

fun RecyclerView.configure(
    customAdapter: ((List<ViewBinder<Any, ViewBinding>>) -> MultiBindingAdapter)? = null,
    block: RecyclerViewConfig.() -> Unit
) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
        adapter = customAdapter?.invoke(it.bindersSet) ?: MultiBindingAdapter(it.bindersSet)
		it._itemDecoration?.let { addItemDecoration(it) }
    }
}

fun RecyclerView.updateList(newList: List<Any>) {
    (adapter as? MultiBindingAdapter)?.apply {

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
}

fun <T> RecyclerView.getItems() = (adapter as? MultiBindingAdapter)?.items as? List<T>

class RecyclerViewConfig(
    private val context: Context
) {

    internal var bindersSet = listOf<ViewBinder<Any, ViewBinding>>()
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
        orientation: Int = DividerItemDecoration.VERTICAL,
		block: DividerItemDecoration.() -> Unit
    ) {
        itemDecoration(
            DividerItemDecoration(context, orientation).apply(block)
        )
    }

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

    fun viewBinders(vararg viewBinder: ViewBinder<*, *>) {
        bindersSet = viewBinder.toList() as List<ViewBinder<Any, ViewBinding>>
    }

}