package com.delicloud.app.deiui.feedback.dialog

import android.util.Pair
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.feedback.adapter.TViewHolder
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 * 带列表弹窗，列表Item ViewHolder
 */
class DeiUiListDialogViewHolder : TViewHolder() {
    private var itemView: AppCompatTextView? = null


    override fun getResId(): Int {
        return R.layout.deiui_list_item_dialog_item
    }

    override fun inflate() {
        itemView = view.findViewById(R.id.custom_dialog_text_view)
        if (itemView != null) {
            setTextViewAttr()
        }

    }


    override fun refresh(item: Any) {
        if (item is Pair<*, *>) {
            val pair = item as Pair<String, Int>
            itemView!!.text = pair.first
            itemView!!.setTextColor(pair.second)
        }
        setTextViewAttr()
    }

    private fun setTextViewAttr() {
        itemView!!.setTextAppearance(itemView!!.context, TEXT_APPEARANCE_RES_ID)
        if (TEXT_APPEARANCE_RES_ID == R.style.deiui_list_with_title_dialog_message_text_style) {
            itemView!!.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            itemView!!.setPadding(0, 0, ScreenUtil.dip2px(23f), 0)
        } else {
            itemView!!.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            itemView!!.setPadding(ScreenUtil.dip2px(23f), 0, 0, 0)
            //设置点击效果
        }
        itemView!!.background =
            ContextCompat.getDrawable(itemView!!.context, R.drawable.deiui_dialog_list_item_selector)
    }

    companion object {

        var TEXT_APPEARANCE_RES_ID = R.style.deiui_list_with_title_dialog_message_text_style
    }

}
