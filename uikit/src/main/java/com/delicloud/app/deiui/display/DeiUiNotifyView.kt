package com.delicloud.app.deiui.display

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.ScreenUtil
import com.makeramen.roundedimageview.RoundedImageView

/**
 *@author Mr.m
 *@date 2019/11/27
 **/
class DeiUiNotifyView : LinearLayout {
    private var mNotifyType = NotifyType.NUMBER
    var mNotifyMax = 99
    private var mPointRadius = ScreenUtil.dip2px(5f)
    private var mNotifyValue = 1
    private lateinit var mNotifyTv: TextView
    private lateinit var mNotifyIv: RoundedImageView
    private lateinit var mRootView: View
    private var mTextWidth = 0f
    private var mTextHeight = 0f
    var mNotifyTextSize = ScreenUtil.sp2px(11f)
        set(value) {
            mNotifyTv.textSize = value.toFloat()
            mPaint.textSize = value.toFloat()
            field = value
        }
    var mNotifyColor: Int = Color.RED
    private lateinit var leftCircleRect: RectF
    private lateinit var rightRectF: RectF
    private lateinit var centerRect: Rect
    val mPaint by lazy {
        Paint()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.deiui_notify_layout, this, true)
        mNotifyTv = mRootView.findViewById(R.id.notify_number)
        mNotifyIv = mRootView.findViewById(R.id.notify_point)
        attributeSet?.run {
            val typeArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.DeiUiNotifyView)
            when (typeArray.getInt(R.styleable.DeiUiNotifyView_notify_type, 1)) {
                0 -> mNotifyType = NotifyType.POINT
                1 -> mNotifyType = NotifyType.NUMBER
            }
            mNotifyMax = typeArray.getInt(R.styleable.DeiUiNotifyView_notify_max, 99)
            mNotifyValue = typeArray.getInt(R.styleable.DeiUiNotifyView_notify_value, 1)
            mNotifyColor = typeArray.getColor(R.styleable.DeiUiNotifyView_notify_color, Color.RED)
            mNotifyTextSize = ScreenUtil.px2sp(
                typeArray.getDimension(
                    R.styleable.DeiUiNotifyView_notify_text_size,
                    ScreenUtil.sp2px(11f).toFloat()
                )
            )
            typeArray.recycle()
        }
        mPaint.apply {
            color = mNotifyColor
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = mNotifyTv.textSize
        }
        gravity = Gravity.CENTER
        refreshNotifyStatus()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        takeIf { mNotifyType == NotifyType.NUMBER }?.run {
            if (mNotifyValue < 10) {
                canvas?.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    mTextHeight / 2 + ScreenUtil.dip2px(4f),
                    mPaint
                )
            } else {
                setRects()
                canvas?.drawArc(leftCircleRect, 90f, 180f, false, mPaint)
                canvas?.drawRect(centerRect, mPaint)
                canvas?.drawArc(rightRectF, -90f, 180f, false, mPaint)
            }
        }
        super.dispatchDraw(canvas)
    }

    fun setPointRadius(radius: Int) {
        mPointRadius = radius
        refreshNotifyStatus()
    }

    private fun setRects() {
        val radius = mTextHeight / 2 + ScreenUtil.dip2px(3f)
        val left = measuredWidth / 2f - mTextWidth / 2 - radius
        val top = measuredHeight / 2f - radius
        val right = left + 2 * radius
        val bottom = top + 2 * radius
        leftCircleRect = RectF(left, top, right, bottom)
        val rectRight = measuredWidth / 2f + mTextWidth / 2
        centerRect = Rect(
            (left + radius).toInt(),
            top.toInt(),
            rectRight.toInt(),
            (top + 2 * radius + 1).toInt()
        )
        rightRectF = RectF(
            rectRight - radius,
            top,
            rectRight + radius,
            bottom
        )
    }


    fun setNotiftType(type: NotifyType) {
        mNotifyType = type
        refreshNotifyStatus()
    }

    fun refreshNotifyStatus() {
        if (mNotifyType == NotifyType.POINT) {
            mNotifyIv.visibility = View.VISIBLE
            mNotifyTv.visibility = View.GONE
            val lp = mNotifyIv.layoutParams
            lp.width = mPointRadius * 2
            lp.height = mPointRadius * 2
            mNotifyIv.layoutParams = lp
            mNotifyIv.setImageDrawable(ColorDrawable(mNotifyColor))
        } else {
            mNotifyIv.visibility = View.GONE
            mNotifyTv.visibility = View.VISIBLE
            setNotifyValue(mNotifyValue)
        }
    }

    fun setNotifyValue(value: Int) {
        mNotifyValue = value
        if (mNotifyType == NotifyType.NUMBER) {
            if (mNotifyValue < mNotifyMax)
                mNotifyTv.text = "$mNotifyValue"
            else {
                mNotifyValue = mNotifyMax
                mNotifyTv.text = "${mNotifyValue}+"
            }
            mTextWidth = mPaint.measureText(mNotifyTv.text.toString())
            mTextHeight = getTextHeight()
        }
        invalidate()
    }

    private fun getTextHeight(): Float {
        val fontMetrics = mPaint.fontMetrics
        return fontMetrics.descent - fontMetrics.ascent
    }

    fun dismissNotify() {
        visibility = View.GONE
    }

    fun showNotify() {
        visibility = View.VISIBLE
    }

    enum class NotifyType {
        POINT, NUMBER
    }
}