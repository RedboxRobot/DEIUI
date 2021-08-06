package com.delicloud.app.deiui.feedback.notice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 * 加载环形视图
 */
internal class AnnularView : View, Determinate {

    private var mWhitePaint: Paint? = null
    private var mGreyPaint: Paint? = null
    private var mBound: RectF? = null
    private var mMax = 100
    private var mProgress = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWhitePaint!!.style = Paint.Style.STROKE
        mWhitePaint!!.strokeWidth = ScreenUtil.dip2px(3f).toFloat()
        mWhitePaint!!.color = Color.WHITE

        mGreyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mGreyPaint!!.style = Paint.Style.STROKE
        mGreyPaint!!.strokeWidth = ScreenUtil.dip2px(3f).toFloat()
        mGreyPaint!!.color = context.resources.getColor(R.color.deiui_kprogresshud_grey_color)

        mBound = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = ScreenUtil.dip2px(4f)
        mBound!!.set(padding.toFloat(), padding.toFloat(), (w - padding).toFloat(), (h - padding).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val mAngle = mProgress * 360f / mMax
        canvas.drawArc(mBound!!, 270f, mAngle, false, mWhitePaint!!)
        canvas.drawArc(mBound!!, 270 + mAngle, 360 - mAngle, false, mGreyPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val dimension = ScreenUtil.dip2px(40f)
        setMeasuredDimension(dimension, dimension)
    }

    override fun setMax(max: Int) {
        this.mMax = max
    }

    override fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
    }
}
