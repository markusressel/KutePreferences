package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.test.core.app.ApplicationProvider
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import java.io.File

@RunWith(ParameterizedRobolectricTestRunner::class)
class DataProviderRoundTripTest(private val providerName: String) {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(scope = scope) {
            File(context.filesDir, "round_trip_test.preferences_pb")
        }
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    private fun provider(): KutePreferenceDataProvider = when (providerName) {
        "SharedPreferences" -> DefaultKutePreferenceDataProvider(context)
        else -> DataStoreKutePreferenceDataProvider(dataStore, scope)
    }

    /**
     * Writes are asynchronous, so the value is expected to become visible soon after storing it.
     */
    private fun <T : Any> awaitValue(provider: KutePreferenceDataProvider, expected: T, default: T) {
        val deadline = System.currentTimeMillis() + TIMEOUT_MILLIS
        var actual = provider.getValueUnsafe(KEY, default)
        while (actual != expected && System.currentTimeMillis() < deadline) {
            Thread.sleep(POLL_MILLIS)
            actual = provider.getValueUnsafe(KEY, default)
        }
        assertEquals(expected, actual)
    }

    private fun <T : Any> assertRoundTrip(value: T, default: T) {
        val writer = provider()
        writer.storeValueUnsafe(KEY, value)
        awaitValue(writer, value, default)
        // a new provider reads what the first one wrote, like after an app restart
        awaitValue(provider(), value, default)
    }

    @Test
    fun boolean() = assertRoundTrip(value = false, default = true)

    @Test
    fun int() = assertRoundTrip(value = 42, default = 1)

    @Test
    fun long() = assertRoundTrip(value = 42L, default = 1L)

    @Test
    fun float() = assertRoundTrip(value = 1.5f, default = 0f)

    @Test
    fun double() = assertRoundTrip(value = 1.5, default = 0.0)

    @Test
    fun string() = assertRoundTrip(value = "hello", default = "")

    @Test
    fun time() = assertRoundTrip(
        value = TimePersistenceModel(hourOfDay = 13, minute = 37),
        default = TimePersistenceModel(hourOfDay = 0, minute = 0),
    )

    @Test
    fun `multi element set`() = assertRoundTrip(value = setOf("a", "b"), default = setOf("c", "d"))

    @Test
    fun `set with a single element default`() = assertRoundTrip(value = setOf("a", "b"), default = setOf("c"))

    @Test
    fun `set with a single element value`() = assertRoundTrip(value = setOf("a"), default = setOf("c", "d"))

    @Test
    fun range() = assertRoundTrip(
        value = RangePersistenceModel(min = 1f, max = 2f),
        default = RangePersistenceModel(min = 0f, max = 1f),
    )

    @Test
    fun `missing complex value returns the default`() {
        val default = TimePersistenceModel(hourOfDay = 1, minute = 2)
        assertEquals(default, provider().getValueUnsafe(KEY, default))
    }

    @Test
    fun `value of a different type returns the default`() {
        val writer = provider()
        writer.storeValueUnsafe(KEY, "hello")
        awaitValue(writer, "hello", "")

        assertEquals(1, writer.getValueUnsafe(KEY, 1))
        assertEquals(TimePersistenceModel(1, 2), writer.getValueUnsafe(KEY, TimePersistenceModel(1, 2)))
    }

    @Test
    fun `last write wins`() {
        val writer = provider()
        repeat(50) { writer.storeValueUnsafe(KEY, it) }

        awaitValue(writer, 49, -1)
    }

    companion object {
        private const val KEY = "key"
        private const val TIMEOUT_MILLIS = 5_000L
        private const val POLL_MILLIS = 10L

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun parameters() = listOf(arrayOf("SharedPreferences"), arrayOf("DataStore"))
    }
}
