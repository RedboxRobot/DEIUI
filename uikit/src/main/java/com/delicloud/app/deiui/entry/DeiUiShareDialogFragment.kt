package com.delicloud.app.deiui.entry

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.feedback.dialog.BaseDialogFragment
import org.jetbrains.anko.collections.forEachWithIndex


/**
 *
 * 分享控件
 * Created By Mr.m
 * 2019/7/26
 *
 */

class DeiUiShareDialogFragment(builder:Builder) :
    BaseDialogFragment<DeiUiShareDialogFragment.Builder>(builder) {
    /**
     * 分享item父布局
     */
    lateinit var contentGridLayout: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val window = this.dialog.window
        val lp = window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable())
        return rootView
    }

    override fun initView() {
        titleTv = rootView.findViewById(R.id.share_title_tv)
        (titleTv as TextView).text = mBuilder.titleStr
        contentGridLayout = rootView.findViewById(R.id.share_gridlayout)
        //设置columnCount
        contentGridLayout.columnCount = mBuilder.columnCount
        mBuilder.shareItems.forEachWithIndex { index, item ->
            //新建itemView
            val shareItemView = LayoutInflater.from(context)
                .inflate(R.layout.deiui_item_share_layout, contentGridLayout, false)
            shareItemView.apply {
                findViewById<ImageView>(R.id.item_share_iv).setImageResource(item.appDrawable)
                findViewById<TextView>(R.id.item_share_tv).text = item.appName
                setOnClickListener {
                    (mBuilder as Builder).listener?.onClick(index, item.appName)
                    dismiss()
                }
            }
            //将item添加到contentGridLayout
            contentGridLayout.addView(shareItemView)
        }
    }


    /**
     * 分享应用图片和名称
     */
    class ShareItem(val appDrawable: Int, val appName: String)

    /**
     * 点击监听
     */
    interface OnShareItemClickListener {
        fun onClick(position: Int, appName: String)
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {
        /**
         * title 分享标题
         * columnCount 分享弹窗列数 通常为2或3
         */
        var columnCount = 3
        /**
         * 应用列表
         */
        val shareItems = arrayListOf<ShareItem>()
        var listener: OnShareItemClickListener? = null

        init {
            resView = R.layout.deiui_sharelayout
            resStyle = R.style.DeiUiActionSheetDialogStyle
        }

        fun setColumnCount(columnCount: Int): Builder {
            this.columnCount = columnCount
            return this
        }

        fun setOnShareItemClickListener(listener: OnShareItemClickListener): Builder {
            this.listener = listener
            return this
        }

        fun setItemList(list: List<ShareItem>): Builder {
            shareItems.clear()
            shareItems.addAll(list)
            return this
        }

        fun addItemView(shareItem: ShareItem): Builder {
            shareItems.add(shareItem)
            return this
        }

        fun build(): DeiUiShareDialogFragment {
            dialogFragment = DeiUiShareDialogFragment(this)
            return dialogFragment as DeiUiShareDialogFragment
        }
    }
}
