package com.mole.androidcodestudy.home

import androidx.appcompat.app.AppCompatActivity
import com.mole.androidcodestudy.activity.AnimationActivity
import com.mole.androidcodestudy.activity.ConstraintLayoutActivity
import com.mole.androidcodestudy.activity.CustomLayoutManagerActivity
import com.mole.androidcodestudy.activity.CustomViewTestActivity
import com.mole.androidcodestudy.activity.MaterialButtonActivity
import com.mole.androidcodestudy.activity.NestedCoordinatorActivity
import com.mole.androidcodestudy.activity.SoftInputActivity
import com.mole.androidcodestudy.activity.TextViewActivity
import com.mole.androidcodestudy.activity.widget.ViewPager2AdaptiveHeightActivity
import com.mole.androidcodestudy.activity.AlarmActivity
import com.mole.androidcodestudy.activity.CalendarActivity
import com.mole.androidcodestudy.activity.FileActivity
import com.mole.androidcodestudy.activity.LocationActivity
import com.mole.androidcodestudy.activity.PickMediaActivity
import com.mole.androidcodestudy.activity.system.BatteryActivity
import com.mole.androidcodestudy.activity.system.ImplicitIntentActivity
import com.mole.androidcodestudy.activity.system.MediaMetadataRetrieverActivity
import com.mole.androidcodestudy.activity.library.ExoplayerActivity
import com.mole.androidcodestudy.activity.library.LombokActivity
import com.mole.androidcodestudy.activity.library.PaletteActivity
import com.mole.androidcodestudy.activity.library.TextRecognitionActivity
import com.mole.androidcodestudy.activity.CoroutineActivity
import com.mole.androidcodestudy.activity.KotlinDelegateActivity
import com.mole.androidcodestudy.activity.LiveDataActivity
import com.mole.androidcodestudy.activity.ViewModelActivity
import com.mole.androidcodestudy.library.activity.PdfViewerActivity
import com.mole.androidcodestudy.library.activity.SqlcipherActivity
import com.mole.androidcodestudy.library.activity.UEToolActivity
import com.mole.androidcodestudy.widget.activity.BreakIteratorActivity
import com.mole.androidcodestudy.widget.activity.CoordinatorLayoutActivity
import com.mole.androidcodestudy.widget.activity.ForegroundImageViewActivity
import com.mole.androidcodestudy.widget.activity.MotionLayoutActivity
import com.yc.toollib.crash.CrashListActivity

enum class HomeCategory { WIDGET, SYSTEM, LIBRARY }

data class HomeEntry(
    val title: String,
    val activity: Class<out AppCompatActivity>,
    val category: HomeCategory
)

object HomeEntries {

    val widgetEntries = listOf(
        HomeEntry("自定义View", CustomViewTestActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("嵌套Coordinator", NestedCoordinatorActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("动画", AnimationActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("软键盘", SoftInputActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("TextView", TextViewActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("MaterialButton", MaterialButtonActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("自定义 LayoutManager", CustomLayoutManagerActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("CoordinatorLayout", CoordinatorLayoutActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("MotionLayout", MotionLayoutActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("ConstraintLayout", ConstraintLayoutActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("流失文本动画", BreakIteratorActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("ForegroundImageView", ForegroundImageViewActivity::class.java, HomeCategory.WIDGET),
        HomeEntry("切换高度的 ViewPager2", ViewPager2AdaptiveHeightActivity::class.java, HomeCategory.WIDGET),
    )

    val systemEntries = listOf(
        HomeEntry("位置", LocationActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("pickMedia", PickMediaActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("文件", FileActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("日历", CalendarActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("定时器", AlarmActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("电池信息", BatteryActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("隐式意图", ImplicitIntentActivity::class.java, HomeCategory.SYSTEM),
        HomeEntry("视频元信息", MediaMetadataRetrieverActivity::class.java, HomeCategory.SYSTEM),
    )

    val libraryEntries = listOf(
        HomeEntry("委托", KotlinDelegateActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("协程", CoroutineActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("LiveData", LiveDataActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("ViewModel", ViewModelActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("Lombok", LombokActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("崩溃分析 CrashTool", CrashListActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("palette", PaletteActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("UETool", UEToolActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("Sqlcipher", SqlcipherActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("motion photo", ExoplayerActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("OCR", TextRecognitionActivity::class.java, HomeCategory.LIBRARY),
        HomeEntry("PDF 预览", PdfViewerActivity::class.java, HomeCategory.LIBRARY),
    )

    val allEntries: List<HomeEntry> = widgetEntries + systemEntries + libraryEntries
}
