package io.github.wukeji.base

import io.github.wukeji.neatlog.api.LogMixin
import io.github.wukeji.neatlog.api.LoggerRegistry
import io.github.wukeji.neatlog.api.android.AndroidUtilLogger
import io.github.wukeji.neatlog.api.neatName
import io.github.wukeji.neatlog.core.LogFacade
import io.github.wukeji.neatlog.elements.InterceptorElement
import io.github.wukeji.neatlog.interceptors.MessagePrefixInterceptor

/**
 * android.util.Log + class simple prefix + suffix
 */
interface NamedLogMixin : LogMixin {

    override val neatLogger: LogFacade
        get() = LoggerRegistry.getOrCreate(this) {
            val messagePrefix = "${this.neatName}: ${this.hashCode()}: "
            val messageSuffix = "."
            val prefix = MessagePrefixInterceptor(messagePrefix)
            val suffix = MessagePrefixInterceptor(messageSuffix)
            AndroidUtilLogger.clone(InterceptorElement(prefix + suffix))
        }
}