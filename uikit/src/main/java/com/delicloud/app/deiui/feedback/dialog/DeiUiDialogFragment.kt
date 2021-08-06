package com.delicloud.app.deiui.feedback.dialog

import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.delicloud.app.deiui.R

/**
 * 05-04，01,02,04,05,07,09,10
 * 普通提示和带图提示，以及水平进度条提示
 * 普通提示包含两个按钮以及Title和Message(居左显示).
 * https://lanhuapp.com/web/#/item/project/board?pid=45fd033f-940e-45b6-a6b2-716929ba5ff3
 * 特殊布局需求可以自定义布局.
 */
class DeiUiDialogFragment @JvmOverloads constructor(
    builder: Builder = Builder()
) : BaseDialogFragment<DeiUiDialogFragment.Builder>(builder) {

    /**
     * 带图弹窗 ImageView
     */
    var imageView: ImageView? = null
    /**
     * 带进度条弹窗，默认不显示，
     */

    private var progressBar: ProgressBar? = null

    private var maxProgress: Int = 100
    private var progress: Int = 0


    fun setDrawableRes(res: Int) {
        mBuilder.setDrawableRes(res)
        imageView?.visibility = View.VISIBLE
        imageView?.setImageDrawable(ContextCompat.getDrawable(context!!, res))
    }

    fun setBitmap(bmp: Bitmap) {
        mBuilder.setBitmap(bmp)
        imageView?.visibility = View.VISIBLE
        imageView?.setImageBitmap(bmp)
    }


    fun setImageUrl(url: String) {
        mBuilder.setImageUrl(url)
        imageView?.run {
            visibility = View.VISIBLE
            Glide.with(this)
                .load(url)
                .centerCrop()
                .dontAnimate()
                .into(imageView!!)
        }
    }


    /**
     * 设置水平进度条进度
     */
    fun setProgress(p: Int) {
        mBuilder.setProgress(p)
        progress = p
        progressBar?.progress = p
    }

    fun setMaxProgress(max: Int) {
        mBuilder.setMaxProgress(max)
        maxProgress = max
        progressBar?.max = max
    }

    override fun initView() {
        try {
            imageView = rootView.findViewById(R.id.dialog_iv)
            mBuilder.bmp?.run { setBitmap(this) }
            when {
                mBuilder.url != null -> {
                    setImageUrl(mBuilder.url!!)
                }
                mBuilder.bmp != null -> {
                    setBitmap(mBuilder.bmp!!)
                }
                mBuilder.drawableRes != -1 -> {
                    setDrawableRes(mBuilder.drawableRes)
                }
            }
            val closeIv = rootView.findViewById<ImageView>(R.id.close_iv)
            closeIv?.setOnClickListener { dismiss() }
            progressBar = rootView.findViewById(R.id.dialog_progress_bar)
            if (mBuilder.showProgress && progressBar != null) {
                initProgressDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initProgressDialog() {
        progressBar?.apply {
            visibility = View.VISIBLE
            isIndeterminate = false
            max = mBuilder.max
            progress = mBuilder.p
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, i, _ ->
            i == KeyEvent.KEYCODE_BACK
        }
    }

    class Builder : BaseDialogFragment.Builder<Builder>() {
        var max: Int = 100
        var showProgress = false
        var drawableRes = -1
        var bmp: Bitmap? = null
        var p = 0
        var url: String? = null
        fun showProgressBar(): Builder {
            showProgress = true
            return this
        }

        fun setMaxProgress(max: Int): Builder {
            showProgressBar()
            this.max = max
            return this
        }

        /**
         * 同时设置url bitmap  res优先级为url,bitmap,res
         */
        fun setImageUrl(url: String): Builder {
            this.url = url
            return this
        }

        fun setProgress(p: Int): Builder {
            showProgressBar()
            this.p = p
            return this
        }

        /**
         * 同时设置url bitmap  res优先级为url,bitmap,res
         */
        fun setDrawableRes(drawableRes: Int): Builder {
            this.drawableRes = drawableRes
            return this
        }

        /**
         * 同时设置url bitmap  res优先级为url,bitmap,res
         */
        fun setBitmap(bmp: Bitmap): Builder {
            this.bmp = bmp
            return this
        }

        fun build(): DeiUiDialogFragment {
            dialogFragment = DeiUiDialogFragment(this)
            return dialogFragment as DeiUiDialogFragment
        }

    }

}
