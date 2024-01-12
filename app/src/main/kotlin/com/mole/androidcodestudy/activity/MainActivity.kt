package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.mole.androidcodestudy.adapter.PageBean
import com.mole.androidcodestudy.adapter.PagesAdapter
import com.mole.androidcodestudy.databinding.ActivityMainBinding
import com.mole.androidcodestudy.di.HiltTestInterface
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    @Inject
    lateinit var hiltTestInterface: HiltTestInterface
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hiltTestInterface.print()

        binding.rvPages.apply {
            adapter = PagesAdapter(pages)
        }
    }


    override fun onResume() {
        super.onResume()
        hiltTestInterface.print()
    }

    private fun tryGC() {
        System.gc()
        Runtime.getRuntime().gc()
    }

    companion object {
        val pages: List<PageBean> = mapOf(
            "自定义View" to CustomViewTestActivity::class.java,
            "位置" to LocationActivity::class.java,
            "pickMedia" to PickMediaActivity::class.java,
            "委托" to KotlinDelegateActivity::class.java,
            "嵌套Coordinator" to NestedCoordinatorActivity::class.java,
            "动画" to AnimationActivity::class.java,
            "软键盘" to SoftInputActivity::class.java,
            "文件" to FileActivity::class.java,
            "TextView" to TextViewActivity::class.java,
            "协程" to CoroutineActivity::class.java

        ).toList()
    }
}