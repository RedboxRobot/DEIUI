package com.delicloud.app.deiui

import android.content.Context
import com.delicloud.app.deiui.utils.ScreenUtil

/**
 *@author Mr.m
 *@date 2019/8/29
 **/
class DeiUiLibrary {
    companion object {
        /**
         * 初始化
         */
        fun init(context: Context) {
            ScreenUtil.init(context)
        }
    }
}