package com.mole.androidcodestudy.activity.system

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityMediaMetadataRetrieverBinding
import com.mole.androidcodestudy.extension.useCompat
import com.mole.androidcodestudy.extension.viewBinding

/**
 *  MediaMetadataRetrieverActivity
 */
class MediaMetadataRetrieverActivity : BaseActivity() {
    private val binding by viewBinding(ActivityMediaMetadataRetrieverBinding::inflate)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding.btnRetrieveMetadata.setOnClickListener {
            retrieveMetadata()
        }
        
        // 初始调用一次
        retrieveMetadata()
    }
    
    private fun retrieveMetadata() {
        MediaMetadataRetriever().useCompat {
            try {
                it.setDataSource("https://oss.dev.fxwljy.com/goods/file/2025082916595638LV.mp4")
                val width = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                val height = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                val duration = it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                
                val info = "Width: $width\nHeight: $height\nDuration: $duration ms"
                binding.tvVideoInfo.text = info
                
                Log.d("MediaMetadata", info)
            } catch (e: Exception) {
                Log.e("MediaMetadata", "Error retrieving metadata", e)
                binding.tvVideoInfo.text = "Error: ${e.message}"
            }
        }
    }
}
