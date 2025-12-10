package io.github.wukeji.base.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class FragmentLifecycleAdapter(
    private val commonAction: (Fragment) -> Unit = {}
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        commonAction(f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        commonAction(f)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        commonAction(f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        commonAction(f)
    }
}