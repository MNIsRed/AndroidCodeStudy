package com.mole.androidcodestudy.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.google.android.material.button.MaterialButton
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.FragmentNestedCoordinatorBinding
import com.mole.androidcodestudy.extension.dp2px
import com.mole.androidcodestudy.extension.start
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
        binding.rcv.apply {
            val horizonDivide = 15f.dp2px().toInt()
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
            adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return ViewHolder(MaterialButton(parent.context).apply {
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT
                        ).apply {
                            marginStart = horizonDivide
                            marginEnd = horizonDivide
                        }
                        textSize = 15f
                    })
                }

                override fun getItemCount(): Int {
                    return 5
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    (holder.itemView as? MaterialButton)?.apply {
                        text = "第${position}个按钮"
                    }
                }

            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}