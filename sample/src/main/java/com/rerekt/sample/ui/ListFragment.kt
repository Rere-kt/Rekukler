package com.rerekt.sample.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rerekt.rekukler.MultiBindingAdapter
import com.rerekt.rekukler.dsl.configure
import com.rerekt.rekukler.dsl.itemDecoration
import com.rerekt.rekukler.dsl.itemTouchHelper
import com.rerekt.rekukler.dsl.linearLayout
import com.rerekt.rekukler.utils.MarginDividerItemDecoration
import com.rerekt.sample.R
import com.rerekt.sample.databinding.FragmentMainBinding
import com.rerekt.sample.ui.global.dip
import com.rerekt.sample.ui.global.list.*

class ListFragment: Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    private val articlesAdapter by lazy {
        MultiBindingAdapter(
            articlesBinder(::onArticleClick),
            loadingBinder()
        )
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initRecycler()
        fillAdapterItems()
    }

    @ExperimentalStdlibApi
    private fun fillAdapterItems() {
        Handler(Looper.getMainLooper()).postDelayed(
            { articlesAdapter.items = buildList {
                addAll(
                    (0..20).map {
                        Article(
                            id = it,
                            title = "Title#$it",
                            description = "Description#$it"
                        )
                    }
                )
                add(Loading)
            } }, 1000
        )
    }

    private fun initRecycler() {
        binding.rvArticles.apply {
            configure(articlesAdapter) {
                linearLayout {
                    orientation = RecyclerView.VERTICAL
                }
                itemDecoration(
                    MarginDividerItemDecoration(
                        dividerLineWidth = 1.dip(resources),
                        dividerLineMargin = 12.dip(resources),
                        dividerLineColorRes = R.color.black
                    )
                )
                itemTouchHelper()
            }
        }
    }

    private fun onArticleClick(article: Article) {
        articlesAdapter.items = articlesAdapter.items.map {
            if (it is Article && it.id == article.id) {
                it.copy(
                    description = it.description + " + "
                )
            } else {
                it
            }
        }

        println("Click from Article item")
    }

}