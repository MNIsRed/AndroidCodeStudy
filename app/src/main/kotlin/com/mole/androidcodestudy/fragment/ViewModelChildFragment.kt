package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.FragmentViewModelChildBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.viewmodel.ParentFragmentViewModel

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/11/29
 *     desc   : 实验可以通过ViewModelProvider.ViewModelStoreOwner获取共享的 ViewModel
 *     version: 1.0
 * </pre>
 */
class ViewModelChildFragment private constructor() :
    BaseFragment(R.layout.fragment_view_model_child) {
    private val binding by viewBinding<FragmentViewModelChildBinding>()
    private val parentViewModel by lazy {
        ViewModelProvider(requireParentFragment())[ParentFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt("position")
        binding.button.setOnClickListener {
            parentViewModel.changeText("修改文案的 ChildFragment：${position}")
        }
    }

    companion object {
        fun newInstance(position: Int): ViewModelChildFragment {
            val args = Bundle()
            args.putInt("position", position)
            val fragment = ViewModelChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
}