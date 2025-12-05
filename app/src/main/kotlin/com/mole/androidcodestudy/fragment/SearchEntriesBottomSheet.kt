package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.FragmentSearchEntriesBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.start
import com.mole.androidcodestudy.home.HomeEntries
import com.mole.androidcodestudy.widget.decoration.GridSpacingItemDecoration

class SearchEntriesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchEntriesBinding? = null
    private val binding get() = _binding!!

    private val allEntries = HomeEntries.allEntries
    private val adapter = PagesAdapter(
        data = allEntries.map { PageBean(it.title, it.activity) },
        onItemClick = { entry ->
            requireActivity().start(entry.second)
            dismissAllowingStateLoss()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEntriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupSearch()
    }

    private fun setupList() {
        binding.rvResults.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    GridSpacingItemDecoration(
                        spanCount = 2,
                        spacing = 12f.dp2px().toInt()
                    )
                )
            }
            adapter = this@SearchEntriesBottomSheet.adapter
        }
    }

    private fun setupSearch() {
        filter("") // 初始展示全部
        binding.etSearch.requestFocus()
        binding.etSearch.addTextChangedListener {
            filter(it?.toString().orEmpty())
        }
    }

    private fun filter(keyword: String) {
        val trimmed = keyword.trim()
        val filtered = if (trimmed.isEmpty()) {
            allEntries
        } else {
            allEntries.filter { it.title.contains(trimmed, ignoreCase = true) }
        }
        adapter.submit(filtered.map { PageBean(it.title, it.activity) })
        binding.tvEmpty.isVisible = filtered.isEmpty()
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { sheet ->
            sheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
            peekHeight = resources.displayMetrics.heightPixels
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
