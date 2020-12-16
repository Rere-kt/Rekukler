package com.rerekt.sample.ui.global.list

import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R
import com.rerekt.sample.databinding.ListItemBinding

data class Article(
    val id: Int,
    val title: String,
    val description: String
)

fun articlesBinder(
    onClick: (Article) -> Unit = {}
) = viewBinder<Article, ListItemBinding>(
    layoutResId = R.layout.list_item,
    binder = { ListItemBinding.bind(it) },
	isForItem = { it is Article },
    areItemsSame = { old, new -> old.id == new.id },
    areContentsSame = { old, new -> old == new }
) {
	bindView { data ->
        tvTitle.text = data.title
        tvDescription.text = data.description
        tvPosition.text = getString(R.string.position, position)
        itemView.setOnClickListener { onClick.invoke(data) }
	}
}
