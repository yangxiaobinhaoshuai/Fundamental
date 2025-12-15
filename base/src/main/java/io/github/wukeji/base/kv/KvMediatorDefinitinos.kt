package io.github.wukeji.base.kv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow


/**
 * Jetpack DataStore : https://juejin.cn/post/7109395564789235720
 *
 * SP 原理分析 : https://juejin.cn/post/7169265620306165790
 */

sealed class KvResult<out T> {
    data class Success<T>(val data: T) : KvResult<T>()
    data class Error(val exception: Exception) : KvResult<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
    }
}

val KvResult<*>.isSuccessful: Boolean
    get() = this is KvResult.Success

fun <T> KvResult<T>.getOrDefault(default: T): T = when (this) {
    is KvResult.Success -> data
    else -> default
}

fun <T> KvResult<T>.getOrElse(block: (Exception?) -> T): T = when (this) {
    is KvResult.Success -> data
    is KvResult.Error -> block(exception)
}

sealed interface KvType<T> {
    data object Str : KvType<String?>
    data object IntT : KvType<Int>
    data object LongT : KvType<Long>
    data object FloatT : KvType<Float>
    data object DoubleT : KvType<Double>
    data object BoolT : KvType<Boolean>
    data object StrSet : KvType<Set<String>?>
}

sealed interface KvOp {
    data class Put<T>(val key: String, val type: KvType<T>, val value: T) : KvOp
    data class Remove(val key: String) : KvOp
    data object Clear : KvOp
}


class KvWriteScope internal constructor() {
    internal val ops = mutableListOf<KvOp>()

    fun putString(key: String, value: String?) {
        ops += KvOp.Put(key, KvType.Str, value)
    }

    fun putInt(key: String, value: Int) {
        ops += KvOp.Put(key, KvType.IntT, value)
    }

    fun putLong(key: String, value: Long) {
        ops += KvOp.Put(key, KvType.LongT, value)
    }

    fun putFloat(key: String, value: Float) {
        ops += KvOp.Put(key, KvType.FloatT, value)
    }

    fun putDouble(key: String, value: Double) {
        ops += KvOp.Put(key, KvType.DoubleT, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        ops += KvOp.Put(key, KvType.BoolT, value)
    }

    fun putStringSet(key: String, value: Set<String>?) {
        ops += KvOp.Put(key, KvType.StrSet, value)
    }

    fun remove(key: String) {
        ops += KvOp.Remove(key)
    }

    fun clear() {
        ops += KvOp.Clear
    }
}


interface KvMediator {

    val kvScope: CoroutineScope

    suspend fun <T> read(key: String, type: KvType<T>, defaultValue: T): T
    suspend fun write(ops: List<KvOp>): KvResult<Unit>
    fun <T> observe(key: String, type: KvType<T>, defaultValue: T): StateFlow<T>


    suspend fun edit(block: KvWriteScope.() -> Unit): KvResult<Unit> {
        val scope = KvWriteScope().apply(block)
        return write(scope.ops)
    }

    suspend fun clear(): KvResult<Unit> = write(listOf(KvOp.Clear))
    suspend fun remove(key: String): KvResult<Unit> = write(listOf(KvOp.Remove(key)))

    suspend fun getString(key: String, defaultValue: String? = null): String? = read(key, KvType.Str, defaultValue)
    suspend fun getInt(key: String, defaultValue: Int = 0): Int = read(key, KvType.IntT, defaultValue)
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long = read(key, KvType.LongT, defaultValue)
    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float = read(key, KvType.FloatT, defaultValue)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = read(key, KvType.BoolT, defaultValue)
    suspend fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>? = read(key, KvType.StrSet, defaultValue)


    fun observeString(key: String, defaultValue: String? = null): StateFlow<String?> = observe(key, KvType.Str, defaultValue)
    fun observeInt(key: String, defaultValue: Int = 0): StateFlow<Int> = observe(key, KvType.IntT, defaultValue)
    fun observeLong(key: String, defaultValue: Long = 0L): StateFlow<Long> = observe(key, KvType.LongT, defaultValue)
    fun observeFloat(key: String, defaultValue: Float = 0f): StateFlow<Float> = observe(key, KvType.FloatT, defaultValue)
    fun observeBoolean(key: String, defaultValue: Boolean = false): StateFlow<Boolean> = observe(key, KvType.BoolT, defaultValue)
    fun observeStringSet(key: String, defaultValue: Set<String>? = null): StateFlow<Set<String>?> = observe(key, KvType.StrSet, defaultValue)

    companion object KvScope {
        fun createDefault(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}

suspend inline fun KvMediator.safeEdit(
    noinline block: KvWriteScope.() -> Unit,
    crossinline onError: (Exception) -> Unit
): Boolean = when (val r = edit(block)) {
    is KvResult.Success -> true
    is KvResult.Error -> {
        onError(r.exception); false
    }
}
