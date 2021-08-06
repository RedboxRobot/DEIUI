package com.delicloud.app.deiui.entry

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.delicloud.app.deiui.R
import java.math.BigDecimal

/**
 * 步进器
 *Created By Mr.m
 *2019/7/25
 **/
open class DeiUiStepView : LinearLayout {
    private lateinit var root: View
    private var mContext: Context
    //增加button
    private lateinit var incImageView: ImageView
    //减小button
    private lateinit var desImageView: ImageView
    //每次步进数
    private var mSection = 1f

    var leftDrawable: Int = R.drawable.deiui_stepview_des_big_selector
        set(value) {
            desImageView.setImageDrawable(ContextCompat.getDrawable(mContext, value))
            field = value
        }
    var rightDrawable: Int = R.drawable.deiui_stepview_inc_big_selector
        set(value) {
            incImageView.setImageDrawable(ContextCompat.getDrawable(mContext, value))
            field = value
        }
    //float类型的有效位数
    var maxLength: Int = 7
        set(value) {
            stepEt.filters = arrayOf(InputFilter.LengthFilter(value))
            field = value
        }
    var textColor: Int = resources.getColor(R.color.color_33)
        set(value) {
            stepEt.setTextColor(value)
            field = value
        }
    //步数监听
    var stepChangeListener: OnStepChangeListener? = null
    lateinit var stepEt: EditText
    //步数下限
    var min = -999.9f
    //步数上限
    var max = 999.9f
    //当前步数
    private var mStep = 10f
    var inputIllegalListener: InputStepIllegalListener? = null


