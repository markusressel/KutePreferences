package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.date.DatePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersistedPreferenceBehaviorTest {

    private val dataProvider = DummyDataProvider()

    private fun textBehavior(regex: String? = null) = TextPreferenceBehavior(
        KuteTextPreference(key = "1", title = "Text", regex = regex, defaultValue = "default", dataProvider = dataProvider)
    )

    @Test
    fun `input only changes the current value until persisted`() {
        val behavior = textBehavior()

        behavior.onInputChanged("edited")
        assertEquals("edited", behavior.currentValue.value)
        assertEquals("default", behavior.persistedValue.value)

        behavior.persistCurrentValue()
        assertEquals("edited", behavior.persistedValue.value)
    }

    @Test
    fun `cancel restores the persisted value`() {
        val behavior = textBehavior()

        behavior.onInputChanged("edited")
        behavior.onCancelClicked()

        assertEquals("default", behavior.currentValue.value)
    }

    @Test
    fun `default restores the default without persisting`() {
        val behavior = textBehavior()
        behavior.onInputChanged("edited")
        behavior.persistCurrentValue()

        behavior.onDefaultClicked()

        assertEquals("default", behavior.currentValue.value)
        assertEquals("edited", behavior.persistedValue.value)
    }

    @Test
    fun `listener receives old and new value`() {
        val changes = mutableListOf<Pair<String, String>>()
        val behavior = TextPreferenceBehavior(
            KuteTextPreference(
                key = "1",
                title = "Text",
                defaultValue = "default",
                dataProvider = dataProvider,
                onPreferenceChangedListener = { old, new -> changes += old to new },
            )
        )

        behavior.onInputChanged("edited")
        behavior.persistCurrentValue()
        behavior.persistCurrentValue()

        assertEquals(listOf("default" to "edited"), changes)
    }

    @Test
    fun `text validity follows the regex`() {
        val behavior = textBehavior(regex = "^a+$")

        behavior.onInputChanged("aaa")
        assertTrue(behavior.isValid())

        behavior.onInputChanged("b")
        assertFalse(behavior.isValid())
    }

    @Test
    fun `number validity follows the bounds`() {
        val behavior = NumberPreferenceBehavior(
            KuteNumberPreference(
                key = "1",
                title = "Number",
                minimum = 0,
                maximum = 10,
                defaultValue = 5,
                dataProvider = dataProvider,
            )
        )

        behavior.onInputChanged(10)
        assertTrue(behavior.isValid())

        behavior.onInputChanged(11)
        assertFalse(behavior.isValid())

        behavior.onInputChanged(-1)
        assertFalse(behavior.isValid())
    }

    @Test
    fun `validator is checked in addition to the built-in rules`() {
        val text = TextPreferenceBehavior(
            KuteTextPreference(
                key = "1",
                title = "Text",
                regex = "^a+$",
                defaultValue = "a",
                dataProvider = dataProvider,
                validator = { it.length < 3 },
            )
        )

        text.onInputChanged("aa")
        assertTrue(text.isValid())
        text.onInputChanged("aaa")
        assertFalse(text.isValid())
        text.onInputChanged("b")
        assertFalse(text.isValid())
    }

    @Test
    fun `date validity follows the bounds`() {
        val date = DatePreferenceBehavior(
            KuteDatePreference(key = "1", title = "Date", minimum = 10, maximum = 20, defaultValue = 15, dataProvider = dataProvider)
        )

        date.onInputChanged(20)
        assertTrue(date.isValid())
        date.onInputChanged(21)
        assertFalse(date.isValid())
        date.onInputChanged(9)
        assertFalse(date.isValid())
    }

    @Test
    fun `password and url items pass their validator to the behavior`() {
        val password = PasswordPreferenceBehavior(
            KutePasswordPreference(
                key = "1",
                title = "Password",
                regex = null,
                defaultValue = "",
                dataProvider = dataProvider,
                validator = { it != "forbidden" },
            )
        )

        password.onInputChanged("forbidden")
        assertFalse(password.isValid())
    }
}
