package io.github.wukeji.permission_manager

sealed interface PermissionResult {

    data object Granted : PermissionResult
    sealed interface Denied : PermissionResult

    data object ShouldShowRational : Denied

    data object NeverAskAgain : Denied
}


class PermissionCallbacks {

    internal var granted: (() -> Unit)? = null
    internal var rational: (() -> Unit)? = null
    internal var neverAskAgain: (() -> Unit)? = null

    fun onGranted(block: () -> Unit) {
        granted = block
    }

    fun showRational(block: () -> Unit) {
        rational = block
    }

    fun neverAskAgain(block: () -> Unit) {
        neverAskAgain = block
    }
}