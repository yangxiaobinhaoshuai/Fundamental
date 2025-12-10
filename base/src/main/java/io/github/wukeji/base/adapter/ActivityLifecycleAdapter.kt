package io.github.wukeji.base.adapter

import android.app.Activity
import android.app.Application
import android.os.Bundle

open class ActivityLifecycleAdapter(val commonAction: (Activity) -> Unit = {}) :
    Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        commonAction(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        commonAction(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        commonAction(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        commonAction(activity)
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        commonAction(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        commonAction(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        commonAction(activity)
    }
}