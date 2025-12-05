package com.mole.androidcodestudy.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEachIndexed
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityMainBinding
import com.mole.androidcodestudy.di.HiltTestInterface
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.fragment.SearchEntriesBottomSheet
import com.mole.androidcodestudy.fragment.LibraryFragment
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
        binding.actionSearch.setOnClickListener {
            SearchEntriesBottomSheet().show(supportFragmentManager, "search_entries")
        }

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment).let {
            it as? NavHostFragment
        }?.apply {
            navController.apply {
                graph = createGraph(startDestination = fragmentRoutes[0]) {
                    fragment<WidgetFragment>(fragmentRoutes[0]) {
                        label = fragmentRoutes[0]
                    }
                    fragment<SystemFragment>(fragmentRoutes[1]) {
                        label = fragmentRoutes[1]
                    }
                    fragment<LibraryFragment>(fragmentRoutes[2]) {
                        label = fragmentRoutes[2]
                    }
                }
            }.also { navController ->
                binding.bottomNavigation.setOnItemSelectedListener {
                    binding.bottomNavigation.menu.forEachIndexed { index, item ->
                        if (item.itemId == it.itemId) {
                            navController.navigate(fragmentRoutes[index])
                        }
                    }
                    true
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

    companion object{
        val fragmentRoutes = listOf("widget", "system", "library")
    }

}
