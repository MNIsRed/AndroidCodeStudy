package com.mole.androidcodestudy.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
    }

    private fun setClick(){
        findViewById<Button>(R.id.click_button).setOnClickListener{
            viewModel.updatePet()
        }
        findViewById<Button>(R.id.file_button).setOnClickListener {
            tryFileOutput()
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
}