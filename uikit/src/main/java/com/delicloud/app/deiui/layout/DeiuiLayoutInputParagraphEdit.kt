package com.delicloud.app.deiui.layout

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.delicloud.app.deiui.R
import kotlinx.android.synthetic.main.deiui_layout_input_paragraph_constraintlayout.view.tv_des_number
import kotlinx.android.synthetic.main.deiui_layout_input_paragraph_constraintlayout.view.et_content
import kotlinx.android.synthetic.main.deiui_layout_input_paragraph_constraintlayout.view.v_bottom
import kotlinx.android.synthetic.main.deiui_layout_input_paragraph_constraintlayout.view.v_top
import java.util.regex.Pattern


/**
 * 布局目录-表单内容输入-段落文本输入
 * 01-04
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=c6468a51-8166-470c-8a05-50ba3afc4661
 * @author wangchangwei
 * @create 2019/7/19 9:32
 * @Describe
 */
class DeiuiLayoutInputParagraphEdit : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { init(context, attrs) }
    }

    //returns dp(dp) dimension value in pixels
    fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    fun init(context: Context, attrs: AttributeSet) {

        View.inflate(context, R.layout.deiui_layout_input_paragraph_constraintlayout, this)

        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.DeiuiLayoutInputParagraphEdit)

        //输入框内容
        val editContent =
            attr.getString(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphEditContent)
        //输入框hint内容
        val editHintContent =
            attr.getString(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphEditHintContent)


        //是否显示计数器
        val isShowCounter = attr.getBoolean(
            R.styleable.DeiuiLayoutInputParagraphEdit_paragraphIsShowCounter, true
        )
        //计数器的最大值
        val counterMaxNumber =
            attr.getInt(
                R.styleable.DeiuiLayoutInputParagraphEdit_paragraphCounterMax,
                120
            )

        //是否显示上边线
        val isShowTopView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputParagraphEdit_paragraphIsShowTopView, true
        )
        //是否显示下边线
        val isShowBottomView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputParagraphEdit_paragraphIsShowBottomView, true
        )

        //上边线的左偏移值
        val topViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphTopViewLeftMargin, 0)

        //上边线的右偏移值
        val topViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphTopViewRightMargin, 0)

        //下边线的左偏移值
        val bottomViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphBottomViewLeftMargin, 0)

        //下边线的右偏移值
        val bottomViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputParagraphEdit_paragraphBottomViewRightMargin, 0)

        attr.recycle()


        //1.判断是否显示计数器
        if (isShowCounter) tv_des_number.visibility = View.VISIBLE else tv_des_number.visibility = View.GONE

        //2.5 设置边线的显示隐藏或者边距
        if (isShowTopView) v_top.visibility = View.VISIBLE else v_top.visibility = View.GONE
        if (isShowBottomView) v_bottom.visibility = View.VISIBLE else v_bottom.visibility = View.GONE

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

        //输入的正则，中英文数字、符号（，。、！？）
        val KEY_INPUT_REGEX =
            "[A-Za-z0-9- / : ; ( ) “ [ ]{ } # % ^ * + = _ \\ | ~ < >  . , ? !，。？！、：；……“”‘’（）《》—— \\u4e00-\\u9fa5]$"
        // 只允许字母、数字和汉字
        //val KEY_INPUT_REGEX_AZ = "[A-Za-z0-9\\u4e00-\\u9fa5]$"
        //过滤器
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            val input = Pattern.compile(KEY_INPUT_REGEX)
            val inputMatcher = input.matcher(source)
            if (inputMatcher.find()) source else ""
        }


        //3.设置文字
        et_content.filters = arrayOf<InputFilter>(inputFilter, InputFilter.LengthFilter(counterMaxNumber))
        et_content.setText(editContent)
        et_content.hint = editHintContent
        et_content.setSelection(et_content.length())
        tv_des_number.text = "${et_content.length()}/$counterMaxNumber"




        et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isShowCounter) {
                    return
                }
                s.let {
                    if (s != null && s.length > 0) {
                        tv_des_number.setText("${s.length}/$counterMaxNumber")
                    }
                }
            }
        })
    }

    fun getEditTextContent() = et_content.text

    fun setEditFilters(filters : Array<InputFilter>) {
        et_content.filters = filters
    }


}