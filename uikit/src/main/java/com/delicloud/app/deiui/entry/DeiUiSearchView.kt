package com.delicloud.app.deiui.entry

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.view.MotionEvent

import com.delicloud.app.deiui.R

/**
 * Created by Irvin
 * on 2017/8/14 0014.
 * 右侧带图标，用于删除文字，作搜索
 */

class DeiUiSearchView : AppCompatEditText {

    private var mContext: Context? = null
    private var hintText: String? = null
    private var hintTextSize: Float = 0.toFloat()

    internal val DRAWABLE_LEFT = 0
    internal val DRAWABLE_TOP = 1
    internal val DRAWABLE_RIGHT = 2
    internal val DRAWABLE_BOTTOM = 3
    private var onDropArrowClickListener: OnDropArrowClickListener? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {

        mContext = context
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.DeiUiSearchView, 0, 0
        )
        try {
            hintText = a.getString(R.styleable.DeiUiSearchView_hintText)
            hintTextSize = a.getDimension(
                R.styleable.DeiUiSearchView_hintTextSize, resources.getDimension(R.dimen.deiui_main_edit_hint_text_size)
            )
        } finally {
            a.recycle()
        }

        setHintTextSize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        mContext = context
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.DeiUiSearchView, defStyleAttr, 0
        )
        try {
            hintText = a.getString(R.styleable.DeiUiSearchView_hintText)
            hintTextSize = a.getDimension(
                R.styleable.DeiUiSearchView_hintTextSize, resources.getDimension(R.dimen.deiui_main_edit_hint_text_size)
            )
        } finally {
            a.recycle()
        }

        setHintTextSize()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue 从getTextSize获取的值是px
     * @return setTextSize的值是sp
     */
    private fun px2sp(pxValue: Float): Int {
        val fontScale = mContext!!.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    private fun setHintTextSize() {
        if (TextUtils.isEmpty(hintText)) {
            return
        }
        val s = SpannableString(hintText)
        val textSize = AbsoluteSizeSpan(px2sp(hintTextSize), true)
        s.setSpan(textSize, 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        hint = s
    }

    fun setXHintText(text: String) {
        hintText = text
        setHintTextSize()
    }

    fun setXHintText(id: Int) {
        hintText = mContext!!.resources.getString(id)
        setHintTextSize()
    }

    interface OnDropArrowClickListener {
        fun onDropArrowClick()
    }

    fun setOnDropArrowClickListener(onDropArrowClickListener: OnDropArrowClickListener) {
        this.onDropArrowClickListener = onDropArrowClickListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableRight = compoundDrawables[DRAWABLE_RIGHT]
            if (drawableRight != null) {
                //本次点击事件的x轴坐标，如果>当前控件宽度-控件右间距-drawable实际展示大小
                if (event.x >= width - paddingRight - drawableRight.intrinsicWidth) {
                    //设置点击EditText右侧图标EditText失去焦点，
                    // 防止点击EditText右侧图标EditText获得焦点，软键盘弹出
                    isFocusableInTouchMode = false
                    isFocusable = false
                    if (onDropArrowClickListener != null) {
                        onDropArrowClickListener!!.onDropArrowClick()
                    }
                } else {
                    isFocusableInTouchMode = true
                    isFocusable = true
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
