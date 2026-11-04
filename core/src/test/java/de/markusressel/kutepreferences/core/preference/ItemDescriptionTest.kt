package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemDescriptionTest {

    @Test
    fun `color is described as argb hex`() {
        val item = KuteColorPreference(key = "c", title = "C", defaultValue = 0, dataProvider = DummyDataProvider())

        assertEquals("#80ff0001", item.createDescription(0x80FF0001.toInt()))
        assertEquals("#00000000", item.createDescription(0))
    }
}
