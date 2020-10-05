package com.rerekt.sample.ui.list

import android.widget.TextView
import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R

fun articlesBinder(
    onClick: (Pair<String, String>) -> Unit = {}
) = viewBinder<Pair<String, String>>(R.layout.list_item) {  data ->
    val (title, description) = data
    findViewById<TextView>(R.id.tv_title).text = title
    findViewById<TextView>(R.id.tv_description).text = description
    setOnClickListener { onClick.invoke(data) }
}
