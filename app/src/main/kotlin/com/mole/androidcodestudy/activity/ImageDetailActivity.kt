package com.mole.androidcodestudy.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mole.androidcodestudy.databinding.ActivityImageDetailBinding
import com.mole.androidcodestudy.extension.viewBinding
import com.mole.androidcodestudy.widget.PinchImageView
import java.util.Date
import java.util.LinkedList

class ImageDetailActivity : BaseActivity() {
    private val binding by viewBinding(ActivityImageDetailBinding::inflate)
    private var mImgs: List<String>? = null
    private var mCurrentPosition = 0
    private var mPreviousPos = -1
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("pos", mPreviousPos)
        setResult(667, intent)
        super.onBackPressed()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = findViewById<View>(R.id.photo_tv) as TextView
        mImgs = intent.getSerializableExtra("list") as List<String>?
        mCurrentPosition = intent.getIntExtra(KEY_CURRENT_INDEX, 0)
        val saveTv = findViewById<View>(R.id.photo_save_tv) as TextView
        if (mCurrentPosition >= mImgs!!.size) {
            finish()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager!!.transitionName =
                String.format(getString(R.string.mall_banner_transation_name), mImgs!![mCurrentPosition])
        }
        val toolbar = findViewById<View>(R.id.photo_toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.icon_back_white)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { v: View? ->
            val intent = Intent()
            intent.putExtra("pos", mPreviousPos)
            setResult(667, intent)
            finish()
        }
        saveTv.setOnClickListener { v: View? ->
            PermissionsUtils.getInstance().showSavePhotoPermission(this,
                object : RequestResponseHelper() {
                    fun requestSuccess() {
                        val helper = SDFileHelper(getContext())
                        showToastMessage(R.string.saving)
                        helper.savePicture(DateUtil.date2Str(Date()), mImgs!![mCurrentPosition], true)
                    }

                    fun requestFailed() {
                        showToastMessage(getString(R.string.permission_request_failed_read))
                    }
                }
            )
        }
        textView.text = (mCurrentPosition + 1).toString() + "/" + mImgs!!.size
        mViewPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
                textView.text = (mCurrentPosition + 1).toString() + "/" + mImgs!!.size
                if (mPreviousPos != -1 && mPreviousPos != position) {
                    var adapter: PhotoAdapter
                    if (null != mViewPager && null != (mViewPager!!.adapter as PhotoAdapter?).also {
                            adapter = it!!
                        }) {
                        adapter.resetView(mPreviousPos)
                        adapter.page(position)
                    }
                }
                mPreviousPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mViewPager!!.adapter =
            PhotoAdapter(mImgs, getContext(), mCurrentPosition, intent.getBooleanExtra(FROM_MALL, false))
        mViewPager!!.currentItem = mCurrentPosition
    }

    internal class PhotoAdapter(
        private val images: List<String>?,
        private val context: Context?,
        private val pos: Int
    ) : PagerAdapter() {
        private var mViewCache: LinkedList<View>? = null
        private var cacheArray: SparseArray<PinchImageView?>? = null
        private var preCacheView: View? = null
        private var currPos = -1
        private var isDestroy = false

        init {
            mViewCache = LinkedList()
            cacheArray = SparseArray()
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var viewHolder: ViewHolder? = null
            var convertView: View? = null
            if (mViewCache!!.size == 0) {
                convertView = PinchImageView(context)
                viewHolder = ViewHolder()
                viewHolder!!.banner = convertView
                convertView.setTag(R.id.pager_tag, viewHolder)
            } else {
                convertView = mViewCache!!.removeFirst()
                viewHolder = convertView.getTag(R.id.pager_tag) as ViewHolder
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (pos == position) {
                    convertView!!.transitionName = String.format(
                        container.context.getString(R.string.mall_banner_transation_name),
                        images!![pos]
                    )
                } else {
                    convertView!!.transitionName = ""
                }
            }
            container.addView(convertView)

            //cdn域名的原因，代购站的图片基本不支持添加后缀修改分辨率，甚至可能导致无法加载正确图片
            val originUrl = images!![position]
            ImageUtils.displayMaybeGif(
                GlideApp.with(context),
                originUrl
            )
                .thumbnail(
                    ImageUtils.displayMaybeGif(
                        GlideApp.with(context),
                        originUrl
                    )
                )
                .placeholder(R.drawable.place_holder)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(convertView as ImageView?)
            (convertView as PinchImageView?)!!.setOnClickListener(
                View.OnClickListener {
                    if (null == context) {
                        return@OnClickListener
                    }
                    if (context is BaseActivity) {
                        val intent = Intent()
                        intent.putExtra("pos", currPos)
                        context.setResult(667, intent)
                        context.finish()
                    }
                })
            cacheArray!!.put(position, convertView)
            return convertView.also { preCacheView = it }!!
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val contentView = `object` as View
            container.removeView(contentView)
            if (null != contentView.background) {
                contentView.background.callback = null
            }
            Glide.with(contentView).clear(contentView)
            if (!isDestroy) {
                mViewCache!!.add(contentView)
            }
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        fun resetView(curr: Int) {
            if (null == cacheArray) {
                return
            }
            val item = cacheArray[curr]
            item?.reset()
        }

        override fun getCount(): Int {
            return images?.size ?: 0
        }

        fun page(position: Int) {
            currPos = position
        }

        inner class ViewHolder {
            var banner: ImageView? = null
        }

        fun destroy() {
            if (null != mViewCache) {
                var itemView: View?
                for (pos in mViewCache!!.indices) {
                    itemView = mViewCache!![pos]
                    Glide.with(itemView).clear(itemView)
                    unbindDrawables(itemView)
                    itemView.destroyDrawingCache()
                    itemView = null
                }
                mViewCache!!.clear()
                mViewCache = null
            }
            isDestroy = true
        }

        private fun unbindDrawables(view: View?) {
            if (null == view) {
                return
            }
            if (view.background != null) {
                view.background.callback = null
            }
            if (view is ViewGroup && view !is AdapterView<*>) {
                for (i in 0 until view.childCount) {
                    unbindDrawables(view.getChildAt(i))
                }
                view.removeAllViews()
            }
        }
    }

    override fun onDestroy() {
        PermissionsUtils.getInstance().closeDialog()
        super.onDestroy()
    }

    companion object {
        private const val KEY_CURRENT_INDEX = "currentIndex"
        fun newIntent(
            context: Context?,
            list: ArrayList<String>?,
            index: Int?
        ): Intent {
            val intent = Intent(context, ImageDetailActivity::class.java)
            intent.putStringArrayListExtra("list", list)
            intent.putExtra(KEY_CURRENT_INDEX, index)
            return intent
        }


    }
}
