package com.delicloud.app.deiui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.delicloud.app.deiui.R
import kotlinx.android.synthetic.main.deiui_layout_input_single_next_constraintlayout.view.et_content
import kotlinx.android.synthetic.main.deiui_layout_input_single_next_constraintlayout.view.guideline
import kotlinx.android.synthetic.main.deiui_layout_input_single_next_constraintlayout.view.tv_title
import kotlinx.android.synthetic.main.deiui_layout_input_single_next_constraintlayout.view.v_bottom
import kotlinx.android.synthetic.main.deiui_layout_input_single_next_constraintlayout.view.v_top
import org.jetbrains.anko.leftPadding


/**
 * 利用类型别名定义空的lambda表达式
 */
typealias ClickListener = () -> Unit

/**
 * 布局目录-表单内容输入-（两端或左对齐）的组合布局,带箭头进入下一页面的
 * 01-04
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=c6468a51-8166-470c-8a05-50ba3afc4661
 * @author wangchangwei
 * @create 2019/7/16 9:24
 * @Describe
 */
class DeiuiLayoutInputSingleEditNext : ConstraintLayout {

    private lateinit var onClickListener: ClickListener

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { init(context, attrs) }
    }

    //returns dp(dp) dimension value in pixels
    fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    fun init(context: Context, attrs: AttributeSet) {
        /*val constraintLayout = layoutParams as ConstraintLayout.LayoutParams

        constraintLayout.height = context.dp(45)
        constraintLayout.width = LayoutParams.MATCH_PARENT*/



        View.inflate(context, R.layout.deiui_layout_input_single_next_constraintlayout, this)

        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.DeiuiLayoutInputSingleEditNext)

        //文字内容
        val textContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextTextContent)
        //输入框内容
        val editContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextEditContent)
        //输入框hint内容
        val editHintContent =
            attr.getString(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextEditHintContent)
        //输入框内容的颜色
        val editContentColor =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextEditContentColor, 0)


        //对齐方式
        val alignType =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextAlignType, 0)

        //输入框的左边是否是根据guideline比例固定的
        val isEditLeftGuidePercent =
            attr.getBoolean(
                R.styleable.DeiuiLayoutInputSingleEditNext_singleNextIsEditLeftGuidePercent,
                false
            )
        //左边guideline的偏移值
        val editLeftGuidePercentBias =
            attr.getFloat(
                R.styleable.DeiuiLayoutInputSingleEditNext_singleNextEditLeftGuidePercentBias,
                0.2f
            )

        //是否显示上边线
        val isShowTopView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputSingleEditNext_singleNextIsShowTopView, false
        )
        //是否显示下边线
        val isShowBottomView = attr.getBoolean(
            R.styleable.DeiuiLayoutInputSingleEditNext_singleNextIsShowBottomView, false
        )

        //上边线的左偏移值
        val topViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextTopViewLeftMargin, 0)

        //上边线的右偏移值
        val topViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextTopViewRightMargin, 0)

        //下边线的左偏移值
        val bottomViewLeftMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextBottomViewLeftMargin, 0)

        //下边线的右偏移值
        val bottomViewRightMargin =
            attr.getInt(R.styleable.DeiuiLayoutInputSingleEditNext_singleNextBottomViewRightMargin, 0)
        attr.recycle()


        //1.判断对齐方式
        when (alignType) {
            0 -> {//左对齐
                val layoutParams = et_content.layoutParams as ConstraintLayout.LayoutParams
                et_content.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                if (isEditLeftGuidePercent) {
                    layoutParams.startToEnd = guideline.id
                    guideline.setGuidelinePercent(editLeftGuidePercentBias)
                } else {
                    et_content.leftPadding = context.dp(15)
                    layoutParams.startToEnd = tv_title.id
                }

            }
            1 -> {//两端对齐
                et_content.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }

        }

        //2.设置文字
        tv_title.text = textContent
        et_content.setText(editContent)
        et_content.hint = editHintContent

        //修改输入框文字的颜色
        if (editContentColor == 1) {
            et_content.setTextColor(resources.getColor(R.color.color_99))
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


        //3.点击事件
        setOnClickListener {
            if (::onClickListener.isInitialized) {
                onClickListener.invoke()
            }
        }
        et_content.setOnClickListener {
            if (::onClickListener.isInitialized) {
                onClickListener.invoke()
            }
        }
    }


    /**
     *点击
     */
    fun setOnViewClickListener(onClickListener: ClickListener) {
        this.onClickListener = onClickListener
    }

    fun getEditTextContent() = et_content.text

    fun setEditTextContent(string: String) {
        et_content.setText(string)
    }


}