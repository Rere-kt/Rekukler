package com.rerekt.rekukler

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewConfig(
    internal val context: Context
) {

    internal var itemDecorations: MutableList<RecyclerView.ItemDecoration> = mutableListOf()
    internal var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
    internal var itemTouchHelper: ItemTouchHelper? = null

}