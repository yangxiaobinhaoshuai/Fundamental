package io.github.wukeji.base.definitions

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * Application 生命周期的“中控台”，由真正的 Application 转发调用。
 */
object AppDelegate : AppEntry {

    private val entries = mutableListOf<AppEntry>()

    /**
     * 在 App 启动前把各个模块的 AppEntry 注册进来。
     * 通常在 Application.onCreate 之前或静态代码块中调用。
     */
    @Synchronized
    fun register(entry: AppEntry) {
        entries += entry
    }

    // ==== 以下是转发逻辑，Application 只需调用 AppDelegate 对应方法 ====

    override fun attachBaseContext(app: Application, base: Context) {
        entries.forEach { it.attachBaseContext(app, base) }
    }

    override fun onCreate(app: Application) {
        entries.forEach { it.onCreate(app) }
    }

    override fun onTerminate(app: Application) {
        entries.forEach { it.onTerminate(app) }
    }

    override fun onConfigurationChanged(app: Application, newConfig: Configuration) {
        entries.forEach { it.onConfigurationChanged(app, newConfig) }
    }

    override fun onLowMemory(app: Application) {
        entries.forEach { it.onLowMemory(app) }
    }

    override fun onTrimMemory(app: Application, level: Int) {
        entries.forEach { it.onTrimMemory(app, level) }
    }
}