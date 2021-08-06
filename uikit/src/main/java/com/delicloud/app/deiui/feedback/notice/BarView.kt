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
 * 加载条形图
 */
internal class BarView : View, Determinate {

    private var mOuterPaint: Paint? = null
    private var mInnerPaint: Paint? = null
    private var mBound: RectF? = null
    private var mInBound: RectF? = null
    private var mMax = 100
    private var mProgress = 0
    private var mBoundGap: Float = 0.toFloat()

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
        mOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOuterPaint!!.style = Paint.Style.STROKE
        mOuterPaint!!.strokeWidth = ScreenUtil.dip2px(2f).toFloat()
        mOuterPaint!!.color = Color.WHITE

        mInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInnerPaint!!.style = Paint.Style.FILL
        mInnerPaint!!.color = Color.WHITE

        mBoundGap = ScreenUtil.dip2px(5f).toFloat()
        mInBound = RectF(
            mBoundGap, mBoundGap,
            (width - mBoundGap) * mProgress / mMax, height - mBoundGap
        )

        mBound = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = ScreenUtil.dip2px(2f)
        mBound!!.set(padding.toFloat(), padding.toFloat(), (w - padding).toFloat(), (h - padding).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(mBound!!, mBound!!.height() / 2, mBound!!.height() / 2, mOuterPaint!!)
        canvas.drawRoundRect(mInBound!!, mInBound!!.height() / 2, mInBound!!.height() / 2, mInnerPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthDimension = ScreenUtil.dip2px(100f)
        val heightDimension = ScreenUtil.dip2px(20f)
        setMeasuredDimension(widthDimension, heightDimension)
    }

    override fun setMax(max: Int) {
        this.mMax = max
    }

    override fun setProgress(progress: Int) {
        this.mProgress = progress
        mInBound!!.set(
            mBoundGap, mBoundGap,
            (width - mBoundGap) * mProgress / mMax, height - mBoundGap
        )
        invalidate()
    }
}
