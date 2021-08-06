package com.delicloud.app.deiui.feedback.dialog

import android.util.Pair
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ListView
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.feedback.adapter.TAdapter
import com.delicloud.app.deiui.feedback.adapter.TAdapterDelegate
import com.delicloud.app.deiui.feedback.adapter.TViewHolder
import com.delicloud.app.deiui.feedback.notice.DeiUiProgressUtil.dismiss
import java.util.*

/**
 * 05-04-03,04
 * 列表弹窗
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=3ee521f0-c1de-4b22-8be4-953aca53446f
 */
class DeiUiListItemDialogFragment @JvmOverloads  constructor(builder: Builder=Builder()) :
    BaseDialogFragment<DeiUiListItemDialogFragment.Builder>(builder) {

    private var titleView: View? = null

    private var titleBtn: ImageButton? = null

    private var listView: ListView? = null

    private fun initAdapter() {
        mBuilder.setAdapter(
            TAdapter(context, mBuilder.itemTextList, object : TAdapterDelegate {
                override fun getViewTypeCount(): Int {
                    return mBuilder.itemListenerList.size
                }


                override fun viewHolderAtPosition(position: Int): Class<out TViewHolder> {
                    return DeiUiListDialogViewHolder::class.java
                }

                override fun enabled(position: Int): Boolean {
                    return true
                }
            }),
            OnItemClickListener { parent, view, position, id ->
                mBuilder.itemListenerList[position].onClick()
                dismiss()
            })
    }


    override fun initView() {
        initAdapter()
        titleView = rootView.findViewById(R.id.easy_dialog_title_view)
        titleBtn = rootView.findViewById<View>(R.id.easy_dialog_title_button) as ImageButton
        takeIf { mBuilder.titleBtnVisible }?.run {
            titleBtn?.visibility = View.VISIBLE
        }
        mBuilder.titleListener?.run {
            titleBtn?.setOnClickListener(this)
        }
        listView = rootView.findViewById<View>(R.id.easy_dialog_list_view) as ListView
        if (mBuilder.titleStr == null && mBuilder.messageStr == null) {
            //标题和描述信息都不可见时，去掉标题描述信息，更改文字样式
            titleView?.visibility = View.GONE
            DeiUiListDialogViewHolder.TEXT_APPEARANCE_RES_ID =
                R.style.deiui_list_without_title_dialog_message_text_style
        } else {
            DeiUiListDialogViewHolder.TEXT_APPEARANCE_RES_ID =
                R.style.deiui_list_with_title_dialog_message_text_style
        }
        if (mBuilder.itemSize > 0) {
            updateListView()
        }
    }


    fun clearData() {
        mBuilder.itemTextList.clear()
        mBuilder.itemListenerList.clear()
        mBuilder.setItemSize(0)
    }

    private fun updateListView() {
        mBuilder.listAdapter?.notifyDataSetChanged()
        listView?.adapter = mBuilder.listAdapter
        listView?.onItemClickListener = mBuilder.itemListener
    }

    interface onSeparateItemClickListener {
        fun onClick()
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {
        var titleListener: View.OnClickListener? = null
            private set
        var titleBtnVisible = false
            private set
        var itemSize = 0
            private set
        var itemListener: OnItemClickListener? = null
            private set
        var defaultColor = R.color.deiui_high_level_text_color
            private set
        val itemTextList = LinkedList<Pair<String, Int>>()

        val itemListenerList = LinkedList<onSeparateItemClickListener>()
        var listListener: View.OnClickListener? = null
            private set

        var listAdapter: BaseAdapter? = null
            private set

        init {
            resView = R.layout.deiui_alert_dialog_with_listview
        }

        fun setItemSize(size: Int): Builder {
            this.itemSize = size
            return this
        }

        fun setTitleBtnVisible(visible: Boolean, listener: View.OnClickListener? = null): Builder {
            titleBtnVisible = visible
            titleListener = listener
            return this
        }

        fun setAdapter(adapter: BaseAdapter, listener: View.OnClickListener?): Builder {
            listAdapter = adapter
            listListener = listener
            itemListener = OnItemClickListener { parent, view, position, id ->
                dismiss()
                listListener?.onClick(view)
            }
            return this
        }

        fun setAdapter(adapter: BaseAdapter, listener: OnItemClickListener?): Builder {
            listAdapter = adapter
            itemListener = listener
            return this
        }

        fun addItem(itemText: String, listener: onSeparateItemClickListener?): Builder {
            addItem(itemText, defaultColor, listener)
            return this
        }

        fun addItem(itemText: String, color: Int, listener: onSeparateItemClickListener?): Builder {
            itemTextList.add(Pair(itemText, color))
            itemListenerList.add(listener ?: object : onSeparateItemClickListener {
                override fun onClick() {

                }
            })
            itemSize = itemTextList.size
            return this
        }


        fun addItemAfterAnother(
            itemText: String,
            another: String,
            listener: onSeparateItemClickListener?
        ): Builder {
            val index = itemTextList.indexOfFirst { it.first == another }
            itemTextList.add(index + 1, Pair(itemText, defaultColor))
            itemListenerList.add(index + 1, listener ?: object : onSeparateItemClickListener {
                override fun onClick() {

                }
            })
            itemSize = itemTextList.size
            return this
        }


        fun build(): DeiUiListItemDialogFragment {
            dialogFragment = DeiUiListItemDialogFragment(this)
            return dialogFragment as DeiUiListItemDialogFragment
        }
    }
}
