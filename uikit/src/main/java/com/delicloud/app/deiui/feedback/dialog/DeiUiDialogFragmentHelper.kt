package com.delicloud.app.deiui.feedback.dialog

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.DialogFragment
import com.delicloud.app.deiui.R


object DeiUiDialogFragmentHelper {


    /**
     * 单个按钮弹窗
     * @param mContext
     * @param titleString
     * @param msgString
     * @param btnString
     * @param cancelable
     * @param positiveListener
     * @return
     */
    fun createOneButtonDialog(
        mContext: Context?,
        titleString: CharSequence?,
        msgString: CharSequence?,
        btnString: CharSequence = "确定",
        cancelable: Boolean = true,
        positiveListener: OnClickListener?
    ): DeiUiDialogFragment {
        val builder = DeiUiDialogFragment.Builder()
        configOneButtonDialog(
            builder,
            titleString,
            msgString,
            btnString,
            cancelable,
            positiveListener
        )
        return builder.build()
    }

    /**
     * 填充单按钮弹窗文字和监听参数
     *@param  builder
     * @param titleString
     * @param msgString
     * @param btnString
     * @param cancelable
     * @param positiveListener
     */
    private fun configOneButtonDialog(
        builder: DeiUiDialogFragment.Builder,
        titleString: CharSequence? = null,
        msgString: CharSequence? = null,
        btnString: CharSequence,
        cancelable: Boolean,
        positiveListener: OnClickListener?
    ) {
        takeIf { titleString != null }?.run { builder.setTitle(titleString.toString()) }
        takeIf { msgString != null }?.run { builder.setMessage(msgString.toString()) }
        builder.isCancelable(cancelable)
            .addPositiveBtn(btnString.toString(), object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    builder.dialogFragment.dismiss()
                    positiveListener?.onClick(view)
                }
            })
    }

    /**
     *
     * 填充双按钮弹窗参数
     * @param dialog
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     */
    private fun <T : BaseDialogFragment.Builder<T>> configDoubleButtonDialog(
        builder: BaseDialogFragment.Builder<T>,
        title: CharSequence?,
        message: CharSequence?,
        okStr: CharSequence,
        cancelStr: CharSequence,
        cancelable: Boolean,
        listener: OnDialogActionListener?
    ) {
        val okListener = OnClickListener {
            builder.dialogFragment.dismiss()
            listener?.doOkAction()
        }
        val cancelListener = OnClickListener {
            builder.dialogFragment.dismiss()
            listener?.doCancelAction()
        }
        if (title != null) {
            builder.setTitle(title.toString())
        }
        if (message != null) {
            builder.setMessage(message.toString())
        }
        builder.isCancelable(cancelable)
            .addPositiveBtn(okStr.toString(), object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    okListener.onClick(view)
                }
            })
            .addNegativeBtn(cancelStr.toString(), object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    cancelListener.onClick(view)
                }
            })
    }

    /**
     *05-04-05
     * 单按钮带图弹窗,图像位于顶部居中
     *https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=a991aa25-df3a-4218-aa43-eda289b41200
     * @param mContext
     * @param titleString
     * @param msgString
     * @param btnString
     * @param cancelable
     * @param positiveListener
     * @return
     */
    fun createOneButtonWithImgDialog(
        mContext: Context?,
        titleString: CharSequence,
        msgString: CharSequence,
        btnString: CharSequence = "确定",
        cancelable: Boolean = true,
        positiveListener: OnClickListener?
    ): DeiUiDialogFragment {
        val builder =
            DeiUiDialogFragment.Builder().view(R.layout.deiui_alert_dialog_with_img)
        configOneButtonDialog(
            builder,
            titleString,
            msgString,
            btnString,
            cancelable,
            positiveListener
        )
        return builder.build()
    }

    /**
     * 05-04-05
     * 双按钮带大图弹窗
     * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=bcbd1895-2e7b-4998-83a2-03f9b06ff079
     * @param mContext
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return
     */

    fun createDoubleButtonWithBigImgDialog(
        mContext: Context?,
        title: CharSequence,
        message: CharSequence,
        okStr: CharSequence = "确定",
        cancelStr: CharSequence = "取消",
        cancelable: Boolean,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        val builder =
            DeiUiDialogFragment.Builder().view(R.layout.deiui_alert_dialog_with_big_img) as DeiUiDialogFragment.Builder
        configDoubleButtonDialog(builder, title, message, okStr, cancelStr, cancelable, listener)
        return builder.build()
    }

    /**
     * 05-04-07
     * 引用弹窗，描述信息左侧有图像
     * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=21f40269-1e76-4523-8e7f-76c171f6cf88
     * @param mContext
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return
     */
    fun createDoubleButtonQuoteDialog(
        mContext: Context?,
        title: CharSequence,
        message: CharSequence,
        okStr: CharSequence = "确定",
        cancelStr: CharSequence = "取消",
        cancelable: Boolean = true,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        val builder =
            DeiUiDialogFragment.Builder()
                .view(R.layout.deiui_alert_dialog_quote)
        configDoubleButtonDialog(builder, title, message, okStr, cancelStr, cancelable, listener)
        return builder.build()
    }

    /**
     * 05-04-09
     * 创建活动运营弹窗
     * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=ddcf676e-b97d-4c0d-8024-cda429550c26
     * @param mContext
     * @param title
     * @param message
     * @param okStr
     * @param cancelable
     * @param positiveListener
     * @return
     */
    fun createActivityOperationDialog(
        mContext: Context?, title: CharSequence, message: CharSequence,
        okStr: CharSequence = "确定", cancelable: Boolean = true, positiveListener: OnClickListener?
    ): DeiUiDialogFragment {
        val builder =
            DeiUiDialogFragment.Builder().view(R.layout.deiui_alert_dialog_activity)
        configOneButtonDialog(
            builder,
            title,
            message,
            okStr,
            cancelable,
            positiveListener
        )
        return builder.build()
    }

    /**
     * 05-04-10
     * 水平进度条弹窗
     * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=2b182e3a-d0a9-48e4-90fe-4dceb2b7a579
     * @param mContext
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param listener
     * @return
     */
    fun createDoubleButtonHorizontalProgressDialog(
        mContext: Context?,
        title: CharSequence,
        message: CharSequence,
        okStr: CharSequence = "确定",
        cancelStr: CharSequence = "取消",
        maxProgress: Int = 100,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        val builder = DeiUiDialogFragment.Builder()
        configDoubleButtonDialog(builder, title, message, okStr, cancelStr, false, listener)
        builder.setMaxProgress(maxProgress)
            .setProgress(0)
        //初始化进度为0
        return builder.build()
    }

    fun createOneButtonHorizontalProgressDialog(
        mContext: Context?,
        title: CharSequence,
        message: CharSequence,
        cancelStr: CharSequence = "取消",
        maxProgress: Int = 100,
        listener: OnClickListener?
    ): DeiUiDialogFragment {
        val builder = DeiUiDialogFragment.Builder()
        configOneButtonDialog(builder, title, message, cancelStr, false, listener)
        builder.setMaxProgress(maxProgress)
            .setProgress(0)
        return builder.build()
    }

    /**
     * 05-04-02，04
     * 两个按钮的dialog
     * @param context
     * @param title      根据title是否为空设置有无标题
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return DeiUiDialogFragment
     */
    private fun createDoubleButtonDialog(
        context: Context?,
        title: CharSequence?,
        message: CharSequence,
        okStr: CharSequence,
        cancelStr: CharSequence,
        cancelable: Boolean,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        val builder = DeiUiDialogFragment.Builder()
        configDoubleButtonDialog(builder, title, message, okStr, cancelStr, cancelable, listener)
        return builder.build()
    }

    /**
     * 05-04-02
     * 双按钮带标题信息弹窗
     *  * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=699bb1b7-7447-4c32-98fe-1526e95f6867
     * @param context
     * @param title
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return
     */

    fun createDoubleButtonWithTitleDialog(
        context: Context?,
        title: CharSequence,
        message: CharSequence,
        okStr: CharSequence = "确定",
        cancelStr: CharSequence = "取消",
        cancelable: Boolean = true,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        return createDoubleButtonDialog(
            context,
            title,
            message,
            okStr,
            cancelStr,
            cancelable,
            listener
        )
    }

    /**
     * 05-04-04
     * 双按钮不带标题
     * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=692e3030-5855-44c5-85c1-65c4ea017159
     * @param context
     * @param message
     * @param okStr
     * @param cancelStr
     * @param cancelable
     * @param listener
     * @return
     */

    fun createDoubleButtonWithoutTittleDialog(
        context: Context,
        message: CharSequence,
        okStr: CharSequence = "确定",
        cancelStr: CharSequence = "取消",
        cancelable: Boolean = true,
        listener: OnDialogActionListener?
    ): DeiUiDialogFragment {
        return createDoubleButtonDialog(
            context,
            null,
            message,
            okStr,
            cancelStr,
            cancelable,
            listener
        )
    }


    interface OnDialogActionListener {
        fun doCancelAction()

        fun doOkAction()
    }

    private fun getString(context: Context, id: Int): String? {
        return if (id == 0) {
            null
        } else context.getString(id)
    }
}
