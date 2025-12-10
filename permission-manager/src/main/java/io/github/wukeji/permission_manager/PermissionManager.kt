package io.github.wukeji.permission_manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Usage :
 * PermissionManager.requestPermission(
 *     Manifest.permission.CAMERA,
 *     this
 * ) {
 *     onGranted { ... }
 *     showRational { ... }
 *     neverAskAgain { ... }
 * }
 */
object PermissionManager {

    private const val TAG = "io.github.wukeji.permission_manager.Host"

    // 为某个 Activity 拿到 / 创建唯一的 host fragment
    private fun hostOf(activity: FragmentActivity): PermissionHostFragment {
        val fm = activity.supportFragmentManager
        val existing = fm.findFragmentByTag(TAG) as? PermissionHostFragment
        if (existing != null) return existing

        val fragment = PermissionHostFragment()
        fm.beginTransaction()
            .add(fragment, TAG)
            .commitNow()
        return fragment
    }


    fun requestPermission(
        permission: String,
        activity: FragmentActivity,
        block: PermissionCallbacks.() -> Unit
    ) {
        val callbacks = PermissionCallbacks().apply(block)
        hostOf(activity).requestPermission(permission, callbacks)
    }


    fun requestPermission(
        permission: String,
        fragment: Fragment,
        block: PermissionCallbacks.() -> Unit
    ) {
        val act = fragment.requireActivity()
        val callbacks = PermissionCallbacks().apply(block)
        hostOf(act).requestPermission(permission, callbacks)
    }
}