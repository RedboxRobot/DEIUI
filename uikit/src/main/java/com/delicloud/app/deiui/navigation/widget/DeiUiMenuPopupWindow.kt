package com.delicloud.app.deiui.navigation.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.navigation.model.DeiUiMenuPopupWindowModel
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 *导航栏菜单弹窗
 * Created by Irvin
 * on 2017/8/5.
 */
class DeiUiMenuPopupWindow(context: Activity, list: List<DeiUiMenuPopupWindowModel>, theme: PopItemTheme) :
    PopupWindow() {

    private val mContext: Context
    private val popupList: ListView
    private val windowWidth: Int
    private val popupWidth: Int


    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.deiui_menu_popup_window, null)

        mContext = context
        popupList = contentView.findViewById<View>(R.id.popup_list) as ListView
        popupList.adapter = PopupListAdapter(context, list, theme)
        windowWidth = ScreenUtil.displayWidth
        setContentView(contentView)
        popupWidth = ScreenUtil.dip2px(140f)
        width = popupWidth
        // 设置SelectPicPopupWindow弹出窗体的高
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体可点击
        isFocusable = true
        isOutsideTouchable = true

        // If the inflated view doesn't have a background set,
        // or the popup window itself doesn't have a background
        // set (or has a transparent background) then you won't get a shadow.
        // 必须给popup window设置不透明的背景否则没有立体的阴影
        // ColorDrawable dw = new ColorDrawable(0x80000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismissListener ，设置其他控件变化等操作
        //  setBackgroundDrawable(dw)
        //这里有自定义背景，不需要再设置，设置为透明
        setBackgroundDrawable(ColorDrawable(0x00000000))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 10f
        }
        // 刷新状态
        update()

        // 设置PopupWindow弹出窗体动画效果
        //setAnimationStyle(R.style.AnimationPreview);
    }

    fun setOnItemClick(itemClickListener: AdapterView.OnItemClickListener) {
        popupList.onItemClickListener = itemClickListener
    }


    private inner class PopupListAdapter internal constructor(
        context: Context,
        private val list: List<DeiUiMenuPopupWindowModel>?,
        private val popItemTheme: PopItemTheme
    ) : BaseAdapter() {
        private val inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return list?.size ?: 0
        }

        override fun getItem(position: Int): Any {
            return list!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: Holder
            if (convertView == null) {
                holder = Holder()
                if (popItemTheme == PopItemTheme.IMG_TEXT) {
                    //加载带图片视图
                    convertView = inflater.inflate(R.layout.deiui_menu_popup_window_item_img_text, null)
                    holder.imageItem = convertView!!.findViewById<View>(R.id.menu_item_image) as ImageView
                } else {
                    //加载仅含文字视图
                    convertView = inflater.inflate(R.layout.deiui_menu_popup_window_item_text, null)
                }
                holder.textItem = convertView!!.findViewById<View>(R.id.menu_item_text) as AppCompatTextView
                convertView.minimumWidth = windowWidth
                convertView.tag = holder

            } else {
                holder = convertView.tag as Holder
            }
            if (popItemTheme == PopItemTheme.IMG_TEXT)
                holder.imageItem!!.setImageResource(list!![position].imageResource)
            holder.textItem!!.setText(list!![position].textInt)
            holder.textItem!!.setTextColor(list[position].textColor)
            return convertView
        }

        internal inner class Holder {
            var imageItem: ImageView? = null
            var textItem: TextView? = null
        }
    }

    /**
     * 弹出菜单主题，仅文字和文字图片
     */
    enum class PopItemTheme {
        TEXT, IMG_TEXT
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    fun showPopupWindowAsDefault(parent: View) {
        if (!isShowing) {
            //View anchor, int xoff, int yoff
            // 相对某个控件的位置有偏移;
            // xoff表示x轴的偏移，正值表示向左，负值表示向右；
            // yoff表示相对y轴的偏移，正值是向下，负值是向上；
            showAsDropDown(
                parent,
                windowWidth - popupWidth - ScreenUtil.dip2px(10f), -12
            )
        } else {
            dismiss()
        }
    }
}
