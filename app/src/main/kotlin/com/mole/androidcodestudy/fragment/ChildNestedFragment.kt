package com.mole.androidcodestudy.fragment

import androidx.fragment.app.Fragment
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.FragmentChildNestedBinding
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
class ChildNestedFragment : Fragment(R.layout.fragment_child_nested){
    private val binding : FragmentChildNestedBinding by viewBinding()
}