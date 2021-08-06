package com.delicloud.app.deiui.display

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import com.delicloud.app.deiui.R

import java.util.ArrayList
import kotlin.math.min

/**
 * 日期：16/6/22 14:15
 *
 *
 * 描述：水平StepsViewIndicator 指示器
 */
class DeiUiHorizontalStepsViewIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    //定义默认的高度   definition default height
    private val defaultStepIndicatorNum =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, resources.displayMetrics).toInt()

    private var mCompletedLineHeight: Float = 0.toFloat()//完成线的高度     definition completed line height
    /**
     * get圆的半径  get circle radius
     *
     * @return
     */
    private var circleRadius: Float = 0.toFloat()

    private var mCompleteIcon: Drawable? = null//完成的默认图片    definition default completed icon
    private var mAttentionIcon: Drawable? = null//正在进行的默认图片     definition default underway icon
    private var mDefaultIcon: Drawable? = null//默认的背景图  definition default unCompleted icon
    private var mCenterY: Float = 0.toFloat()//该view的Y轴中间位置     definition view centerY position
    private var mLeftY: Float = 0.toFloat()//左上方的Y位置  definition rectangle LeftY position
    private var mRightY: Float = 0.toFloat()//右下方的位置  definition rectangle RightY position

    private var mStepBeanList: List<StepBean>? = null//当前有几部流程    there are currently few step
    private var mStepNum = 0
    private var mLinePadding: Float = 0.toFloat()//两条连线之间的间距  definition the spacing between the two circles
    private var mDrawbleLinePadding: Float = 0.toFloat()

    private var mCircleCenterPointPositionList: MutableList<Float>? =
        null//定义所有圆的圆心点位置的集合 definition all of circles center point list

    private var mCompleteLine: Drawable? = null//完成的默认图片    definition default completed icon
    private var mAttentionLine: Drawable? = null//正在进行的默认图片     definition default underway icon
    private var mDefaultLine: Drawable? = null//默认的背景图  definition default unCompleted icon

    private var mUnCompletedPaint: Paint? = null//未完成Paint  definition mUnCompletedPaint
    private var mCompletedPaint: Paint? = null//完成paint      definition mCompletedPaint
    private var mUnCompletedLineColor =
        ContextCompat.getColor(getContext(), R.color.deiui_uncompleted_color)//定义默认未完成线的颜色  definition
    private var mCompletedLineColor =
        ContextCompat.getColor(getContext(), R.color.deiui_white)//定义默认完成线的颜色      definition mCompletedLineColor
    private var mEffects: PathEffect? = null
    private var mCompletingPosition: Int = 0//正在进行position   underway position


    private var mPath1: Path? = null
    private var mPath2: Path? = null

    private var mOnDrawListener: OnDrawIndicatorListener? = null
    private var screenWidth: Int = 0//this screen width

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    val circleCenterPointPositionList: List<Float>?
        get() = mCircleCenterPointPositionList

    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    fun setOnDrawListener(onDrawListener: OnDrawIndicatorListener) {
        mOnDrawListener = onDrawListener
    }

    init {
        init()
    }

    /**
     * init
     */
    private fun init() {
        mStepBeanList = ArrayList()
        mPath1 = Path()
        mPath2 = Path()
        mEffects = DashPathEffect(floatArrayOf(10f, 10f), 10f)

        mCircleCenterPointPositionList = ArrayList()//初始化

        mUnCompletedPaint = Paint()
        mCompletedPaint = Paint()
        mUnCompletedPaint!!.isAntiAlias = true
        mUnCompletedPaint!!.style = Paint.Style.STROKE
        mUnCompletedPaint!!.strokeWidth = 10f
        mUnCompletedPaint!!.pathEffect = mEffects

        mCompletedPaint!!.isAntiAlias = true
        mCompletedPaint!!.style = Paint.Style.STROKE
        mCompletedPaint!!.strokeWidth = 10f
        mCompletedPaint!!.pathEffect = mEffects


        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.08f * defaultStepIndicatorNum
        //圆的半径  set mCircleRadius
        circleRadius = 0.28f * defaultStepIndicatorNum
        //线与线之间的间距    set mLinePadding
        mLinePadding = 2.5f * defaultStepIndicatorNum
        //完成先图直接的间距
        mDrawbleLinePadding = 0.1f * defaultStepIndicatorNum

        mCompleteIcon = ContextCompat.getDrawable(context, R.drawable.deiui_complted)//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(context, R.drawable.deiui_attention)//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(context, R.drawable.deiui_default_icon)//未完成的icon

        mCompleteLine = ContextCompat.getDrawable(context, R.drawable.deiui_transition2)//已经完成的icon
        mAttentionLine = ContextCompat.getDrawable(context, R.drawable.deiui_transition1)//正在进行的icon
        mDefaultLine = ContextCompat.getDrawable(context, R.drawable.deiui_transition0)//未完成的icon
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width: Int = (mStepNum.toFloat() * circleRadius * 2f - (mStepNum - 1) * mLinePadding).toInt()
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            screenWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        var height = defaultStepIndicatorNum
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = min(height, MeasureSpec.getSize(heightMeasureSpec))
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * height
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - mCompletedLineHeight / 2
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2

        mCircleCenterPointPositionList!!.clear()
        for (i in 0 until mStepNum) {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离）*2）
            val paddingLeft =
                (screenWidth.toFloat() - mStepNum.toFloat() * circleRadius * 2f - (mStepNum - 1) * mLinePadding) / 2
            //add to list
            mCircleCenterPointPositionList!!.add(paddingLeft + circleRadius + i.toFloat() * circleRadius * 2f + i * mLinePadding)
        }

        /**
         * set listener
         */
        if (mOnDrawListener != null) {
            mOnDrawListener!!.onDrawIndicator()
        }
    }

    @SuppressLint("DrawAllocation")
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mOnDrawListener != null) {
            mOnDrawListener!!.onDrawIndicator()
        }

        //-----------------------画线-------draw line-----------------------------------------------
        for (i in 0 until mCircleCenterPointPositionList!!.size - 1) {
            //前一个ComplectedXPosition
            val preComplectedXPosition = mCircleCenterPointPositionList!![i]
            //后一个ComplectedXPosition
            val afterComplectedXPosition = mCircleCenterPointPositionList!![i + 1]

            val rect = Rect(
                (preComplectedXPosition + circleRadius + mDrawbleLinePadding).toInt(),
                (mCenterY - mCompletedLineHeight / 2).toInt(),
                (afterComplectedXPosition - circleRadius - mDrawbleLinePadding).toInt(),
                (mCenterY + mCompletedLineHeight / 2).toInt()
            )

            val stepsBean = mStepBeanList!![i]
            if (stepsBean.state == StepBean.STEP_UNDO) {
                mDefaultLine!!.bounds = rect
                mDefaultLine!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_CURRENT) {
                mAttentionLine!!.bounds = rect
                mAttentionLine!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_COMPLETED) {
                mCompleteLine!!.bounds = rect
                mCompleteLine!!.draw(canvas)
            }


        }
        //-----------------------画图标-----draw icon-----------------------------------------------
        for (i in mCircleCenterPointPositionList!!.indices) {
            val currentComplectedXPosition = mCircleCenterPointPositionList!![i]
            val rect = Rect(
                (currentComplectedXPosition - circleRadius).toInt(),
                (mCenterY - circleRadius).toInt(),
                (currentComplectedXPosition + circleRadius).toInt(),
                (mCenterY + circleRadius).toInt()
            )

            val stepsBean = mStepBeanList!![i]

            if (stepsBean.state == StepBean.STEP_UNDO) {
                mDefaultIcon!!.bounds = rect
                mDefaultIcon!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_CURRENT) {
                mAttentionIcon!!.bounds = rect
                mAttentionIcon!!.draw(canvas)
            } else if (stepsBean.state == StepBean.STEP_COMPLETED) {
                mCompleteIcon!!.bounds = rect
                mCompleteIcon!!.draw(canvas)
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }

    /**
     * 设置流程步数
     *
     * @param stepsBeanList 流程步数
     */
    fun setStepNum(stepsBeanList: List<StepBean>) {
        this.mStepBeanList = stepsBeanList
        mStepNum = mStepBeanList!!.size

        if (mStepBeanList != null && mStepBeanList!!.isNotEmpty()) {
            for (i in 0 until mStepNum) {
                val stepsBean = mStepBeanList!![i]
                if (stepsBean.state == StepBean.STEP_COMPLETED) {
                    mCompletingPosition = i
                }
            }
        }

        requestLayout()
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    fun setUnCompletedLineColor(unCompletedLineColor: Int) {
        this.mUnCompletedLineColor = unCompletedLineColor
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    fun setCompletedLineColor(completedLineColor: Int) {
        this.mCompletedLineColor = completedLineColor
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    fun setDefaultIcon(defaultIcon: Drawable) {
        this.mDefaultIcon = defaultIcon
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    fun setCompleteIcon(completeIcon: Drawable) {
        this.mCompleteIcon = completeIcon
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setAttentionIcon(attentionIcon: Drawable) {
        this.mAttentionIcon = attentionIcon
    }

    /**
     * 设置默认线的图片
     * @param completeLine
     */
    fun setCompleteLine(completeLine: Drawable) {
        mCompleteLine = completeLine
    }

    /**
     * 设置正在进行中的线的图片
     * @param attentionLine
     */
    fun setAttentionLine(attentionLine: Drawable) {
        mAttentionLine = attentionLine
    }

    /**
     * 设置已完成线的图片
     * @param defaultLine
     */
    fun setDefaultLine(defaultLine: Drawable) {
        mDefaultLine = defaultLine
    }

    /**
     * 设置对view监听
     */
    interface OnDrawIndicatorListener {
        fun onDrawIndicator()
    }
}
