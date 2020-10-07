package com.rerekt.sample.ui.global.list

import android.widget.TextView
import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R

data class Article(
    val id: Int,
    val title: String,
    val description: String
)

fun articlesBinder(
    onClick: (Article) -> Unit = {}
) = viewBinder<Article>(
    layoutResId = R.layout.list_item,
    areItemsSame = { old, new -> old.id == new.id },
) { data ->
    findViewById<TextView>(R.id.tv_title).text = data.title
    findViewById<TextView>(R.id.tv_description).text = data.description
    setOnClickListener { onClick.invoke(data) }
}
