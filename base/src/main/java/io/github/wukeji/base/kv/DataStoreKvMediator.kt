package io.github.wukeji.base.kv

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.File

/**
 * Usage:
 *
 * private const val KV_STORE_NAME = "kv_store"
 *
 * val Context.dataStore by preferencesDataStore(name = KV_STORE_NAME)
 *
 * val Context.kvDataStore by lazy { DataStoreKvMediator(this.dataStore) }
 */

fun createKvDataStore(
    context: Context,
    fileName: String = "kv_store.preferences_pb"
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = {
            File(context.filesDir, fileName)
        }
    )
}

class DataStoreKvMediator(
    private val dataStore: DataStore<Preferences>,
    scope: CoroutineScope = KvMediator.createDefault(),
) : CachedKvMediator(scope) {

    private fun strKey(key: String) = stringPreferencesKey(key)
    private fun intKey(key: String) = intPreferencesKey(key)
    private fun longKey(key: String) = longPreferencesKey(key)
    private fun floatKey(key: String) = floatPreferencesKey(key)
    private fun doubleKey(key: String) = doublePreferencesKey(key)
    private fun boolKey(key: String) = booleanPreferencesKey(key)
    private fun strSetKey(key: String) = stringSetPreferencesKey(key)

    override fun observeStringCold(key: String, defaultValue: String?): Flow<String?> =
        dataStore.data
            .map { it[strKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeIntCold(key: String, defaultValue: Int): Flow<Int> =
        dataStore.data
            .map { it[intKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeLongCold(key: String, defaultValue: Long): Flow<Long> =
        dataStore.data
            .map { it[longKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeFloatCold(key: String, defaultValue: Float): Flow<Float> =
        dataStore.data
            .map { it[floatKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeDoubleCold(key: String, defaultValue: Double): Flow<Double> =
        dataStore.data
            .map { it[doubleKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeBooleanCold(key: String, defaultValue: Boolean): Flow<Boolean> =
        dataStore.data
            .map { it[boolKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override fun observeStringSetCold(key: String, defaultValue: Set<String>?): Flow<Set<String>?> =
        dataStore.data
            .map { it[strSetKey(key)] ?: defaultValue }
            .catch { emit(defaultValue) }

    override suspend fun write(ops: List<KvOp>): KvResult<Unit> {
        return try {
            dataStore.edit { prefs ->
                ops.forEach { op ->
                    when (op) {
                        is KvOp.Put<*> -> applyPut(prefs, op)
                        is KvOp.Remove -> {
                            // 注意：Remove 需要“按所有可能类型”都 remove 一遍
                            ops.filterIsInstance<KvOp.Remove>().forEach { r ->
                                prefs.remove(strKey(r.key))
                                prefs.remove(intKey(r.key))
                                prefs.remove(longKey(r.key))
                                prefs.remove(floatKey(r.key))
                                prefs.remove(doubleKey(r.key))
                                prefs.remove(boolKey(r.key))
                                prefs.remove(strSetKey(r.key))
                            }
                        }

                        KvOp.Clear -> prefs.clear()
                    }
                }
            }
            KvResult.Success(Unit)
        } catch (e: Exception) {
            KvResult.Error(e)
        }
    }

    private fun applyPut(prefs: MutablePreferences, op: KvOp.Put<*>) {
        @Suppress("UNCHECKED_CAST")
        when (op.type) {
            KvType.Str -> prefs[strKey(op.key)] = op.value as String
            KvType.IntT -> prefs[intKey(op.key)] = op.value as Int
            KvType.LongT -> prefs[longKey(op.key)] = op.value as Long
            KvType.FloatT -> prefs[floatKey(op.key)] = op.value as Float
            KvType.DoubleT -> prefs[doubleKey(op.key)] = op.value as Double
            KvType.BoolT -> prefs[boolKey(op.key)] = op.value as Boolean
            KvType.StrSet -> prefs[strSetKey(op.key)] = op.value as Set<String>
        }
    }
}