    constructor(context: Context) : super(context) {
        this.mContext = context
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        mContext = context
        initView(context)
        initAttr(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        mContext = context
        initView(context)
        initAttr(attributeSet)
    }

    fun initAttr(attributeSet: AttributeSet) {
        val typeArray =
            mContext.obtainStyledAttributes(attributeSet, R.styleable.DeiUiStepView, 0, -1)
        try {
            //填充参数
            min = typeArray.getFloat(R.styleable.DeiUiStepView_min, -999.9f)
            max = typeArray.getFloat(R.styleable.DeiUiStepView_max, 999.9f)
            mSection = typeArray.getFloat(R.styleable.DeiUiStepView_section, 1f)
            mStep = typeArray.getFloat(R.styleable.DeiUiStepView_step, 0f)
            val editable = typeArray.getBoolean(R.styleable.DeiUiStepView_editable, false)
            val size = typeArray.getInteger(R.styleable.DeiUiStepView_size, 0)
            rightDrawable = typeArray.getResourceId(
                R.styleable.DeiUiStepView_rightDrawable,
                if (size == 1) R.drawable.deiui_stepview_inc_big_selector else R.drawable.deiui_stepview_inc_selector
            )

            leftDrawable = typeArray.getResourceId(
                R.styleable.DeiUiStepView_leftDrawable,
                if (size == 1) R.drawable.deiui_stepview_des_big_selector else R.drawable.deiui_stepview_des_selector
            )
            val textSize = typeArray.getDimension(R.styleable.DeiUiStepView_stepTextSize, 24f)
            val textColor = typeArray.getColor(
                R.styleable.DeiUiStepView_stepTextColor,
                resources.getColor(R.color.color_33)
            )
            stepEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            stepEt.setTextColor(textColor)
            setStep(mStep)
            setStepEditable(editable)
            incImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    rightDrawable
                )
            )
            desImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    leftDrawable
                )
            )
            addStepTextChangeListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (stepEt.text.isNullOrEmpty()) {
                        return
                    }
                    var value = s.toString()
                    if (s!!.contains("-") && !s.startsWith("-")) {
                        val sur = s.substring(0, s.indexOf("-"))
                        value = s.replaceFirst(Regex(sur), "") + sur
                        stepEt.setText(value)
                    }
                    if (value == "-") {
                        value = min.toString()
                        stepEt.setText(value)
                    }
                    val stepEtValue = value.toFloat()
                    if (isStepIllegal(stepEtValue)) {
                        mStep = stepEtValue
                        refreshView()
                    } else {
                        if (mSection % 1f == 0f) mStep.toInt() + mSection.toInt() else BigDecimal(mStep.toString()) + BigDecimal(mSection.toString())
                        if (stepEtValue < min) {
                            stepEt.setText("${if (mSection % 1f == 0f)  min.toInt().toString() else min.toString()}")
                        }
                        if (stepEtValue > max) {
                            stepEt.setText("${if (mSection % 1f == 0f)  max.toInt().toString() else max.toString()}")
                        }
                        if (stepEt.isFocusable) {
                            stepEt.setSelection(stepEt.text.length)
                        }
                        inputIllegalListener?.onStepIllegal(stepEtValue)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        } finally {
            typeArray.recycle()
        }
    }

    fun initView(context: Context) {
        root = this
     LayoutInflater.from(context).inflate(R.layout.deiui_stepview, this,true)
        incImageView = root.findViewById(R.id.inc_iv)
        desImageView = root.findViewById(R.id.des_iv)
        stepEt = root.findViewById(R.id.step_tv)
        desImageView.setOnClickListener {
            if (stepEt.text.isNullOrEmpty())
                return@setOnClickListener
            Log.i("stepView", "des")
            if ((BigDecimal(mStep.toString()) + BigDecimal(mSection.toString())).toFloat() >= min) {
                mStep = (BigDecimal(mStep.toString()) - BigDecimal(mSection.toString())).toFloat()
                setStep(mStep)
                stepChangeListener?.onStepChange(mStep)
            }
        }
        incImageView.setOnClickListener {
            if (stepEt.text.isNullOrEmpty())
                return@setOnClickListener
            Log.i("stepView", "inc")
            if ((BigDecimal(mStep.toString()) + BigDecimal(mSection.toString())).toFloat() <= max) {
                mStep = (BigDecimal(mStep.toString()) + BigDecimal(mSection.toString())).toFloat()
                setStep(mStep)
                stepChangeListener?.onStepChange(mStep)
            }
        }
    }

    fun getStep(): Int {
        return mStep.toInt()
    }

    fun getFloatStep(): Float {
        return mStep
    }

    fun setStep(step: Float) {
        if (isStepIllegal(step)) {
            mStep = step
            if (mSection % 1 == 0f) {
                stepEt.setText("${mStep.toInt()}")
            } else {
                stepEt.setText(mStep.toString())
            }
            if (stepEt.isFocusable) {
                stepEt.setSelection(stepEt.text.length)
            }
            refreshView()
        } else {
            inputIllegalListener?.onStepIllegal(step)
        }
    }

    private fun isStepIllegal(value: Float): Boolean {
        if (value > max || value < min) {
            return false
        }
        return value in min..max
    }


    fun setStepEditable(editable: Boolean) {
        stepEt.isFocusable = editable
        stepEt.isFocusableInTouchMode = editable
    }


    /**
     * 刷新增加和减小button状态和步数值
     */
    fun refreshView() {
        desImageView.isEnabled = (mStep != min)

        incImageView.isEnabled = (mStep != max)
                && ("${if (mSection % 1f == 0f) mStep.toInt() + mSection.toInt() else BigDecimal(mStep.toString()) + BigDecimal(mSection.toString())}".length <= maxLength)
    }

    /**
     * 设置步数变化监听
     */
    fun setOnStepChangedListener(listener: OnStepChangeListener) {
        this.stepChangeListener = listener
    }

    fun addStepTextChangeListener(textWatcher: TextWatcher) {
        stepEt.addTextChangedListener(textWatcher)
    }

    /**
     * 输入步数不合理时监听，如小于最小值
     */
    interface InputStepIllegalListener {
        fun onStepIllegal(step: Float)
    }


    /**
     * 步数监听
     */
    interface OnStepChangeListener {
        fun onStepChange(step: Float)
    }
}