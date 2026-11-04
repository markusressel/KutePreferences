package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ChoicePreferenceTest {

    private val dataProvider = DummyDataProvider()
    private val options = mapOf("a" to "First", "b" to "Second", "c" to "Third")

    private fun choice() = KuteChoicePreference(
        key = "c",
        title = "Choice",
        defaultValue = "a",
        options = options,
        dataProvider = dataProvider,
    )

    private fun multi(defaultValue: Set<String> = setOf("a", "c")) = KuteMultiChoicePreference(
        key = "m",
        title = "Multi",
        defaultValue = defaultValue,
        options = options,
        dataProvider = dataProvider,
    )

    @Test
    fun `a choice saves the value and describes it with the title`() {
        val item = choice()
        assertEquals("First", item.description)

        item.persistValue("b")

        assertEquals("b", item.getCurrentPersistedValue())
        assertEquals("Second", item.createDescription("b"))
    }

    @Test
    fun `a saved value that is no option anymore is still described`() {
        assertEquals("removed", choice().createDescription("removed"))
    }

    @Test
    fun `a multi choice describes its titles in the order of the options`() {
        assertEquals("First, Third", multi().createDescription(setOf("c", "a")))
    }

    @Test
    fun `a default that is no option is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            KuteChoicePreference(
                key = "c",
                title = "Choice",
                defaultValue = "missing",
                options = options,
                dataProvider = dataProvider,
            )
        }
        assertThrows(IllegalArgumentException::class.java) { multi(setOf("a", "missing")) }
    }

    @Test
    fun `search finds the title of the selected option`() {
        assertEquals(true, choice().search("First"))
        assertEquals(false, choice().search("Second"))
    }

}
