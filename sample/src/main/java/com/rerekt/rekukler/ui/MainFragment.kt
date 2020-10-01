package com.rerekt.rekukler.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rerekt.rekukler.R
import com.rerekt.rekukler.configure

class MainFragment: Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        initRecycler(recyclerView)
    }

    private fun initRecycler(recyclerView: RecyclerView) {
        recyclerView.configure {
            linearLayout {
                reverseLayout = false
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

}