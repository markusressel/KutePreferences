package de.markusressel.kutepreferences.core.preference.number.range

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RangePreferenceTest {

    @Test
    fun `an int range saves whole numbers and describes them`() {
        val item = KuteIntRangePreference(
            key = "int_range",
            title = "Range",
            minimum = 0,
            maximum = 10,
            defaultValue = RangePersistenceModel(2, 5),
            dataProvider = DummyDataProvider(),
        )
        assertEquals("2 .. 5", item.description)

        item.persistValue(RangePersistenceModel(3, 7))

        assertEquals(RangePersistenceModel(3L, 7L), item.getCurrentPersistedValue())
        assertEquals("3 .. 7", item.createDescription(RangePersistenceModel(3, 7)))
    }

    @Test
    fun `a float range has no steps by default`() {
        assertEquals(0, floatRange().steps)
        assertEquals(3, floatRange(steps = 3).steps)
    }

    @Test
    fun `negative steps are rejected`() {
        assertThrows(IllegalArgumentException::class.java) { floatRange(steps = -1) }
    }

    private fun floatRange(steps: Int = 0) = KuteFloatRangePreference(
        key = "float_range",
        title = "Range",
        minimum = 0f,
        maximum = 1f,
        defaultValue = RangePersistenceModel(0f, 1f),
        dataProvider = DummyDataProvider(),
        steps = steps,
    )

}
