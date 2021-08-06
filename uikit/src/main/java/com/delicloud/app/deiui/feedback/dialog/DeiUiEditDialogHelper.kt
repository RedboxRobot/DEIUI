package com.delicloud.app.deiui.feedback.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.DialogFragment

/**
 *
 * Created by Irvin
 * Date: on 2018/7/6 0006.
 */

object DeiUiEditDialogHelper {


    /**
     * 输入弹窗
     */
    fun createEditDialog(
        context: Context?,
        titleString: CharSequence,
        hintString: CharSequence,
        confirmString: CharSequence,
        cancelString: CharSequence,
        cancelable: Boolean,
        listener: OnDialogActionListener
    ): DeiUiEditDialogFragment {
        val builder = DeiUiEditDialogFragment.Builder()
        builder
            .setTitle(titleString as String)
            .setEditHint(hintString as String)
            .isCancelable(cancelable)
            .addNegativeBtn(cancelString.toString(), object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    builder.dialogFragment.dismiss()
                    listener.doCancelAction()
                }
            }).addEditPositiveBtn(
                confirmString.toString(),
                listener = object :DeiUiEditDialogFragment.OnPositiveClickListener {
                    override fun onClick(dialogFragment: DeiUiEditDialogFragment, msg: String?) {
                        builder.dialogFragment.dismiss()
                        listener.doOkAction(msg)
                    }
                })
        return builder.build()
    }

    /**
     * 输入框默认值弹窗
     */
    fun createEditDialog(
        context: Context?,
        titleString: CharSequence,
        hintString: CharSequence,
        editString: CharSequence,
        cancelable: Boolean,
        listener: OnDialogActionListener
    ): DeiUiEditDialogFragment {
        val builder = DeiUiEditDialogFragment.Builder()
        builder.setTitle(titleString as String)
        if (!TextUtils.isEmpty(editString)) {
            builder.setEditHint(hintString as String)
        }
        builder.setEditString(editString as String)
            .isCancelable(cancelable)
            .addNegativeBtn(listener = object : BaseDialogFragment.OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    builder.dialogFragment.dismiss()
                    listener.doCancelAction()
                }
            }).addEditPositiveBtn(listener = object : DeiUiEditDialogFragment.OnPositiveClickListener {
                override fun onClick(dialogFragment: DeiUiEditDialogFragment, msg: String?) {
                    builder.dialogFragment.dismiss()
                    listener.doOkAction(msg)
                }
            })
        return builder.build()
    }

    interface OnDialogActionListener {
        fun doCancelAction()

        fun doOkAction(msg: String?)
    }
}
