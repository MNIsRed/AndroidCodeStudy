package com.mole.androidcodestudy.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.FragmentNestedCoordinatorBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class NestedCoordinatorFragment : Fragment(R.layout.fragment_nested_coordinator){
    private val binding : FragmentNestedCoordinatorBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.apply {
            adapter = object:FragmentPagerAdapter(childFragmentManager){
                private val mFragment: ChildNestedFragment by lazy { ChildNestedFragment() }
                override fun getCount(): Int {
                    return 1
                }

                override fun getItem(position: Int): Fragment {
                    return mFragment
                }

            }
        }
    }
}