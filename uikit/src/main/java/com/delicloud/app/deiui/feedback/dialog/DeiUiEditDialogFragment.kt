package com.delicloud.app.deiui.feedback.dialog

import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.StringUtil

/**
 * 05-04-06
 * 简单的带有输入框的对话框
 * Created by huangjun on 2015/5/28.股灾
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=aa636628-66d8-4839-8b54-97f8f7ad03fb
 */
class DeiUiEditDialogFragment @JvmOverloads constructor(builder: Builder=Builder()) :
    BaseDialogFragment<DeiUiEditDialogFragment.Builder>(builder) {


    private var mEdit: EditText? = null

    val editMessage: String?
        get() = if (mEdit != null) {
            mEdit!!.editableText.toString()
        } else {
            null
        }

    @kotlin.Deprecated("推荐使用 builder 构建DialogFragment")
    fun setEditHint(hint: String) {
        mBuilder.setEditHint(hint)
        if (!TextUtils.isEmpty(hint)) {
            mEdit?.hint = hint
        }
    }

    @kotlin.Deprecated("推荐使用 builder 构建DialogFragment")
    fun setEditText(EditString: String) {
        mBuilder.setEditString(EditString)
        if (!TextUtils.isEmpty(EditString)) {
            mEdit?.setText(EditString)
            mEdit?.setSelection(EditString.length)
        }
    }

    @kotlin.Deprecated("推荐使用 builder 构建DialogFragment")
    fun setInputType(type: Int) {
        mBuilder.setInputType(type)
        mEdit?.inputType = type
    }

    @kotlin.Deprecated("推荐使用 builder 构建DialogFragment")
    fun setEditTextMaxLength(maxLength: Int) {
        mBuilder.setEditTextMaxLength(maxLength)
        mEdit?.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }


    override fun initView() {
        try {
            val builder = mBuilder
            mEdit = rootView.findViewById<View>(R.id.easy_alert_dialog_edit_text) as EditText
            mEdit?.filters = arrayOf(InputFilter.LengthFilter(builder.mMaxEditTextLength))
            if (builder.editInputType != -1) {
                mEdit?.inputType = builder.editInputType
            }
            if (builder.mEditHint != null) {
                mEdit?.hint = builder.mEditHint
            }

            if (!TextUtils.isEmpty(builder.mEditString)) {
                mEdit?.setText(builder.mEditString)
                if (builder.mEditString!!.length >= 50) {
                    mEdit?.setSelection(50)
                } else {
                    mEdit?.setSelection(builder.mEditString!!.length)
                }
            }
            if (builder.mMaxLines > 0) {
                mEdit!!.maxLines = builder.mMaxLines
            }
            if (builder.mSingleLine) {
                mEdit!!.setSingleLine()
            }
            positiveTv?.setOnClickListener {
                mBuilder.onPositiveClickListener?.onClick(this, mEdit?.text?.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    class EditTextWatcher(
        private val editText: EditText?,
        private val lengthTV: TextView?,
        private val maxLength: Int,
        show: Boolean
    ) : TextWatcher {

        private var show = false

        init {
            this.show = show
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (editText == null) {
                return
            }
            var editStart = editText.selectionStart
            var editEnd = editText.selectionEnd
            editText.removeTextChangedListener(this)
            while (StringUtil.counterChars(s.toString()) > maxLength) {
                s.delete(editStart - 1, editEnd)
                editStart--
                editEnd--
            }
            editText.setSelection(editStart)
            editText.addTextChangedListener(this)
            if (show && lengthTV != null) {
                val remainLength = (maxLength - StringUtil.counterChars(s.toString())).toLong()
                lengthTV.text = "" + remainLength / 2
                lengthTV.visibility = View.VISIBLE
            }
        }
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {
        var mEditHint: String? = null
            private set

        var mEditString: String? = null
            private set

        var mMaxEditTextLength: Int = 0
            private set

        var mMaxLines = 0
            private set

        var mSingleLine = false
            private set

        var mShowEditTextLength = false
            private set

        var editInputType = -1
            private set
        var onPositiveClickListener: OnPositiveClickListener? = null

        init {
            mMaxEditTextLength = 50
            resView = R.layout.deiui_alert_dialog_with_edit_text
            resStyle = R.style.deiui_sdk_share_dialog
        }


        fun setInputType(type: Int): Builder {
            this.editInputType = type
            return this
        }

        fun setEditTextMaxLength(maxLength: Int): Builder {
            this.mMaxEditTextLength = maxLength
            this.mShowEditTextLength = true
            return this
        }

        fun setEditTextMaxLines(maxLines: Int): Builder {
            this.mMaxLines = maxLines
            return this
        }

        fun setEditTextSingleLine(): Builder {
            this.mSingleLine = true
            return this
        }

        fun setEditString(editStr: String): Builder {
            this.mEditString = editStr
            return this
        }

        fun setEditHint(editHint: String): Builder {
            this.mEditHint = editHint
            return this
        }

        fun build(): DeiUiEditDialogFragment {
            dialogFragment = DeiUiEditDialogFragment(this)
            return dialogFragment as DeiUiEditDialogFragment
        }

        @JvmOverloads
        fun addEditPositiveBtn(
            positiveStr: String = "确定",
            listener: OnPositiveClickListener
        ): Builder {
            this.positiveStr = positiveStr
            this.onPositiveClickListener = listener
            return this
        }


    }

    interface OnPositiveClickListener {
        fun onClick(dialogFragment: DeiUiEditDialogFragment, msg: String?)
    }

}
