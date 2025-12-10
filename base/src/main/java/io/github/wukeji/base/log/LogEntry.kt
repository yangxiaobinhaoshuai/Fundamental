package io.github.wukeji.base.log

import android.app.Application
import io.github.wukeji.base.definitions.AppEntry
import io.github.wukeji.neatlog.api.NeatLogger
import io.github.wukeji.neatlog.api.android.AndroidUtilLogPrinter

class LogEntry : AppEntry {

    override fun onCreate(app: Application) {
        super.onCreate(app)

        NeatLogger.init {
            printer = AndroidUtilLogPrinter()
        }

        val activityLogger = ActivityLifecycleLogger()
        app.registerActivityLifecycleCallbacks(activityLogger)
    }
}