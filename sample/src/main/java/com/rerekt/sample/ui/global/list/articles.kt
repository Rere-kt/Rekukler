package com.rerekt.sample.ui.global.list

import android.widget.TextView
import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R

fun articlesBinder(
    onClick: (Pair<String, String>) -> Unit = {}
) = viewBinder<Pair<String, String>>(
    layoutResId = R.layout.list_item,
    areItemsSame = { old, new -> old.first == new.first },
) {  data ->
    val (title, description) = data
    findViewById<TextView>(R.id.tv_title).text = title
    findViewById<TextView>(R.id.tv_description).text = description
    setOnClickListener { onClick.invoke(data) }
}
