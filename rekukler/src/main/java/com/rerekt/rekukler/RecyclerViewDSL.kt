package com.rerekt.rekukler

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.*

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

fun RecyclerView.updateList(newList: List<Any>) {
    (adapter as? MultiBindingAdapter)?.apply {
        items = newList
    }
}

fun <T> RecyclerView.getItems() = (adapter as? MultiBindingAdapter)?.items as? List<T>

class RecyclerViewConfig(
    private val context: Context
) {

	internal var itemDecorations: MutableList<RecyclerView.ItemDecoration> = mutableListOf()

    internal var layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    internal var itemTouchHelper: ItemTouchHelper? = null

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

    fun itemTouchHelper(
        dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT,
        swipeFlags: Int = 0,
        onSwiped: (RecyclerView.ViewHolder, direction: Int) -> Unit = { _, _ -> },
        onMove: (RecyclerView.ViewHolder, RecyclerView.ViewHolder) -> Boolean = { _, _ -> true }
    ) = ItemTouchHelper(
            object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int = makeMovementFlags(dragFlags, swipeFlags)
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean =
                        onMove(viewHolder, target)
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    onSwiped.invoke(viewHolder, direction)
                }
            }
    ).apply { itemTouchHelper = this }

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
        itemDecorations.add(decoration)
    }

}