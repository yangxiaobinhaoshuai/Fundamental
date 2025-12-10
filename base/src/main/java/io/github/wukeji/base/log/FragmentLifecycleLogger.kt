package io.github.wukeji.base.log

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.wukeji.base.adapter.FragmentLifecycleAdapter
import io.github.wukeji.base.definitions.neatName
import io.github.wukeji.neatlog.api.NeatLogger

class FragmentLifecycleLogger : FragmentLifecycleAdapter() {

    override fun onFragmentCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        val name = f.neatName
        NeatLogger.d(name, "$name::onCreate")
    }

    override fun onFragmentDestroyed(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentDestroyed(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onDestroy")
    }

    override fun onFragmentStarted(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentStarted(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onStarted")
    }

    override fun onFragmentResumed(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentResumed(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onResumed")
    }

    override fun onFragmentPaused(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentPaused(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onPaused")
    }

    override fun onFragmentStopped(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentStopped(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onStopped")
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        val name = f.neatName
        NeatLogger.d(name, "$name::onViewCreated")
    }

    override fun onFragmentViewDestroyed(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentViewDestroyed(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onViewDestroyed")
    }

    override fun onFragmentAttached(
        fm: FragmentManager,
        f: Fragment,
        context: Context
    ) {
        super.onFragmentAttached(fm, f, context)
        val name = f.neatName
        NeatLogger.d(name, "$name::onAttached")
    }

    override fun onFragmentDetached(
        fm: FragmentManager,
        f: Fragment
    ) {
        super.onFragmentDetached(fm, f)
        val name = f.neatName
        NeatLogger.d(name, "$name::onDetached")
    }
}