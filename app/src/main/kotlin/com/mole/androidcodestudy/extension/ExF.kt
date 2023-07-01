package com.mole.androidcodestudy.extension

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.core.content.ContextCompat

/**
 * 扩展函数聚合类
 */

/**
 * R.attr 中声明的颜色属性取值封装扩展
 *  context.theme.resolveAttribute获取到的是主题属性，可能是
 *  #FF000000，也可能是R.color.black
 */
fun Int.getAttrColor(context:Context):Int{
    val typedValue = TypedValue()
    context.theme.resolveAttribute(this,typedValue,true)
    return if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT &&
        typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT){
        //attribute对应的是一个颜色值
        typedValue.data
    }else if(typedValue.type == TypedValue.TYPE_REFERENCE){
        //attribute是一个颜色资源引用
        ContextCompat.getColor(context,typedValue.resourceId)
    }else{
        0
    }
}

/**
 * R.color 比 R.attr好拿资源值的原因是，R.attr指向的是一个theme里的主题属性。
 */
fun Int.getColor(context: Context):Int{
    return context.resources.getColor(this,null)
}


fun Float.dp2px(context: Context):Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this,context.resources.displayMetrics)
}
fun Float.dp2px():Float{
    //其实也可以用Resources.getSystem().displayMetrics,这样不用传入context而且设备信息是一样的
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)
}

fun Int.dp2px():Float = this.toFloat().dp2px()