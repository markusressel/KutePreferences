package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataStoreProviderInstancesTest {

    @Test
    fun `multiple providers for the same context can be used at the same time`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val first = DataStoreKutePreferenceDataProvider(context)
        val second = DataStoreKutePreferenceDataProvider(context)

        runBlocking {
            first.load()
            second.load()
        }

        assertEquals("default", first.getValueUnsafe("key", "default"))
        assertEquals("default", second.getValueUnsafe("key", "default"))
    }
}
