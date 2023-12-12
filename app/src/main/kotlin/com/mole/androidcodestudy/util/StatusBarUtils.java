package com.mole.androidcodestudy.util;

import static com.google.android.material.appbar.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN;
import static com.mole.androidcodestudy.util.ExtKt.getHasM;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mole.androidcodestudy.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 修改状态栏颜色
 * Created by wuDi on 2017/5/25.
 * <p>
 * TODO 之后考虑统一 状态栏工具类 将 StatusBarUtil 和 StatusBarCompat 功能合并到这里
 * <p>
 * 页面状态栏主要的类型：
 * 1.全屏Activity+白色背景+浅色主题的状态栏文字颜色
 * 2.沉浸式状态栏同时，给toolbar手动添加margin形成fitSystemWindow的效果(CommunitySearchActivity)
 * 3.Activity沉浸式，Fragment间各不相同（MainActivity）
 * 4.一开始沉浸式的，但是向下滑动后变成浅色主题状态栏（YahooDetailsActivity）
 * <p>
 * 大部分默认设置是第一种情况，白色状态栏+黑色状态栏文字+FULL_SCREEN(侧滑效果)
 * 这种其实就是设置成白色背景的沉浸式这应该是BaseActivity的默认设置，功能应该成三个:
 * ①.设置沉浸式全屏
 * ②.xml需要设置fitSystemWindow=true
 * ③.大部分情况都是白色背景所以状态栏文字黑色，例外情况注意设置回白色文字状态栏
 */

public class StatusBarUtils {

    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;
    private static final int FAKE_IN_APPBAR_STATUS_BAR_VIEW_ID = R.id.status_bar_in_appbar_fake_view;

    public static void adaptationStatusBar(Activity activity) {
        transparencyBar(activity);
        changeStatusFontColorStyle(activity, true);
    }

    public static void clearAdaptionStatusBar(Activity activity) {
        //int type = getTypes(activity);
        changeStatusFontColorStyle(activity, false);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        setWindowStatusBarColorInt(activity, activity.getResources().getColor(colorResId));
    }

    public static void setWindowStatusBarColorInt(Activity activity, int colorId) {
        try {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorId);
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                if (window != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
                }

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改状态栏为全透明
     * 并且设置Activity全屏
     *
     * @param activity activity context
     */
    public static void transparencyBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int uiVisibility = window.getDecorView().getSystemUiVisibility();
        window.getDecorView().setSystemUiVisibility(
                uiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public static int changeStatusFontColorStyle(Activity activity, boolean isBlack) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(isBlack ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            result = 3;
        } else if (MIUISetStatusBarLightMode(activity.getWindow(), isBlack)) {
            result = 1;
        } else if (FlymeSetStatusBarLightMode(activity.getWindow(), isBlack)) {
            result = 2;
        }
        return result;
    }

    public static int changeStatusFontColorStyle(Window window, boolean isBlack) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView()
                    .setSystemUiVisibility(isBlack ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            result = 3;
        } else if (MIUISetStatusBarLightMode(window, isBlack)) {
            result = 1;
        } else if (FlymeSetStatusBarLightMode(window, isBlack)) {
            result = 2;
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag =
                        WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 为设置FUllSCREEN的Fragment设置状态栏默认白色
     * Fragment不可使用FitSystemWindow
     * Android6.0 以下不可修改状态栏文字颜色，所以使用FF6868
     */
    public static void compatFragment(Fragment fragment) {
        compatFragment(fragment, getHasM() ? R.color.white : R.color.masa_red);
    }

    /**
     * 为设置FUllSCREEN的Fragment设置状态栏颜色
     * Fragment不可使用FitSystemWindow
     */
    public static void compatFragment(Fragment fragment, int colorRes) {
        if (fragment == null) return;
        View mRootView = fragment.getView();
        Context context = fragment.getContext();
        if (mRootView == null || context == null) return;

        if (null == mRootView.findViewById(R.id.status_campat_view_fragment)) {
            View statusBarView = new View(context);
            statusBarView.setId(R.id.status_campat_view_fragment);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(context));
            statusBarView.setBackgroundColor(ExtKt.getColor(colorRes));
            ((LinearLayout) mRootView).addView(statusBarView, 0, lp);
        }
    }

    /**
     * 设置浅色状态栏，不全屏
     */
    public static void pureChangeStatusFontColorStyle(Activity activity, boolean isBlack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int originVisibility = activity.getWindow()
                    .getDecorView().getSystemUiVisibility();
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(isBlack ? originVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : originVisibility ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (MIUISetStatusBarLightMode(activity.getWindow(), isBlack)) {
        } else if (FlymeSetStatusBarLightMode(activity.getWindow(), isBlack)) {
        }
    }

    /**
     * 非图片式沉浸式需要设置FitSystemWindow = true
     */
    public static void setFitSystemWindow(Activity activity, boolean fitSystemWindow) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fitSystemWindow);
                ((ViewGroup) childView).setClipToPadding(fitSystemWindow);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int height;
        try {
            // 获得状态栏高度
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            height = (int) Math.ceil((double) context.getResources().getDimension(resourceId));
        } catch (Exception e) {
            height = 75;
        }
        return height;
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param activity       需要设置的activity
     * @param drawerLayout   DrawerLayout
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */
    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout,
                                               @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        transparencyBar(activity);
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        View fakeStatusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            contentLayout.addView(createStatusBarView(activity, color, 0), 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(),
                            getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
        addTranslucentView(activity, statusBarAlpha);
    }


    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout,
                                                ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity,
                                           @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    /**
     * 为 MallDiscount设置拖拽不会产生statusBar显示
     */
    public static void setFakeStatusBarColor(Activity activity,
                                             @ColorInt int color) {
        transparencyBar(activity);
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            contentLayout.addView(createStatusBarView(activity, color, 0), 0);
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 为状态栏颜色会因为 AppbarLayout的滑动距离改变添加固定view
     *
     * @see #FAKE_IN_APPBAR_STATUS_BAR_VIEW_ID
     */
    public static void addInAppBarFakeStatusView(Activity activity, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        CollapsingToolbarLayout.LayoutParams params =
                new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
        params.setCollapseMode(COLLAPSE_MODE_PIN);
        statusBarView.setLayoutParams(params);
        statusBarView.setId(FAKE_IN_APPBAR_STATUS_BAR_VIEW_ID);
        statusBarView.setElevation(toolbar.getElevation());
        collapsingToolbarLayout.addView(statusBarView, params);
    }

    /**
     * 为状态栏颜色会因为 AppbarLayout的滑动距离改变
     * 占位view的背景颜色
     */
    public static void setFakeAppBarStatusBarColor(Activity activity, int color) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_IN_APPBAR_STATUS_BAR_VIEW_ID);
        if (fakeTranslucentView != null) {
            fakeTranslucentView.setBackgroundColor(color);
        }
    }
}
