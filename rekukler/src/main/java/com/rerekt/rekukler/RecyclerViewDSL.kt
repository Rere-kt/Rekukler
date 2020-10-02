package com.rerekt.rekukler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

fun RecyclerView.configure(block: RecyclerViewConfig.() -> Unit) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
    }
}

class RecyclerViewConfig(
    private val context: Context
) {

    var bindersSet = mutableSetOf<Pair<KClass<*>, ViewHolderBinder<*>>>()
    internal var layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    fun linearLayout(
        block: LinearLayoutManager.() -> Unit = {}
    ) { layoutManager = LinearLayoutManager(context).apply(block) }

    fun gridLayout(
        spansCount: Int = 1,
        block: GridLayoutManager.() -> Unit = {}
    ) { layoutManager = GridLayoutManager(context, spansCount).apply(block) }

    inline fun <reified Type : Any> viewBinder(
        layoutResId: Int,
        noinline isFor: (item: Type, adapterPosition: Int) -> Boolean = { _, _ -> true },
        noinline block: ViewHolderConfig<Type>.() -> Unit
    ) {
        bindersSet.add(
            Type::class to ViewHolderBinder(
                layoutResId = layoutResId,
                isFor = isFor,
                initBlock = block
            )
        )
    }

    inline fun <reified Type: Any> viewBinder(
        viewBinder: ViewHolderBinder<Type>
    ) { bindersSet.add(Type::class to viewBinder) }

}