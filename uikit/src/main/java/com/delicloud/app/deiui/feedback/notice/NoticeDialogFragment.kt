package com.delicloud.app.deiui.feedback.notice

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.delicloud.app.deiui.R
import com.delicloud.app.deiui.feedback.dialog.BaseDialogFragment
import com.delicloud.app.deiui.utils.ScreenUtil


class NoticeDialogFragment(builder: KProgressBuilder) :
    BaseDialogFragment<NoticeDialogFragment.KProgressBuilder>(builder) {


    private var mDeterminateView: Determinate? = null
    private var mIndeterminateView: Indeterminate? = null
    private var mView: View? = null
    private var mLabelText: TextView? = null
    private var mDetailsText: TextView? = null
    private var mLabel: String? = null
    private var mDetailsLabel: String? = null
    private var mCustomViewContainer: FrameLayout? = null
    private var mBackgroundLayout: BackgroundLayout? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mLabelColor = Color.WHITE
    private var mDetailColor = Color.WHITE


    override fun initStyleAndSize() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(0))
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        if (!mBuilder.mIsFocusable) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
        val layoutParams = window.attributes
        layoutParams.dimAmount = mBuilder.mDimAmount
        layoutParams.gravity = Gravity.CENTER
        window.attributes = layoutParams
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        mBackgroundLayout = rootView.findViewById<View>(R.id.background) as BackgroundLayout
        mBackgroundLayout!!.setBaseColor(mBuilder.mWindowColor)
        mBackgroundLayout!!.setCornerRadius(mBuilder.mCornerRadius)
        if (mWidth != 0) {
            updateBackgroundSize()
        }
        mCustomViewContainer = rootView.findViewById<View>(R.id.container) as FrameLayout
        addViewToFrame(mView)
        mDeterminateView?.setMax(mBuilder.mMaxProgress)
        mIndeterminateView?.setAnimationSpeed(mBuilder.mAnimateSpeed.toFloat())
        mLabelText = rootView.findViewById<View>(R.id.label) as TextView
        setLabel(mLabel, mLabelColor)
        mDetailsText = rootView.findViewById<View>(R.id.details_label) as TextView
        setDetailsLabel(mDetailsLabel, mDetailColor)
    }

    private fun addViewToFrame(view: View?) {
        if (view == null) {
            mCustomViewContainer?.visibility = View.GONE
            return
        }
        mCustomViewContainer?.visibility = View.VISIBLE
        val wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT
        val params = ViewGroup.LayoutParams(wrapParam, wrapParam)
        mCustomViewContainer?.addView(view, params)
    }

    private fun updateBackgroundSize() {
        val params = mBackgroundLayout!!.layoutParams
        params.width = ScreenUtil.dip2px(mWidth.toFloat())
        params.height = ScreenUtil.dip2px(mHeight.toFloat())
        mBackgroundLayout!!.layoutParams = params
    }

    fun setProgress(progress: Int) {
        if (mDeterminateView != null) {
            mDeterminateView!!.setProgress(progress)
            if (mBuilder.mIsAutoDismiss && progress >= mBuilder.mMaxProgress) {
                dismiss()
            }
        }
    }

    fun setView(view: View?) {
        if (view != null) {
            if (view is Determinate) {
                mDeterminateView = view
            }
            if (view is Indeterminate) {
                mIndeterminateView = view
            }
            mView = view
            if (mBuilder.isShowing) {
                mCustomViewContainer?.removeAllViews()
                addViewToFrame(view)
            }
        }
    }

    fun setLabel(label: String?) {
        mLabel = label
        if (mLabelText != null) {
            if (label != null) {
                mLabelText!!.text = label
                mLabelText!!.visibility = View.VISIBLE
            } else {
                mLabelText!!.visibility = View.GONE
            }
        }
    }

    fun setDetailsLabel(detailsLabel: String?) {
        mDetailsLabel = detailsLabel
        if (mDetailsText != null) {
            if (detailsLabel != null) {
                mDetailsText!!.text = detailsLabel
                mDetailsText!!.visibility = View.VISIBLE
            } else {
                mDetailsText!!.visibility = View.GONE
            }
        }
    }

    fun setLabel(label: String?, color: Int) {
        mLabel = label
        mLabelColor = color
        if (mLabelText != null) {
            if (label != null) {
                mLabelText!!.text = label
                mLabelText!!.setTextColor(color)
                mLabelText!!.visibility = View.VISIBLE
            } else {
                mLabelText!!.visibility = View.GONE
            }
        }
    }

    fun setDetailsLabel(detailsLabel: String?, color: Int) {
        mDetailsLabel = detailsLabel
        mDetailColor = color
        if (mDetailsText != null) {
            if (detailsLabel != null) {
                mDetailsText!!.text = detailsLabel
                mDetailsText!!.setTextColor(color)
                mDetailsText!!.visibility = View.VISIBLE
            } else {
                mDetailsText!!.visibility = View.GONE
            }
        }
    }

    fun setSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        if (mBackgroundLayout != null) {
            updateBackgroundSize()
        }
    }

    /**
     * 加载，轻提示弹窗
     */
    class KProgressBuilder(val mContext: Context) :
        BaseDialogFragment.Builder<KProgressBuilder>() {

        // To avoid redundant APIs, make the HUD as a wrapper class around a Dialog
        private val mNoticeDialog: NoticeDialogFragment?
        var mDimAmount: Float = 0.toFloat()
        var mWindowColor: Int = 0
        var mCornerRadius: Float = 0.toFloat()

        var mAnimateSpeed: Int = 0

        var mMaxProgress: Int = 0
        var mIsAutoDismiss: Boolean = false

        var mGraceTimeMs: Int = 0
        var mGraceTimer: Handler? = null
        var mFinished: Boolean = false
        //Add by Irvin
        var mIsFocusable: Boolean = false

        val isShowing: Boolean
            get() = mNoticeDialog != null && mNoticeDialog.isVisible

        val context: Context?
            get() = mNoticeDialog?.context

        /**
         * 加载框类型，旋转，饼图，环形图，条形图
         */
        enum class Style {
            SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
        }

        init {
            resView = R.layout.deiui_kprogresshud_hud
            mNoticeDialog = NoticeDialogFragment(this)
            mDimAmount = 0f
            mWindowColor = Color.parseColor("#b1000000")
            mAnimateSpeed = 1
            mCornerRadius = 10f
            mIsAutoDismiss = true
            mGraceTimeMs = 0
            mFinished = false
            //Add by Irvin
            mIsFocusable = true
        }

        /**
         * @NewAPI
         * Specify the HUD focusable (not needed if you use a custom view)
         * @param focusable the KProgressHUD will not close if press back
         * @return Current HUD
         */
        fun setFocusable(focusable: Boolean): KProgressBuilder {
            mIsFocusable = focusable
            return this
        }

        /**
         * Specify the HUD style (not needed if you use a custom view)
         * @param style One of the KProgressHUD.Style values
         * @return Current HUD
         */
        fun setStyle(style: Style): KProgressBuilder {
            val view = when (style) {
                Style.SPIN_INDETERMINATE -> SpinView(mContext)
                Style.PIE_DETERMINATE -> PieView(mContext)
                Style.ANNULAR_DETERMINATE -> AnnularView(mContext)
                Style.BAR_DETERMINATE -> BarView(mContext)
            }// No custom view style here, because view will be added later
            mNoticeDialog?.view = view
            return this
        }

        /**
         * Specify the dim area around the HUD, like in Dialog
         * @param dimAmount May take value from 0 to 1. Default to 0 (no dimming)
         * @return Current HUD
         */
        fun setDimAmount(dimAmount: Float): KProgressBuilder {
            if (dimAmount in 0.0..1.0) {
                mDimAmount = dimAmount
            }
            return this
        }

        /**
         * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
         * @param width in dp
         * @param height in dp
         * @return Current HUD
         */
        fun setSize(width: Int, height: Int): KProgressBuilder {
            mNoticeDialog!!.setSize(width, height)
            return this
        }

        /**
         * @param color ARGB color
         * @return Current HUD
         */
        @Deprecated(
            "As of release 1.1.0, replaced by {@link #setBackgroundColor(int)}\n" +
                    "      "
        )
        fun setWindowColor(color: Int): KProgressBuilder {
            mWindowColor = color
            return this
        }

        /**
         * Specify the HUD background color
         * @param color ARGB color
         * @return Current HUD
         */
        fun setBackgroundColor(color: Int): KProgressBuilder {
            mWindowColor = color
            return this
        }

        /**
         * Specify corner radius of the HUD (default is 10)
         * @param radius Corner radius in dp
         * @return Current HUD
         */
        fun setCornerRadius(radius: Float): KProgressBuilder {
            mCornerRadius = radius
            return this
        }

        /**
         * Change animation speed relative to default. Used with indeterminate style
         * @param scale Default is 1. If you want double the speed, set the param at 2.
         * @return Current HUD
         */
        fun setAnimationSpeed(scale: Int): KProgressBuilder {
            mAnimateSpeed = scale
            return this
        }

        /**
         * Optional label to be displayed.
         * @return Current HUD
         */
        fun setLabel(label: String): KProgressBuilder {
            mNoticeDialog!!.setLabel(label)
            return this
        }

        /**
         * Optional label to be displayed
         * @return Current HUD
         */
        fun setLabel(label: String, color: Int): KProgressBuilder {
            mNoticeDialog!!.setLabel(label, color)
            return this
        }

        /**
         * Optional detail description to be displayed on the HUD
         * @return Current HUD
         */
        fun setDetailsLabel(detailsLabel: String): KProgressBuilder {
            mNoticeDialog!!.setDetailsLabel(detailsLabel)
            return this
        }

        /**
         * Optional detail description to be displayed
         * @return Current HUD
         */
        fun setDetailsLabel(detailsLabel: String, color: Int): KProgressBuilder {
            mNoticeDialog!!.setDetailsLabel(detailsLabel, color)
            return this
        }

        /**
         * Max value for use in one of the determinate styles
         * @return Current HUD
         */
        fun setMaxProgress(maxProgress: Int): KProgressBuilder {
            mMaxProgress = maxProgress
            return this
        }

        /**
         * Set current progress. Only have effect when use with a determinate style, or a custom
         * view which implements Determinate interface.
         */
        fun setProgress(progress: Int) {
            mNoticeDialog!!.setProgress(progress)
        }

        /**
         * Provide a custom view to be displayed.
         * @param view Must not be null
         * @return Current HUD
         */
        fun setCustomView(view: View?): KProgressBuilder {
            if (view != null) {
                mNoticeDialog!!.view = view
            } else {
                throw RuntimeException("Custom view must not be null!")
            }
            return this
        }

        /**
         * Specify whether this HUD can be cancelled by using back button (default is false)
         *
         * Setting a cancelable to true with this method will set a null callback,
         * clearing any callback previously set with
         * [.setCancellable]
         *
         * @return Current HUD
         */
        fun setCancellable(isCancellable: Boolean): KProgressBuilder {
            mNoticeDialog!!.isCancelable = isCancellable
            mNoticeDialog.onCancelListener = null
            return this
        }

        /**
         * Specify a callback to run when using the back button (default is null)
         *
         * @param listener The code that will run if the user presses the back
         * button. If you pass null, the dialog won't be cancellable, just like
         * if you had called [.setCancellable] passing false.
         *
         * @return Current HUD
         */
        fun setCancellable(listener: DialogInterface.OnCancelListener?): KProgressBuilder {
            mNoticeDialog!!.isCancelable = null != listener
            mNoticeDialog.onCancelListener = listener
            return this
        }

        /**
         * Specify whether this HUD closes itself if progress reaches max. Default is true.
         * @return Current HUD
         */
        fun setAutoDismiss(isAutoDismiss: Boolean): KProgressBuilder {
            mIsAutoDismiss = isAutoDismiss
            return this
        }

        /**
         * Grace period is the time (in milliseconds) that the invoked method may be run without
         * showing the HUD. If the task finishes before the grace time runs out, the HUD will
         * not be shown at all.
         * This may be used to prevent HUD display for very short tasks.
         * Defaults to 0 (no grace time).
         * @param graceTimeMs Grace time in milliseconds
         * @return Current HUD
         */
        fun setGraceTime(graceTimeMs: Int): KProgressBuilder {
            mGraceTimeMs = graceTimeMs
            return this
        }

        fun show(fragmentManager: FragmentManager): KProgressBuilder {
            if (!isShowing) {
                mFinished = false
                if (mGraceTimeMs == 0) {
                    mNoticeDialog?.show(fragmentManager)
                } else {
                    mGraceTimer = Handler()
                    mGraceTimer!!.postDelayed({
                        if (mNoticeDialog != null && !mFinished) {
                            mNoticeDialog.show(fragmentManager)
                        }
                    }, mGraceTimeMs.toLong())
                }
            }
            return this
        }

        fun dismiss() {
            mFinished = true
            if (mNoticeDialog != null && mNoticeDialog.isVisible) {
                mNoticeDialog.dismiss()
            }
            if (mGraceTimer != null) {
                mGraceTimer!!.removeCallbacksAndMessages(null)
                mGraceTimer = null
            }
        }


        companion object {

            /**
             * Create a new HUD. Have the same effect as the constructor.
             * For convenient only.
             * @param context Activity mContext that the HUD bound to
             * @return An unique HUD instance
             */
            fun create(context: Context): KProgressBuilder {
                return KProgressBuilder(context)
            }

            /**
             * Create a new HUD. specify the HUD style (if you use a custom view, you need `KProgressHUD.create(Context mContext)`).
             *
             * @param context Activity mContext that the HUD bound to
             * @param style One of the KProgressHUD.Style values
             * @return An unique HUD instance
             */
            fun create(context: Context, style: Style): KProgressBuilder {
                return KProgressBuilder(context).setStyle(style)
            }
        }
    }
}