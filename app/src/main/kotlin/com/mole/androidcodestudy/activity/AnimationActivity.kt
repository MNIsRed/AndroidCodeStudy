package com.mole.androidcodestudy.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.mole.androidcodestudy.databinding.ActivityAnimationBinding
import com.mole.androidcodestudy.extension.viewBinding

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2023/12/14
 *     desc   : 测试动画
 *     version: 1.0
 * </pre>
 */
class AnimationActivity : BaseActivity() {
    private val binding by viewBinding(ActivityAnimationBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoTransitionTest()
        infinityScrollImage()
    }

    private fun autoTransitionTest() {
        val autoTransition = AutoTransition()
        autoTransition.startDelay = 500
        autoTransition.duration = 2000
        autoTransition.interpolator = AccelerateDecelerateInterpolator() // 插值器

        binding.hideButton.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, autoTransition)
            //rotation,translateX无效
            //直接修改left，设置textSize改变大小，以及设置visibility都有效
            //同时设置时，最后一项有动画效果
            //binding.hideText.left += 20
            //binding.hideText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
            binding.hideText.text =
                if (binding.hideText.visibility == View.VISIBLE) "隐藏这段文字" else "显示这段文字"
            binding.hideText.visibility =
                if (binding.hideText.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private var infinityRotationAnimator: ValueAnimator? = null
    private fun infinityScrollImage() {
        ValueAnimator.ofFloat(0f, 360f * 200).apply {
            duration = 200 * 1000
            repeatCount = 1
            interpolator = CustomInterpolator()
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                binding.ivAvatar.rotation = animatedValue
            }
        }.also {
            infinityRotationAnimator = it
        }

        binding.ivAvatar.setOnClickListener {
            infinityRotationAnimator?.let {
                if (it.isRunning) {
                    it.cancel()
                    binding.ivAvatar.rotation = 0f
                } else {
                    it.start()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        infinityRotationAnimator?.let {
            if (it.isRunning) {
                it.cancel()
            }
        }
    }

    // 自定义插值器
    class CustomInterpolator : Interpolator {
        override fun getInterpolation(input: Float): Float {
            // 应用指数上升的插值器
            //实践证明，插值器的值可以比1大，借此可以实现越转越快的效果
            return (input * 200) * input * 10 / 200
        }
    }

}