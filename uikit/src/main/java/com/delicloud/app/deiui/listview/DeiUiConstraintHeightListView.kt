package com.delicloud.app.deiui.listview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import com.delicloud.app.deiui.R

/**
 * 根据Item数目约束高度ListView，用于弹窗中ListView显示
 */
class DeiUiConstraintHeightListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr) {
    private var mMaxHeight = 100f//默认100px

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DeiUiConstraintHeightListView, 0, defStyleAttr)
        val count = array.indexCount
        for (i in 0 until count) {
            val type = array.getIndex(i)
            if (type == R.styleable.DeiUiConstraintHeightListView_listMaxHeight) {
                //获得布局中限制的最大高度
                mMaxHeight = array.getDimension(type, -1f)
            }
        }
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        //获取lv本身高度
        val specSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //限制高度小于lv高度,设置为限制高度
        if (mMaxHeight <= specSize && mMaxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                java.lang.Float.valueOf(mMaxHeight).toInt(),
                MeasureSpec.AT_MOST
            )
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}