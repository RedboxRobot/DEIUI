package com.delicloud.app.deiui.feedback.notice

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 * 旋转框饼图
 */
internal class PieView : View, Determinate {

    private var mWhitePaint: Paint? = null
    private var mGreyPaint: Paint? = null
    private var mBound: RectF? = null
    private var mMax = 100
    private var mProgress = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWhitePaint!!.style = Paint.Style.FILL_AND_STROKE
        mWhitePaint!!.strokeWidth = ScreenUtil.dip2px(0.1f).toFloat()
        mWhitePaint!!.color = Color.WHITE

        mGreyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mGreyPaint!!.style = Paint.Style.STROKE
        mGreyPaint!!.strokeWidth = ScreenUtil.dip2px(2f).toFloat()
        mGreyPaint!!.color = Color.WHITE

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
        canvas.drawArc(mBound!!, 270f, mAngle, true, mWhitePaint!!)
        val padding = ScreenUtil.dip2px(4f)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2 - padding).toFloat(), mGreyPaint!!)
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
        this.mProgress = progress
        invalidate()
    }
}
