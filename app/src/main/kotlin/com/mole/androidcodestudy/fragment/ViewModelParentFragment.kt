package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.FragmentViewModelParentBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.viewmodel.ParentFragmentViewModel

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/11/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class ViewModelParentFragment : BaseFragment(R.layout.fragment_view_model_parent) {
    private val binding by viewBinding<FragmentViewModelParentBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this)[ParentFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentPager.apply {
            adapter = NestedFragmentAdapter(this@ViewModelParentFragment)
        }
        viewModel.text.observe(viewLifecycleOwner) {
            binding.tvFromParentFragment.text = it
        }
    }

    inner class NestedFragmentAdapter(fragment: BaseFragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(p0: Int): Fragment {
            return ViewModelChildFragment.newInstance(p0)
        }

    }
}