package io.github.wukeji.base.kv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.ConcurrentHashMap

abstract class CachedKvMediator(val appScope: CoroutineScope = KvMediator.createDefault()) :
    KvMediator {

    override val kvScope: CoroutineScope get() = appScope

    // 子类提供：冷流 + 写
    protected abstract fun observeStringCold(key: String, defaultValue: String?): Flow<String?>
    protected abstract fun observeIntCold(key: String, defaultValue: Int): Flow<Int>
    protected abstract fun observeLongCold(key: String, defaultValue: Long): Flow<Long>
    protected abstract fun observeFloatCold(key: String, defaultValue: Float): Flow<Float>
    protected abstract fun observeDoubleCold(key: String, defaultValue: Double): Flow<Double>
    protected abstract fun observeBooleanCold(key: String, defaultValue: Boolean): Flow<Boolean>
    protected abstract fun observeStringSetCold(
        key: String,
        defaultValue: Set<String>?
    ): Flow<Set<String>?>

    override suspend fun <T> read(key: String, type: KvType<T>, defaultValue: T): T {
        // 统一复用 observe().first()
        return observe(key, type, defaultValue).first()
    }

    private val cache = ConcurrentHashMap<String, StateFlow<*>>()

    private fun cacheKey(type: String, key: String, default: Any?): String =
        "$type|$key|${default?.hashCode() ?: 0}"

    @Suppress("UNCHECKED_CAST")
    private fun <T> cachedStatedFlow(
        type: String,
        key: String,
        defaultValue: T,
        cold: Flow<T>
    ): StateFlow<T> {
        val ck = cacheKey(type, key, defaultValue)
        val sf = cache.computeIfAbsent(ck) {
            cold.distinctUntilChanged().stateIn(
                scope = kvScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = defaultValue
            )
        }
        return sf as StateFlow<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> observe(key: String, type: KvType<T>, defaultValue: T): StateFlow<T> {

        return when (type) {
            KvType.Str -> cachedStatedFlow(
                "str",
                key,
                defaultValue,
                observeStringCold(key, defaultValue as String?)
            )

            KvType.IntT -> cachedStatedFlow(
                "int",
                key,
                defaultValue,
                observeIntCold(key, defaultValue as Int)
            )

            KvType.LongT -> cachedStatedFlow(
                "long",
                key,
                defaultValue,
                observeLongCold(key, defaultValue as Long)
            )

            KvType.FloatT -> cachedStatedFlow(
                "float",
                key,
                defaultValue,
                observeFloatCold(key, defaultValue as Float)
            )

            KvType.DoubleT -> cachedStatedFlow(
                "double",
                key,
                defaultValue,
                observeDoubleCold(key, defaultValue as Double)
            )

            KvType.BoolT -> cachedStatedFlow(
                "bool",
                key,
                defaultValue,
                observeBooleanCold(key, defaultValue as Boolean)
            )

            KvType.StrSet -> cachedStatedFlow(
                "strSet",
                key,
                defaultValue,
                observeStringSetCold(key, defaultValue as Set<String>?)
            )
        } as StateFlow<T>
    }
}