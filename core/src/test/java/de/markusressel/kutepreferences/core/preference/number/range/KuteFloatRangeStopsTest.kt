package de.markusressel.kutepreferences.core.preference.number.range

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class KuteFloatRangeStopsTest {

    private fun range(stops: List<Float>, steps: Int = 0) = KuteFloatRangePreference(
        key = "range",
        title = "Range",
        minimum = 0f,
        maximum = 10f,
        steps = steps,
        stops = stops,
        defaultValue = RangePersistenceModel(0f, 10f),
        dataProvider = DummyDataProvider(),
    )

    @Test
    fun `stops inside of the range are accepted, including its ends`() {
        assertEquals(listOf(0f, 2.5f, 10f), range(listOf(0f, 2.5f, 10f)).stops)
    }

    @Test
    fun `a stop outside of the range is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { range(listOf(11f)) }
        assertThrows(IllegalArgumentException::class.java) { range(listOf(-1f)) }
    }

    @Test
    fun `steps and stops cannot be combined`() {
        assertThrows(IllegalArgumentException::class.java) { range(listOf(1f), steps = 3) }
    }

}
