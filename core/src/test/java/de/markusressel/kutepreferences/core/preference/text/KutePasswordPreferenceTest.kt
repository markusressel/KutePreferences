package de.markusressel.kutepreferences.core.preference.text

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class KutePasswordPreferenceTest {

    private val item = KutePasswordPreference(
        key = "password",
        title = "Password",
        defaultValue = "",
        dataProvider = DummyDataProvider(),
    )

    @Test
    fun `the mask does not reveal the length of the password`() {
        assertEquals(item.createDescription("short"), item.createDescription("a much longer value"))
    }

    @Test
    fun `the mask does not contain the password`() {
        assertEquals(false, item.createDescription("secret").contains("secret"))
    }

    @Test
    fun `an empty password has an empty mask`() {
        assertEquals("", item.createDescription(""))
    }

}
