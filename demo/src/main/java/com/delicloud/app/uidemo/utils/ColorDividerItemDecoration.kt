package com.delicloud.app.uidemo.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView分割线
 * color 颜色
 */
class ColorDividerItemDecoration(color: Int) : RecyclerView.ItemDecoration() {

    private var mDividerHeight: Float = 0.toFloat()
    var mDividerMarginLeft = 0
    var mDividerMarginRight = 0
    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = color
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //        //第一个ItemView不需要在上面绘制分割线
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码为1px
            outRect.top = 1
            mDividerHeight = 1f
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)

            val index = parent.getChildAdapterPosition(view)
            //第一个ItemView不需要绘制
            if (index == 0) {
                continue
            }
            //divider位置
            val dividerTop = view.top - mDividerHeight
            val dividerLeft = parent.paddingLeft.toFloat() + mDividerMarginLeft
            val dividerBottom = view.top.toFloat()
            val dividerRight = (parent.width - parent.paddingRight - mDividerMarginRight).toFloat()

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint)
        }
    }
}
