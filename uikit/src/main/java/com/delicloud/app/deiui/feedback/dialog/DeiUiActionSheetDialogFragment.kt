package com.delicloud.app.deiui.feedback.dialog


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.ScreenUtil
import org.jetbrains.anko.textColor
import java.util.*


/**
 * 05-01-05
 * 底部弹出选择框
 * ActionSheetDialog
 *弹窗目录-系统弹窗-底部弹窗
 *https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=f5f8d79a-9d45-41cf-8b94-a48e0c6a8ee2
 * Copy from internet thanks for share
 *
 * @author Irvin 2017-8-23
 */
class DeiUiActionSheetDialogFragment @JvmOverloads constructor(builder: Builder=Builder()) :
    BaseDialogFragment<DeiUiActionSheetDialogFragment.Builder>(builder) {
    private var txt_title: AppCompatTextView? = null
    private var txt_cancel: AppCompatTextView? = null
    private var lLayout_content: LinearLayout? = null
    private var sLayout_content: ScrollView? = null
    private var isUserCancel = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // 设置Dialog最小宽度为屏幕宽度

        rootView.minimumWidth = getDisplay().width
        // 获取自定义Dialog布局中的控件
        sLayout_content = rootView.findViewById<View>(R.id.sLayout_content) as ScrollView
        lLayout_content = rootView
            .findViewById<View>(R.id.lLayout_content) as LinearLayout
        txt_title = rootView.findViewById(R.id.txt_title)
        txt_cancel = rootView.findViewById(R.id.txt_cancel)
        txt_cancel!!.setOnClickListener { dialog!!.dismiss() }
        val dialogWindow = dialog!!.window
        dialogWindow!!.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        val lp = dialogWindow.attributes
        lp.width = LayoutParams.MATCH_PARENT
        lp.x = 0
        lp.y = 0
        dialogWindow.attributes = lp
        return rootView
    }

    override fun initView() {
        setSheetItems()
        if (mBuilder.titleStr != null) {
            setTitle(mBuilder.titleStr.toString(), mBuilder.titleTextColor)
        }
        txt_cancel?.setTextColor(Color.parseColor(mBuilder.cancelTextColor.name))
    }


    fun  setTitle(title: String, color: SheetItemColor?) {
        mBuilder.setTitle(title,color)
        txt_title?.apply {
            visibility = View.VISIBLE
            text = title
            textColor =
                if (color != null) Color.parseColor(color.name) else ContextCompat.getColor(
                    context,
                    R.color.deiui_actionsheet_gray
                )
        }
    }


    /**
     * 设置条目布局
     */
    private fun setSheetItems() {
        val sheetItemList = (mBuilder ).sheetItemList
        if (sheetItemList == null || sheetItemList.size <= 0) {
            return
        }
        val size = sheetItemList.size

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            val params = sLayout_content!!
                .layoutParams as LayoutParams
            params.height = getDisplay().height / 2
            sLayout_content!!.layoutParams = params
        }

        // 循环添加条目
        for (i in 1..size) {
            val sheetItem = sheetItemList[i - 1]
            val strItem = sheetItem.name
            val color = sheetItem.color
            val listener = sheetItem.itemClickListener

            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 19f
            textView.gravity = Gravity.CENTER

            // 背景图片
            if (size == 1) {
                if (mBuilder.titleStr != null) {
                    textView.setBackgroundResource(R.drawable.deiui_actionsheet_bottom_selector)
                } else {
                    textView.setBackgroundResource(R.drawable.deiui_actionsheet_single_selector)
                }
            } else {
                if (mBuilder.titleStr != null) {
                    if (i in 1 until size) {
                        textView.setBackgroundResource(R.drawable.deiui_actionsheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.deiui_actionsheet_bottom_selector)
                    }
                } else {
                    when {
                        i == 1 -> textView.setBackgroundResource(R.drawable.deiui_actionsheet_top_selector)
                        i < size -> textView.setBackgroundResource(R.drawable.deiui_actionsheet_middle_selector)
                        else -> textView.setBackgroundResource(R.drawable.deiui_actionsheet_bottom_selector)
                    }
                }
            }

            // 字体颜色
            if (color == null) {
                textView.setTextColor(
                    Color.parseColor(
                        SheetItemColor.Blue
                            .name
                    )
                )
            } else {
                textView.setTextColor(Color.parseColor(color.name))
            }

            // 高度
            val height =ScreenUtil.dip2px(58f)
            textView.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, height
            )

            // 点击事件
            textView.setOnClickListener {
                isUserCancel = false
                listener.onClick(i)
                dialog!!.dismiss()
            }
            lLayout_content!!.addView(textView)
        }
    }

    fun getDisplay(): Display {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay
    }

    interface OnSheetItemClickListener {
        fun onClick(which: Int)
    }


    class SheetItem internal constructor(
        internal var name: String, internal var color: SheetItemColor?,
        internal var itemClickListener: OnSheetItemClickListener
    )

    enum class SheetItemColor constructor(var color: String?) {
        Blue("#007AFF"), Red("#FF3B30"), Gray("#999999")
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {

        var cancelTextColor = SheetItemColor.Blue
        var titleTextColor: SheetItemColor? = null
        var sheetItemList: MutableList<SheetItem>? = null

        init {
            resView = R.layout.deiui_toast_view_actionsheet
            resStyle = R.style.DeiUiActionSheetDialogStyle
        }

        fun setTitle(titleStr: String, color: SheetItemColor?): Builder {
            this.titleStr = titleStr
            this.titleTextColor = color
            return this
        }

        fun setCancelTextColor(color: SheetItemColor): Builder {
            this.cancelTextColor = color
            return this
        }

        fun build(): DeiUiActionSheetDialogFragment {
            dialogFragment = DeiUiActionSheetDialogFragment(this)
            return dialogFragment as DeiUiActionSheetDialogFragment
        }

        /**
         * @param strItem  条目名称
         * @param color    条目字体颜色，设置null则默认蓝色
         * @param listener
         * @return
         */
        fun addSheetItem(
            strItem: String, color: SheetItemColor,
            listener: OnSheetItemClickListener
        ): Builder {
            if (sheetItemList == null) {
                sheetItemList = ArrayList()
            }
            sheetItemList!!.add(SheetItem(strItem, color, listener))
            return this
        }
    }
}