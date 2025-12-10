package io.github.wukeji.fundamental

import android.app.Application
import android.content.Context
import io.github.wukeji.base.definitions.AppDelegate
import io.github.wukeji.base.log.LogEntry

class MyApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // 先注册所有模块的 AppEntry（你可以放在独立方法里）
        registerAppEntries()
        AppDelegate.attachBaseContext(this, base)
    }

    private fun registerAppEntries() {
        AppDelegate.register(LogEntry())
    }

    override fun onCreate() {
        super.onCreate()
        AppDelegate.onCreate(this)
    }
}