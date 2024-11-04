package com.mole.androidcodestudy.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.getOrDefault
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import coil.load
import com.mole.androidcodestudy.databinding.ActivityImageDetailBinding
import com.mole.androidcodestudy.databinding.ItemPinchImageviewBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.widget.PinchImageView

class ImageDetailActivity : BaseActivity() {
    private val binding by viewBinding(ActivityImageDetailBinding::inflate)
    private lateinit var mImages: List<String>
    private var mCurrentPosition = 0
    private var mPreviousPos = -1

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImages = intent.getStringArrayListExtra(KEY_IMAGE_LIST) ?: emptyList()
        mCurrentPosition = intent.getIntExtra(KEY_CURRENT_INDEX, 0)
        if (mCurrentPosition >= mImages.size) {
            finish()
            return
        }
        binding.idViewpager.apply {
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    internalOnPageSelected(position)
                }

                override fun onPageScrollStateChanged(p0: Int) {

                }

            })
        }
        binding.idViewpager.adapter = PhotoAdapter(mImages)
        binding.idViewpager.currentItem = mCurrentPosition
        //setCurrentItem不触发onPageSelected
        internalOnPageSelected(mCurrentPosition)
    }

    private fun internalOnPageSelected(position: Int) {
        mCurrentPosition = position
        binding.photoTv.text = (mCurrentPosition + 1).toString() + "/" + mImages.size
        if (mPreviousPos != -1 && mPreviousPos != position) {
            (binding.idViewpager.adapter as? PhotoAdapter)?.let {
                it.resetView(mPreviousPos)
                it.page(position)
            }
        }
        mPreviousPos = position
    }

    internal class PhotoAdapter(
        private val images: List<String>
    ) : PagerAdapter() {
        private var cacheArray: SparseArray<PinchImageView?>? = null
        private var currPos = -1

        init {
            cacheArray = SparseArray(images.size)
        }

        @Deprecated("Deprecated in Java")
        override fun destroyItem(container: View, position: Int, `object`: Any) {
            cacheArray?.remove(position)
        }

        fun resetView(curr: Int) {
            cacheArray?.getOrDefault(curr, null)?.reset()
        }


        fun page(position: Int) {
            currPos = position
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            return ItemPinchImageviewBinding.inflate(LayoutInflater.from(container.context)).apply {
                root.load(images[position])
                cacheArray?.put(position, root)
            }.also {
                container.addView(it.root, 0)
            }.root
        }

        override fun getCount() = images.size


        override fun isViewFromObject(p0: View, p1: Any) = p0 == p1
    }

    companion object {
        private const val KEY_IMAGE_LIST = "imageList"
        private const val KEY_CURRENT_INDEX = "currentIndex"

        @JvmStatic
        fun start(
            context: Context, list: ArrayList<String>, index: Int = 0
        ) {
            val starter = Intent(context, ImageDetailActivity::class.java).apply {
                putStringArrayListExtra(KEY_IMAGE_LIST, list)
                putExtra(KEY_CURRENT_INDEX, index)
            }
            context.startActivity(starter)
        }

    }
}