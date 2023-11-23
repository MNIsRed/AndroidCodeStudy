package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.mole.androidcodestudy.databinding.ActivityNestedCoordinatorBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.fragment.ChildNestedFragment
import com.mole.androidcodestudy.fragment.NestedCoordinatorFragment

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/11/23
 *     desc   : 调试嵌套的NestedScrollView
 *     version: 1.0
 * </pre>
 */
class NestedCoordinatorActivity : BaseActivity(){
    private val binding : ActivityNestedCoordinatorBinding by viewBinding(ActivityNestedCoordinatorBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        binding.viewPager.apply {
            adapter = object: FragmentPagerAdapter(supportFragmentManager){
                private val mFragment: NestedCoordinatorFragment by lazy { NestedCoordinatorFragment() }
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