package io.github.wukeji.base.log

import android.app.Activity
import android.os.Bundle
import io.github.wukeji.base.adapter.ActivityLifecycleAdapter
import io.github.wukeji.base.definitions.neatName
import io.github.wukeji.neatlog.api.NeatLogger

class ActivityLifecycleLogger : ActivityLifecycleAdapter() {

    val fragmentLifecycleLogger = FragmentLifecycleLogger()

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        val name = activity.neatName
        NeatLogger.d(name, "$name::onCreate")

        if (activity is androidx.fragment.app.FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                fragmentLifecycleLogger,
                true // recursive = true，子 Fragment 也会收到回调
            )
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onDestroy")
        if (activity is androidx.fragment.app.FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                fragmentLifecycleLogger
            )
        }
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onPause")
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onResumed")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        super.onActivitySaveInstanceState(activity, outState)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onSaveInstanceState")
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onStart")
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        val name = activity.neatName
        NeatLogger.d(name, "$name::onStopped")
    }
}