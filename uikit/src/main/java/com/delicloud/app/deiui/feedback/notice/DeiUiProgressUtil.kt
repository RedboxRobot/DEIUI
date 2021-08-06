package com.delicloud.app.deiui.feedback.notice

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.view.View
import androidx.fragment.app.FragmentManager


/**
 * date: 2017/8/7
 * https://lanhuapp.com/web/#/item/project/board/detail?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3&project_id=45fd033f-940e-45b6-a6b2-716929ba5ff3&image_id=50599a99-dfee-4c4d-bbb7-cb63be5113e5
 *05—02
 * 反馈-轻提示
 * desc: ProgressUtil等待,轻提示弹窗工具类
 */

object DeiUiProgressUtil {

    private var mNoticeHUD:  NoticeDialogFragment.KProgressBuilder? = null
    private var mSpecial: NoticeDialogFragment.KProgressBuilder? = null

    /**
     * 默认等待弹窗
     */
    fun show(context: Context?, fragmentManager: FragmentManager) {
        if (context == null) {
            return
        }

        if (mNoticeHUD == null) {
            mNoticeHUD = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等...")
                .setCancellable(false)
                .setFocusable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mNoticeHUD!!.show(fragmentManager)
                }
            }
        } else if (mNoticeHUD!!.context !== context) {
            dismiss()
            mNoticeHUD = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等...")
                .setCancellable(false)
                .setFocusable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mNoticeHUD!!.show(fragmentManager)
                }
            }
        }
    }


    fun show(
        context: Context?,
        fragmentManager: FragmentManager,
        message: String,
        canCancelable: Boolean,
        listener: DialogInterface.OnCancelListener?=null
    ) {
        if (context == null) {
            return
        }
        if (mNoticeHUD == null) {
            mNoticeHUD = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(canCancelable)
                .setCancellable(listener)
                .setFocusable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mNoticeHUD!!.show(fragmentManager)
                }
            }
        } else if (mNoticeHUD!!.context !== context) {
            dismiss()
            mNoticeHUD = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(canCancelable)
                .setCancellable(listener)
                .setFocusable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mNoticeHUD!!.show(fragmentManager)
                }
            }
        }
    }

    fun dismiss() {
        if (mNoticeHUD == null) {
            return
        }
        if (mNoticeHUD!!.isShowing) {
            try {
                mNoticeHUD!!.dismiss()
                mNoticeHUD = null
            } catch (e: Exception) {
                // maybe we catch IllegalArgumentException here.
                e.printStackTrace()
            }

        }
    }

    /**
     * 登录弹窗
     *
     * @param context 上下文
     * @param isShow  是否显示dialog
     */
    fun displaySpecialProgress(
        context: Context?, fragmentManager: FragmentManager, isShow: Boolean,
        listener: DialogInterface.OnCancelListener?=null
    ) {
        if (context == null) {
            return
        }
        if (isShow) {
            mSpecial?.dismiss()
            mSpecial = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel("正在登录,请稍候...")
                .setCancellable(true)
                .setCancellable(listener)
                .setFocusable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mSpecial!!.show(fragmentManager)
                }
            }
        } else {
            if (mSpecial != null && mSpecial!!.isShowing
                && !(context as Activity).isDestroyed && !context.isFinishing
            ) {
                mSpecial!!.dismiss()
                mSpecial = null
            }
        }
    }

    /**
     * 自动取消弹窗，用于警告，错误，成功轻提示，带图片
     *02-04
     * @param context
     * @param msg
     * @param duration  弹窗显示时长，秒为单位
     * @param view
     */
    fun displayProgressAutoDismiss(
        context: Context,
        fragmentManager: FragmentManager,
        msg: String,
        duration: Double = 3.0,
        view: View
    ) {
        displayCustomViewProgress(
            context, fragmentManager,msg, true,
            view, null
        )
        Handler().postDelayed(
            {
                //isShow=false,关闭弹窗
                displayCustomViewProgress(context, fragmentManager,"", false, null, null)
            },
            (duration.toInt() * 1000).toLong()
        )
    }

    /**
     * 仅含文本，不带图轻提示,自动消失
     * @param context
     * @param msg
     * @param duration 提示持续时长 s
     */
    fun displayTextAutoDismiss(
        context: Context?,
        fragmentManager: FragmentManager,
        msg: String,
        duration: Double = 3.0
    ) {
        if (context == null) {
            return
        }
        mSpecial?.dismiss()
        mSpecial = NoticeDialogFragment.KProgressBuilder.create(context)
            .setLabel(msg)
            .setCancellable(true)
            .setFocusable(true)
            .setDimAmount(0.5f)
        Handler().postDelayed(
            {
                mSpecial?.dismiss()
            },
            (duration.toInt() * 1000).toLong()
        )
        mSpecial?.show(fragmentManager)
    }

    /**
     * 自定义信息弹窗
     */
    fun displaySpecialProgress(
        context: Context?, fragmentManager: FragmentManager, message: String, isShow: Boolean,
        listener: DialogInterface.OnCancelListener?=null
    ) {
        if (context == null) {
            return
        }
        if (isShow) {
            mSpecial = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(true)
                .setCancellable(listener)
                .setFocusable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mSpecial!!.show(fragmentManager)
                }
            }
        } else {
            if (mSpecial != null && mSpecial!!.isShowing
                && !(context as Activity).isDestroyed && !context.isFinishing
            ) {
                mSpecial?.dismiss()
                mSpecial = null
            }
        }
    }

    /**
     * 自定义提示信息上的View
     */
    @Synchronized
    fun displayCustomViewProgress(
        context: Context?,
        fragmentManager: FragmentManager,
        message: String,
        isShow: Boolean,
        view: View?,
        listener: DialogInterface.OnCancelListener?=null
    ) {
        if (context == null) {
            return
        }
        if (isShow) {
            mSpecial?.dismiss()
            mSpecial = NoticeDialogFragment.KProgressBuilder.create(context)
                .setStyle(NoticeDialogFragment.KProgressBuilder.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(true)
                .setCancellable(listener)
                .setFocusable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            if (view != null) {
                mSpecial?.setCustomView(view)
            }
            if (context is Activity) {
                if (!context.isFinishing && !context.isDestroyed) {
                    mSpecial?.show(fragmentManager)
                }
            }
        } else {
            if (mSpecial != null && mSpecial!!.isShowing
                && !(context as Activity).isDestroyed && !context.isFinishing
            ) {
                mSpecial?.dismiss()
                mSpecial = null
            }
        }
    }
}
