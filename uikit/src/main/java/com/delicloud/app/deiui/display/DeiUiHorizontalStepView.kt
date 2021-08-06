package com.delicloud.app.deiui.display

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.delicloud.app.deiui.R

/**
 * @author
 */
class DeiUiHorizontalStepView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DeiUiHorizontalStepsViewIndicator.OnDrawIndicatorListener {
    private var mTextContainer: RelativeLayout? = null
    private var mStepsViewIndicator: DeiUiHorizontalStepsViewIndicator? = null
    private var mStepBeanList: List<StepBean>? = null
    private val mCompletingPosition: Int = 0
    private var mUnComplectedTextColor =
        ContextCompat.getColor(getContext(), R.color.deiui_uncompleted_text_color)//定义默认未完成文字的颜色;
    private var mComplectedTextColor = ContextCompat.getColor(getContext(), R.color.deiui_white)//定义默认完成文字的颜色;
    private var mTextSize = 14//default textSize
    private var mTextView: TextView? = null

    init {
        init()
    }

    private fun init() {
        val rootView = LayoutInflater.from(context).inflate(R.layout.deiui_widget_horizontal_stepsview, this)
        mStepsViewIndicator = rootView.findViewById<View>(R.id.steps_indicator) as DeiUiHorizontalStepsViewIndicator
        mStepsViewIndicator!!.setOnDrawListener(this)
        mTextContainer = rootView.findViewById<View>(R.id.rl_text_container) as RelativeLayout
    }

    /**
     * 设置显示的文字
     *
     * @param stepsBeanList
     * @return
     */
    fun setStepViewTexts(stepsBeanList: List<StepBean>): DeiUiHorizontalStepView {
        mStepBeanList = stepsBeanList
        mStepsViewIndicator!!.setStepNum(mStepBeanList!!)
        return this
    }

    /**
     * 更新stepView完成状态.
     * @param currentIndex 当前正在进行节点序号
     */
    fun updateStepView(currentIndex: Int) {

        if (mStepBeanList == null || mStepBeanList!!.isEmpty()) {
            return
        }

        if (mStepBeanList!!.size == 1) {
            mStepBeanList!![0].state = StepBean.STEP_COMPLETED
            return
        }

        when (currentIndex) {
            mStepBeanList!!.size - 1 -> for (stepBean in mStepBeanList!!) {
                stepBean.state = StepBean.STEP_COMPLETED
            }
            0 -> for (stepBean in mStepBeanList!!) {
                when {
                    stepBean.index == 0 -> stepBean.state = StepBean.STEP_CURRENT
                    stepBean.index == 1 -> stepBean.state = StepBean.STEP_UNDO
                    else -> stepBean.state = StepBean.STEP_UNDO
                }
            }
            else -> for (stepBean in mStepBeanList!!) {
                when {
                    stepBean.index < currentIndex -> stepBean.state = StepBean.STEP_COMPLETED
                    currentIndex == stepBean.index -> stepBean.state = StepBean.STEP_CURRENT
                    else -> stepBean.state = StepBean.STEP_UNDO
                }
            }
        }
        setStepViewTexts(mStepBeanList!!)
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    fun setStepViewUnComplectedTextColor(unComplectedTextColor: Int): DeiUiHorizontalStepView {
        mUnComplectedTextColor = unComplectedTextColor
        return this
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    fun setStepViewComplectedTextColor(complectedTextColor: Int): DeiUiHorizontalStepView {
        this.mComplectedTextColor = complectedTextColor
        return this
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    fun setStepsViewIndicatorUnCompletedLineColor(unCompletedLineColor: Int): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setUnCompletedLineColor(unCompletedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    fun setStepsViewIndicatorCompletedLineColor(completedLineColor: Int): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setCompletedLineColor(completedLineColor)
        return this
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    fun setStepsViewIndicatorDefaultIcon(defaultIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setDefaultIcon(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    fun setStepsViewIndicatorCompleteIcon(completeIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setCompleteIcon(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    fun setStepsViewIndicatorAttentionIcon(attentionIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setAttentionIcon(attentionIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator默认线的图片
     *
     * @param defaultIcon
     */
    fun setStepsViewIndicatorDefaultLine(defaultIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setDefaultLine(defaultIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator已完成线的图片
     *
     * @param completeIcon
     */
    fun setStepsViewIndicatorCompleteLine(completeIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setCompleteLine(completeIcon)
        return this
    }

    /**
     * 设置StepsViewIndicator正在进行中线的图片
     *
     * @param attentionIcon
     */
    fun setStepsViewIndicatorAttentionLine(attentionIcon: Drawable): DeiUiHorizontalStepView {
        mStepsViewIndicator!!.setAttentionLine(attentionIcon)
        return this
    }

    /**
     * set textSize
     *
     * @param textSize
     * @return
     */
    fun setTextSize(textSize: Int): DeiUiHorizontalStepView {
        if (textSize > 0) {
            mTextSize = textSize
        }
        return this
    }

    override fun onDrawIndicator() {
        if (mTextContainer != null) {
            mTextContainer!!.removeAllViews()
            val complectedXPosition = mStepsViewIndicator!!.circleCenterPointPositionList
            if (mStepBeanList != null && complectedXPosition != null && complectedXPosition.size > 0) {
                for (i in mStepBeanList!!.indices) {
                    mTextView = TextView(context)
                    mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize.toFloat())
                    mTextView!!.text = mStepBeanList!![i].name
                    val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    mTextView!!.measure(spec, spec)
                    // getMeasuredWidth
                    val measuredWidth = mTextView!!.measuredWidth
                    mTextView!!.x = complectedXPosition[i] - measuredWidth / 2
                    mTextView!!.layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                    if (i <= mCompletingPosition) {
                        mTextView!!.setTypeface(null, Typeface.BOLD)
                        mTextView!!.setTextColor(mComplectedTextColor)
                    } else {
                        if (mStepBeanList!![i].state != StepBean.STEP_UNDO) {
                            mTextView!!.setTypeface(null, Typeface.BOLD)
                            mTextView!!.setTextColor(mComplectedTextColor)
                        } else {
                            mTextView!!.setTextColor(mUnComplectedTextColor)
                        }
                    }

                    mTextContainer!!.addView(mTextView)
                }
            }
        }
    }

}
