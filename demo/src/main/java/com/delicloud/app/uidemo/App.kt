package com.delicloud.app.uidemo

import android.app.Application
import android.content.ContextWrapper
import com.delicloud.app.deiui.DeiUiLibrary
import com.tencent.bugly.crashreport.CrashReport
import timber.log.Timber

/**
 * Created by wangchangwei
 * on 2019/07/17.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        DeiUiLibrary.init(this)
        //在这里先使用Timber.plant注册一个Tree，然后调用静态的.d .v 去使用
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        CrashReport.initCrashReport(getApplicationContext(), "12e8ba9b19", true);
    }

    companion object {
        @JvmStatic
        lateinit var INSTANCE: App
    }
}

object AppContext : ContextWrapper(App.INSTANCE)