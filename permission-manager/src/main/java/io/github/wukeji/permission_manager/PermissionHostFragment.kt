package io.github.wukeji.permission_manager

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal class PermissionHostFragment : Fragment() {

    private lateinit var launcher: ActivityResultLauncher<String>

    private var pendingPermission: String? = null
    private var pendingCallbacks: PermissionCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            val permission = pendingPermission ?: return@registerForActivityResult
            val cb = pendingCallbacks ?: return@registerForActivityResult

            if (granted) {
                cb.granted?.invoke()
            } else {
                val shouldShow = shouldShowRequestPermissionRationale(permission)
                if (shouldShow) {
                    cb.rational?.invoke()
                } else {
                    cb.neverAskAgain?.invoke()
                }
            }

            pendingPermission = null
            pendingCallbacks = null
        }
    }

    fun requestPermission(permission: String, callbacks: PermissionCallbacks) {
        val ctx = context ?: return

        // 已经有权限，直接走 granted 分支
        if (ContextCompat.checkSelfPermission(
                ctx,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callbacks.granted?.invoke()
            return
        }

        pendingPermission = permission
        pendingCallbacks = callbacks
        launcher.launch(permission)
    }
}