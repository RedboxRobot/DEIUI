package com.delicloud.app.deiui.layout

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.delicloud.app.deiui.R
import kotlinx.android.synthetic.main.deiui_layout_input_single_constraintlayout.view.*
import org.jetbrains.anko.leftPadding


/**
 * 布局目录-表单内容输入-（两端或左对齐）的组合布局
 * 01-04
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=c6468a51-8166-470c-8a05-50ba3afc4661
 * @author wangchangwei
 * @create 2019/7/16 9:24
 * @Describe
 */
class DeiuiLayoutInputSingleEditText : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { init(context, attrs) }
    }

    //returns dp(dp) dimension value in pixels
    fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    fun init(context: Context, attrs: AttributeSet) {

        View.inflate(context, R.layout.deiui_layout_input_single_constraintlayout, this)

        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.DeiuiLayoutInputSingleEditText)

        //文字内容
        val textContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditText_singleTextContent)
        //输入框内容
        val editContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditText_singleEditContent)
        //输入框hint内容
        val editHintContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditText_singleEditHintContent)

        //输入框的文字内容是否可以修改
        val editContentHasAmend =
            attr.getBoolean(
                R.styleable.DeiuiLayoutInputSingleEditText_singleEditContentHasAmend,
                true
            )

        //显示类型
        val showType =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleShowType, 0)
        //对齐方式
        val alignType =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleAlignType, 0)

        //输入框的左边是否是根据guideline比例固定的
        val isEditLeftGuidePercent =
            attr.getBoolean(
                R.styleable.DeiuiLayoutInputSingleEditText_singleIsEditLeftGuidePercent,
                false
            )
        //左边guideline的偏移值
        val editLeftGuidePercentBias =
            attr.getFloat(
                R.styleable.DeiuiLayoutInputSingleEditText_singleEditLeftGuidePercentBias,
                0.2f
            )

        //是否显示上边线
        val isShowTopView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputSingleEditText_singleIsShowTopView, false
        )
        //是否显示下边线
        val isShowBottomView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputSingleEditText_singleIsShowBottomView, false
        )

        //上边线的左偏移值
        val topViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleTopViewLeftMargin, 0)

        //上边线的右偏移值
        val topViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleTopViewRightMargin, 0)

        //下边线的左偏移值
        val bottomViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleBottomViewLeftMargin, 0)

        //下边线的右偏移值
        val bottomViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditText_singleBottomViewRightMargin, 0)

        attr.recycle()

        //1.先判断showtype的类型,共支持三种类型的显示：纯文字-文字和删除按钮-文字和输入框
        when (showType) {
            0 -> {//单输入框
                tv_title.visibility = View.GONE
                et_content.visibility = View.VISIBLE
                ib_delete.visibility = View.GONE
            }
            1 -> {//输入框和删除按钮
                tv_title.visibility = View.GONE
                et_content.visibility = View.VISIBLE
            }
            2 -> {//文字和输入框
                tv_title.visibility = View.VISIBLE
                et_content.visibility = View.VISIBLE
                ib_delete.visibility = View.GONE
            }
            3 -> {//文字和输入框和删除按钮
                tv_title.visibility = View.VISIBLE
                et_content.visibility = View.VISIBLE
            }
        }

        //2.再判断对齐方式
        when (alignType) {
            0 -> {//左对齐
                val layoutParams = et_content.layoutParams as ConstraintLayout.LayoutParams
                et_content.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                if (isEditLeftGuidePercent) {
                    layoutParams.startToEnd = guideline.id
                    guideline.setGuidelinePercent(editLeftGuidePercentBias)
                } else {
                    if (showType == 2 || showType == 3) et_content.leftPadding = context.dp(15)
                    layoutParams.startToEnd = tv_title.id
                }

            }
            1 -> {//两端对齐
                ib_delete.visibility = View.GONE
                et_content.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }

        }

        //2.5 设置边线的显示隐藏或者边距
        if (isShowTopView) v_top.visibility = View.VISIBLE else  v_top.visibility = View.GONE
        if (isShowBottomView) v_bottom.visibility = View.VISIBLE else  v_bottom.visibility = View.GONE

        if (topViewLeftMargin != 0) {
            val layoutParams = v_top.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginStart = context.dp(topViewLeftMargin)
        }
        if (topViewRightMargin != 0) {
            val layoutParams = v_top.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginEnd = context.dp(topViewRightMargin)
        }

        if (bottomViewLeftMargin != 0) {
            val layoutParams = v_bottom.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginStart = context.dp(bottomViewLeftMargin)
        }
        if (bottomViewRightMargin != 0) {
            val layoutParams = v_bottom.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginEnd = context.dp(bottomViewRightMargin)
        }


        //3.设置文字
        tv_title.text = textContent
        et_content.setText(editContent)
        et_content.hint = editHintContent
        et_content.setSelection(et_content.length())

        //输入框内容不能修改，标题和输入框颜色改变
        if (!editContentHasAmend) {
            et_content.clearFocus()
            et_content.setFocusable(false)
            et_content.setTextColor(resources.getColor(R.color.color_99))
            tv_title.setTextColor(getResources().getColor(R.color.color_99))
        } else {
            if (editContent != null && editContent.length > 0) ib_delete.visibility = View.VISIBLE
        }

        et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (showType == 0 || showType == 2) {
                    return
                }
                s.let {
                    if (s != null && s.isNotEmpty()) ib_delete.visibility = View.VISIBLE else ib_delete.visibility =
                        View.GONE
                }
            }
        })

        //点击事件
        ib_delete.setOnClickListener {
            et_content.setText("")
        }
    }

    fun getEditTextContent() = et_content.text


}