package com.delicloud.app.deiui.feedback.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.delicloud.app.deiui.R

/**
 *05-04-08
 * 申请授权弹窗
 *https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=5abd5dc3-2cfd-42e1-8f3f-b0f9833f91f2
 * @author ChengXinPing
 * @time 2018/8/20 9:52
 */
class DeiUiUpgradeDialogFragment @JvmOverloads constructor(builder: Builder = Builder()) :
    BaseDialogFragment<DeiUiUpgradeDialogFragment.Builder>(builder) {

    var mUpgradeHintList: ListView? = null
    private var mUpgradeButton: AppCompatTextView? = null
    private var mContentTitleTv: AppCompatTextView? = null
    private var mUpgradeCancel: ImageView? = null
    private var dialogIv: ImageView? = null


    override fun initView() {
        mUpgradeButton = rootView.findViewById(R.id.dialog_upgrade_button)
        mUpgradeHintList = rootView.findViewById(R.id.dialog_upgrade_hint_list)
        mUpgradeCancel = rootView.findViewById(R.id.dialog_upgrade_cancel)
        titleTv = rootView.findViewById(R.id.dialog_title)
        dialogIv = rootView.findViewById(R.id.dialog_upgrade_iv)
        mContentTitleTv = rootView.findViewById(R.id.dialog_content_title)
        val adapter = UpgradeDialogHintListAdapter(context!!, mBuilder.upgradeHints)
        mUpgradeHintList?.adapter = adapter
        mUpgradeCancel?.visibility = if (mBuilder.isCancelable) View.VISIBLE else View.INVISIBLE
        isCancelable = mBuilder.isCancelable
        mUpgradeCancel?.setOnClickListener {
            mBuilder.actionListener?.onCancel()
            dismiss()
        }
        mUpgradeButton?.setOnClickListener {
            mBuilder.actionListener?.onButtonClick()
            dismiss()
        }
        mBuilder.let {
            titleTv?.text = it.titleStr
            it.logoDrawableRes?.run {
                setLogoDrawableRes(this)
            }
            setBtnStr(it.btnStr)
            setContentTitle(it.contentTitle)
            setBtnBackgroundRes(it.btnBackgroundRes)
        }
    }


    /**
     * 设置弹窗图像drawable
     */
    fun setLogoDrawableRes(@DrawableRes logoDrawableRes: Int) {
        mBuilder.setLogoDrawableRes(logoDrawableRes)
        dialogIv?.setImageDrawable(ContextCompat.getDrawable(context!!, logoDrawableRes))
    }


    fun setContentTitle(contentTitle: String?) {
        contentTitle?.run {
            mBuilder.setContentTitle(this)
        }
        mContentTitleTv?.text = contentTitle
    }

    fun setBtnStr(btnStr: String?) {
        btnStr?.run {
            mBuilder.setBtnStr(this)
        }
        mUpgradeButton?.text = btnStr
    }

    fun setBtnBackgroundRes(resId: Int) {
        mBuilder.setBtnBackgroundRes(resId)
        mUpgradeButton?.setBackgroundResource(resId)
    }


    /**
     * 权限列表适配器
     */
    inner class UpgradeDialogHintListAdapter(
        private val mContext: Context,
        private val list: List<String>?
    ) :
        BaseAdapter() {

        override fun getCount(): Int {
            return list?.size ?: 0
        }

        override fun getItem(i: Int): Any? {
            return if (list == null) null else list[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.deiui_item_dialog_upgrade_hint_list, null)
                viewHolder = ViewHolder()
                viewHolder.itemHint = convertView!!.findViewById(R.id.item_dialog_upgrade_hint)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            viewHolder.itemHint!!.text = list!![position]
            return convertView
        }

        internal inner class ViewHolder {
            var itemHint: AppCompatTextView? = null
        }
    }

    interface OnDialogActionListener {
        fun onButtonClick()

        fun onCancel()
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {
        var upgradeHints: List<String>? = null
            private set
        var contentTitle: String? = null
            private set
        var btnStr: String? = null
            private set
        //弹窗drawable
        var logoDrawableRes: Int? = null
            private set

        var btnBackgroundRes: Int = R.drawable.deiui_high_white_view_touch_ripple
            private set
        var actionListener: OnDialogActionListener? = null
            private set

        init {
            resView = R.layout.deiui_dialog_app_upgrade
        }

        /**
         * 设置弹窗图像drawable
         */
        fun setLogoDrawableRes(logoDrawableRes: Int): Builder {
            this.logoDrawableRes = logoDrawableRes
            return this
        }

        fun setBtnBackgroundRes(res: Int): Builder {
            btnBackgroundRes = res
            return this
        }

        fun setBtnStr(btnStr: String): Builder {
            this.btnStr = btnStr
            return this
        }

        fun setContentTitle(contentTitle: String): Builder {
            this.contentTitle = contentTitle
            return this
        }

        fun setUpgradeList(upgradeHints: List<String>): Builder {
            this.upgradeHints = upgradeHints
            return this
        }

        fun setDialogActionListener(actionListener: OnDialogActionListener): Builder {
            this.actionListener = actionListener
            return this
        }

        fun build(): DeiUiUpgradeDialogFragment {
            dialogFragment = DeiUiUpgradeDialogFragment(this)
            return dialogFragment as DeiUiUpgradeDialogFragment
        }
    }
}
