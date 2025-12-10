package io.github.wukeji.base.definitions

import android.app.Application
import android.content.Context
import android.content.res.Configuration

interface AppEntry {

    fun attachBaseContext(app: Application, base: Context) {}

    fun onCreate(app: Application) {}

    fun onTerminate(app: Application) {}

    fun onConfigurationChanged(app: Application, newConfig: Configuration) {}

    fun onLowMemory(app: Application) {}

    fun onTrimMemory(app: Application, level: Int) {}
}