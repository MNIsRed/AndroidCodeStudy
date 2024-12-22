package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.activity.CoroutineActivity
import com.mole.androidcodestudy.activity.KotlinDelegateActivity
import com.mole.androidcodestudy.activity.LiveDataActivity
import com.mole.androidcodestudy.activity.ViewModelActivity
import com.mole.androidcodestudy.activity.library.LombokActivity
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentLibraryBinding
import com.mole.androidcodestudy.extension.viewBinding

class LibraryFragment : BaseFragment(R.layout.fragment_library) {

    private val binding: FragmentLibraryBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPages.apply {
            adapter = PagesAdapter(pages)
        }

        binding.buttonAboutLibraries.setOnClickListener {
            LibsBuilder()
                .start(requireContext()) // start the activity
        }

    }

    companion object {
        val pages: List<PageBean> = mapOf(
            "委托" to KotlinDelegateActivity::class.java,
            "协程" to CoroutineActivity::class.java,
            "LiveData" to LiveDataActivity::class.java,
            "ViewModel" to ViewModelActivity::class.java,
            "Lombok" to LombokActivity::class.java
        ).toList()
    }
}