package com.mole.androidcodestudy.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mole.androidcodestudy.di.HiltTestInterface
import com.mole.androidcodestudy.viewmodel.MainViewModel
import com.mole.androidcodestudy.R
import com.mole.androidcodestudy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding : ActivityMainBinding
    @Inject
    lateinit var hiltTestInterface: HiltTestInterface
    private lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setClick()
        viewModel.pet.observe(this){
            findViewById<Button>(R.id.click_button).text = it?.data?.name?:"null"
        }
        hiltTestInterface.print()
        autoTransitionTest()

        binding.showSoftInput.setOnClickListener {
            softInputMethodTest()
        }

        binding.showPickMedia.setOnClickListener {
            PickMediaActivity.start(this)
        }

        binding.showDelegateActivity.setOnClickListener {
            KotlinDelegateActivity.start(this)
        }
    }

    private fun softInputMethodTest(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
    }


    private fun setClick(){
        findViewById<Button>(R.id.click_button).setOnClickListener{
            viewModel.updatePet()
        }
        findViewById<Button>(R.id.file_button).setOnClickListener {
            tryFileOutput()
        }
        binding.goAnother.setOnClickListener {
            CustomViewTestActivity.start(this)
        }
    }

    private fun tryFileOutput(){
        try {
            val output = openFileOutput("data", Context.MODE_APPEND)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.write("click Button At ${LocalDateTime.now()}\n")
                }
            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext,"写入文件异常",Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        hiltTestInterface.print()
    }

    private fun tryGC(){
        System.gc()
        Runtime.getRuntime().gc()
    }

    private fun autoTransitionTest(){
        val autoTransition = AutoTransition()
        autoTransition.startDelay = 500
        autoTransition.duration = 2000
        autoTransition.interpolator = AccelerateDecelerateInterpolator() // 插值器

        binding.hideButton.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.clRoot,autoTransition)
            //rotation,translateX无效
            //直接修改left，设置textSize改变大小，以及设置visibility都有效
            //同时设置时，最后一项有动画效果
            //binding.hideText.left += 20
            //binding.hideText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
            binding.hideText.visibility = if(binding.hideText.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}