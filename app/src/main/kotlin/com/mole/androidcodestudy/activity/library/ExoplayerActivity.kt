package com.mole.androidcodestudy.activity.library

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivityExoplayerBinding
import com.mole.androidcodestudy.extension.viewBinding


/**
 *  ExoplayerActivity
 */
class ExoplayerActivity : BaseActivity() {

    private val binding by viewBinding(ActivityExoplayerBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.playerView.apply {
            player = ExoPlayer.Builder(this@ExoplayerActivity).build()
            val fileNameInAssets = "WechatIMG8.jpg"
            val videoUri = Uri.parse("file:///android_asset/$fileNameInAssets")
            val mediaItem = MediaItem.fromUri(videoUri)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.play()
        }
    }
}