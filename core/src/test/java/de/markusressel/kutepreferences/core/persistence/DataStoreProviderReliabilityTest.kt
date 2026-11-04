package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class DataStoreProviderReliabilityTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val scopes = mutableListOf<CoroutineScope>()

    private fun newScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO).also { scopes += it }

    @After
    fun tearDown() {
        scopes.forEach { it.cancel() }
    }

    /**
     * A write racing with the initial read of the store used to be missed about once every 300 attempts.
     */
    @Test(timeout = 60_000)
    fun `write directly after creating the provider is not lost`() {
        repeat(ATTEMPTS) { attempt ->
            val scope = newScope()
            val dataStore = PreferenceDataStoreFactory.create(scope = scope) {
                File(context.cacheDir, "race_$attempt.preferences_pb")
            }
            val provider = DataStoreKutePreferenceDataProvider(dataStore, scope)

            provider.storeValueUnsafe(KEY, 42)

            val deadline = System.currentTimeMillis() + TIMEOUT_MILLIS
            while (provider.getValueUnsafe(KEY, 1) != 42 && System.currentTimeMillis() < deadline) {
                Thread.sleep(POLL_MILLIS)
            }
            val value: Int = provider.getValueUnsafe(KEY, 1)
            assertEquals("attempt $attempt", 42, value)
            scope.cancel()
        }
    }

    @Test(timeout = 10_000)
    fun `unreadable store returns the default instead of blocking forever`() {
        val brokenStore = object : DataStore<Preferences> {
            override val data: Flow<Preferences> = flow { throw IllegalStateException("broken") }
            override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
                throw IllegalStateException("broken")
            }
        }

        val provider = DataStoreKutePreferenceDataProvider(brokenStore, newScope())

        assertEquals("default", provider.getValueUnsafe(KEY, "default"))
    }

    private companion object {
        const val ATTEMPTS = 500
        const val KEY = "key"
        const val TIMEOUT_MILLIS = 3_000L
        const val POLL_MILLIS = 2L
    }
}
