package com.delicloud.app.deiui.feedback.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.utils.ScreenUtil
import org.jetbrains.anko.textColor

abstract class BaseDialogFragment<T : BaseDialogFragment.Builder<T>>(val mBuilder: T) :
    DialogFragment() {

    lateinit var rootView: View
    var titleTv: AppCompatTextView? = null

    var messageTv: AppCompatTextView? = null

    var positiveTv: AppCompatTextView? = null
    var negativeTv: AppCompatTextView? = null


    /**
     * listeners
     */
    var onDismissListener: DialogInterface.OnDismissListener? = null
        set(value) {
            mBuilder.setOnDismissListener(value)
            field = value
        }
    var onShowListener: DialogInterface.OnShowListener? = null
        set(value) {
            mBuilder.setOnShowListener(value)
            field = value
        }
    var onCancelListener: DialogInterface.OnCancelListener? = null
        set(value) {
            mBuilder.setOnCancelListener(value)
            field = value
        }
    /**
     * views onClickListener
     */
    private var mViewOnClickListeners = hashMapOf<Int, View.OnClickListener>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        mBuilder.onDialogShowListener?.run {
            dialog.setOnShowListener(this)
        }
        mBuilder.onDialogDismissListener?.run {
            dialog.setOnDismissListener(this)
        }
        mBuilder.onDialogCancelListener?.run {
            dialog.setOnCancelListener(this)
        }
        return dialog
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.getBoolean("saved")) {
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("saved", true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initStyleAndSize()
        rootView = inflater.inflate(mBuilder.resView, null)
        return rootView
    }

    open fun initStyleAndSize() {
        setStyle(STYLE_NO_TITLE, mBuilder.resStyle)
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = this.dialog.window
        //去掉dialog默认的padding
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = ScreenUtil.getCustomDialogWidth()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
        window.setBackgroundDrawable(ColorDrawable())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBaseView()
        initView()
    }

    private fun initBaseView() {
        initOnClickListeners()
        titleTv = rootView.findViewById(R.id.dialog_title_tv)
        takeIf { titleTv != null && mBuilder.titleStr != null }?.run {
            setTitle(mBuilder.titleStr!!)
        }
        messageTv = rootView.findViewById(R.id.desc_tv)
        takeIf { messageTv != null && mBuilder.messageStr != null }?.run {
            setMessage(mBuilder.messageStr!!)
        }
        positiveTv = rootView.findViewById(R.id.positive_tv)
        takeIf { positiveTv != null && mBuilder.positiveStr != null }?.run {
            addPositiveButton(mBuilder.positiveStr!!, mBuilder.onPositiveBtnClickListener)
        }
        negativeTv = rootView.findViewById(R.id.negative_tv)
        takeIf { negativeTv != null && mBuilder.negativeStr != null }?.run {
            addNegativeButton(mBuilder.negativeStr!!, mBuilder.onNegativeBtnClickListener)
        }
        dialog.setCancelable(mBuilder.isCancelable)
        dialog.setCanceledOnTouchOutside(mBuilder.cancelTouchOut)
    }

    private fun initOnClickListeners() {
        if (mViewOnClickListeners.size != 0) {
            val iter = mViewOnClickListeners.entries.iterator()
            var view: View?
            while (iter.hasNext()) {
                val entry = iter.next()
                view = rootView.findViewById(entry.key)
                view?.setOnClickListener(entry.value)
            }
        }
    }

    abstract fun initView()

    fun setTitle(title: String) {
        takeIf { mBuilder.titleStr == null }?.run {
            mBuilder.setTitle(title)
        }
        titleTv?.apply {
            visibility = View.VISIBLE
            titleTv!!.text = title
        }
    }

    fun setMessage(message: String) {
        takeIf { mBuilder.messageStr == null }?.run {
            mBuilder.setMessage(message)
        }
        messageTv?.apply {
            visibility = View.VISIBLE
            text = message
            movementMethod = ScrollingMovementMethod.getInstance()
        }
    }

    fun addPositiveButton(
        title: CharSequence,
        positiveBtnListener: OnClickListener?
    ) {
        takeIf { mBuilder.positiveStr == null }?.run {
            mBuilder.addPositiveBtn(title.toString(), positiveBtnListener)
        }
        positiveTv?.apply {
            visibility = View.VISIBLE
            text = title
            mBuilder.positiveColor?.run { textColor = this }
            setOnClickListener {
                positiveBtnListener?.onClick(
                    this@BaseDialogFragment,
                    positiveTv!!
                )
            }
        }
    }

    fun addNegativeButton(
        title: CharSequence,
        negativeBtnListener: OnClickListener?
    ) {
        takeIf { mBuilder.negativeStr == null }?.run {
            mBuilder.addNegativeBtn(title.toString(), negativeBtnListener)
        }
        negativeTv?.apply {
            visibility = View.VISIBLE
            text = title
            mBuilder.negativeColor?.run { textColor = this }
            setOnClickListener {
                negativeBtnListener?.onClick(this@BaseDialogFragment, negativeTv!!)
            }
        }
    }

    /**
     * 需在DialogFragment显示后调用，设置OnShowListener
     */
    fun <T : View> getChildView(@IdRes idRes: Int): T? {
        if (this::rootView.isInitialized)
            return rootView.findViewById(idRes)
        return null
    }

    fun addChildListener(@IdRes idRes: Int, onClickListener: View.OnClickListener) {
        if (dialog?.isShowing == true) {
            getChildView<View>(idRes)?.setOnClickListener(onClickListener)
        } else {
            mViewOnClickListeners[idRes] = onClickListener
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        show(fragmentManager, "")
    }

    /**
     * 覆盖DialogFragment show方法，
     * 使用.commitAllowingStateLoss提交
     */
    override fun show(fragmentManager: FragmentManager?, tag: String?) {
        try {
            val c = Class.forName("androidx.fragment.app.DialogFragment")
            val dismissed = c.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
            val shownByMe = c.getDeclaredField("mShownByMe")
            shownByMe.isAccessible = true
            shownByMe.set(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val ft = fragmentManager?.beginTransaction()
        ft?.add(this, tag)
        ft?.commitAllowingStateLoss()
    }

    override fun dismiss() {

        if (!isDetached && fragmentManager != null ) {
            super.dismissAllowingStateLoss()
        }
    }

    abstract class Builder<T : Builder<T>> {

        lateinit var dialogFragment: BaseDialogFragment<T>
        var cancelTouchOut: Boolean = false
            protected set
        var resStyle = R.style.deiui_dialog_default_style
            protected set
        var onDialogShowListener: DialogInterface.OnShowListener? = null
            protected set
        var onDialogDismissListener: DialogInterface.OnDismissListener? = null
            protected set
        var onPositiveBtnClickListener: OnClickListener? = null
            protected set
        var onNegativeBtnClickListener: OnClickListener? = null
            protected set
        var onDialogCancelListener: DialogInterface.OnCancelListener? = null
            protected set
        var titleStr: String? = null
            protected set
        var messageStr: String? = null
            protected set
        var negativeStr: String? = null
            protected set
        var positiveStr: String? = null
            protected set
        var isCancelable: Boolean = true
            protected set
        var negativeColor: Int? = null
            protected set
        var resView: Int = R.layout.deiui_alert_dialog_default
            protected set
        var positiveColor: Int? = null
            protected set

        fun cancelTouchOut(cancelTouchOut: Boolean): T {
            this.cancelTouchOut = cancelTouchOut
            return this as T
        }

        fun isCancelable(flag: Boolean): T {
            isCancelable = flag
            return this as T
        }


        fun view(resView: Int): T {
            this.resView = resView
            return this as T
        }

        fun style(resStyle: Int): T {
            this.resStyle = resStyle
            return this as T
        }

        fun setOnShowListener(onShowListener: DialogInterface.OnShowListener?): T {
            onDialogShowListener = onShowListener
            return this as T
        }

        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): T {
            onDialogDismissListener = onDismissListener
            return this as T
        }

        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): T {
            onDialogCancelListener = onCancelListener
            return this as T
        }

        fun setTitle(titleStr: String): T {
            this.titleStr = titleStr
            return this as T
        }

        fun setMessage(messageStr: String): T {
            this.messageStr = messageStr
            return this as T
        }

        @JvmOverloads
        fun addNegativeBtn(
            negativeStr: String = "取消",
            listener: OnClickListener? = object : OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    dialogFragment.dismiss()
                }
            }
        ): T {
            this.negativeStr = negativeStr
            this.onNegativeBtnClickListener = listener
            return this as T
        }

        @JvmOverloads
        fun addPositiveBtn(
            positiveStr: String = "确定",
            listener: OnClickListener? = object : OnClickListener {
                override fun onClick(dialogFragment: DialogFragment, view: View) {
                    dialogFragment.dismiss()
                }
            }
        ): T {
            this.positiveStr = positiveStr
            this.onPositiveBtnClickListener = listener
            return this as T
        }

        fun setPositiveColor(color: Int): T {
            this.positiveColor = color
            return this as T
        }

        fun setNegativeColor(color: Int): T {
            this.negativeColor = color
            return this as T
        }

    }

    interface OnClickListener {
        fun onClick(dialogFragment: DialogFragment, view: View)
    }
}