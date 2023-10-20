package com.mole.androidcodestudy.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.mole.androidcodestudy.databinding.ActivityPickMediaBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/10/17
 *     desc   : 测试Android11的照片选择器
 *     https://developer.android.com/training/data-storage/shared/photopicker?hl=zh-cn
 *     version: 1.0
 * </pre>
 */
class PickMediaActivity : BaseActivity(){
    private lateinit var binding:ActivityPickMediaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            it?.let {
                binding.iv.load(it)
            }
        }
        binding.bt.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, PickMediaActivity::class.java)
            context.startActivity(starter)
        }
    }
}