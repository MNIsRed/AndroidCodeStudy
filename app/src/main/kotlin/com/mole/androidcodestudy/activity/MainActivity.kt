package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityMainBinding
import com.mole.androidcodestudy.di.HiltTestInterface
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.fragment.SystemFragment
import com.mole.androidcodestudy.fragment.WidgetFragment
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
        binding

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment).let {
            it as? NavHostFragment
        }?.apply {
            navController.apply {
                graph = createGraph(startDestination = "widget") {
                    fragment<WidgetFragment>("widget") {
                        label = "widget"
                    }
                    fragment<SystemFragment>("system") {
                        label = "system"
                    }
                }
            }
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
}