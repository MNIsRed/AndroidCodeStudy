package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentLibraryBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.setSingleItemDecoration
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.home.HomeEntries
import com.mole.androidcodestudy.widget.decoration.GridSpacingItemDecoration

class LibraryFragment : BaseFragment(R.layout.fragment_library) {

    private val binding: FragmentLibraryBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPages.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setSingleItemDecoration(
                GridSpacingItemDecoration(
                    spanCount = 3,
                    spacing = 12f.dp2px().toInt()
                )
            )
            adapter = PagesAdapter(pages)
        }

        binding.buttonAboutLibraries.setOnClickListener {
            LibsBuilder()
                .start(requireContext()) // start the activity
        }

    }

    companion object {
        val pages: List<PageBean> = HomeEntries.libraryEntries.map { PageBean(it.title, it.activity) }
    }
}
