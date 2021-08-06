//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.delicloud.app.deiui.utils

import android.app.Activity
import android.content.Context
import android.os.Build.VERSION
import androidx.fragment.app.Fragment

/**
 * 生命周期工具类
 */
class AndroidLifecycleUtils {
    companion object {
        /**
         * 是否可以加载图片
         */
        fun canLoadImage(fragment: Fragment?): Boolean {
            return if (fragment == null) {
                true
            } else {
                val activity = fragment.activity
                canLoadImage(activity as Activity?)
            }
        }

        /**
         * 判断是否可以加载图片
         */
        fun canLoadImage(context: Context?): Boolean {
            return when (context) {
                null -> true
                !is Activity -> true
                else -> {
                    val activity = context as Activity?
                    canLoadImage(activity)
                }
            }
        }

        private fun canLoadImage(activity: Activity?): Boolean {
            return if (activity == null) {
                true
            } else {
                //activity 没有被销毁且不是finish状态，返回true
                val destroyed = VERSION.SDK_INT >= 17 && activity.isDestroyed
                !destroyed && !activity.isFinishing
            }
        }
    }
}
