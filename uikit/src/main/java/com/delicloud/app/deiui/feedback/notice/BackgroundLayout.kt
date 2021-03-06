package com.delicloud.app.deiui.feedback.notice

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 * 轻提示弹窗背景
 */
internal class BackgroundLayout : LinearLayout {

    private var mCornerRadius: Float = 0.toFloat()
    private var mBackgroundColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val color = context.resources.getColor(R.color.deiui_kprogresshud_default_color)
        initBackground(color, mCornerRadius)
    }

    private fun initBackground(color: Int, cornerRadius: Float) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(color)
        drawable.cornerRadius = cornerRadius
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = drawable
        } else {

            setBackgroundDrawable(drawable)
        }
    }

    fun setCornerRadius(radius: Float) {
        mCornerRadius = ScreenUtil.dip2px(radius).toFloat()
        initBackground(mBackgroundColor, mCornerRadius)
    }

    fun setBaseColor(color: Int) {
        mBackgroundColor = color
        initBackground(mBackgroundColor, mCornerRadius)
    }
}
