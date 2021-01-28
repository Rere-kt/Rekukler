package com.rerekt.sample.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rerekt.rekukler.MultiBindingAdapter
import com.rerekt.rekukler.configure
import com.rerekt.rekukler.updateList
import com.rerekt.sample.R
import com.rerekt.sample.databinding.FragmentMainBinding
import com.rerekt.sample.ui.global.dip
import com.rerekt.sample.ui.global.list.*

class ListFragment: Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    private val adapter by lazy {
        MultiBindingAdapter(
            articlesBinder { println("Click from Article item") },
            loadingBinder()
        )
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initRecycler()
        postDelayedListUpdate()
    }

    @ExperimentalStdlibApi
    private fun postDelayedListUpdate() {
        adapter.items = buildList {
            addAll(
                (0..10).map {
                    Article(
                        id = it,
                        title = "Title#$it",
                        description = "Description#$it"
                    )
                }
            )
            add(Loading)
        }
    }

    private fun initRecycler() {
        binding.rvArticles.configure(adapter) {
            linearLayout {
                reverseLayout = false
                orientation = LinearLayoutManager.VERTICAL
            }
			dividerItemDecoration(
				size = 2.dip(resources).toInt()
			)
        }
    }

}