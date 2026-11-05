package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationErrorTest {

    private val dataProvider = DummyDataProvider()

    private fun text(
        regex: String? = null,
        validator: Validator<String>? = null,
        message: ((String) -> String)? = null,
    ) = TextPreferenceBehavior(
        KuteTextPreference(
            key = "text",
            title = "Text",
            regex = regex,
            defaultValue = "",
            dataProvider = dataProvider,
            validator = validator,
            validationMessage = message,
        )
    )

    private fun number(minimum: Int? = null, maximum: Int? = null, message: ((Long) -> String)? = null) =
        NumberPreferenceBehavior(
            KuteNumberPreference(
                key = "number",
                title = "Number",
                minimum = minimum,
                maximum = maximum,
                defaultValue = 5,
                dataProvider = dataProvider,
                validationMessage = message,
            )
        )

    private fun password(regex: String? = null, useDefaultRule: Boolean = true) = PasswordPreferenceBehavior(
        when {
            useDefaultRule -> KutePasswordPreference(
                key = "password",
                title = "Password",
                defaultValue = "",
                dataProvider = dataProvider,
            )

            else -> KutePasswordPreference(
                key = "password",
                title = "Password",
                regex = regex,
                defaultValue = "",
                dataProvider = dataProvider,
            )
        }
    )

    @Test
    fun `a valid value has no error`() {
        val behavior = text(validator = { it.length <= 2 }, message = { "too long" })

        behavior.onInputChanged("ab")

        assertNull(behavior.validationError())
        assertTrue(behavior.isValid())
    }

    @Test
    fun `a rejected value has a generic error without a message`() {
        val behavior = text(validator = { false })

        assertEquals(KuteValidationError.Invalid, behavior.validationError())
        assertFalse(behavior.isValid())
    }

    @Test
    fun `the message gets the rejected value`() {
        val behavior = text(validator = { it.length <= 2 }, message = { "${it.length} is too long" })

        behavior.onInputChanged("abc")

        assertEquals(KuteValidationError.Message("3 is too long"), behavior.validationError())
    }

    @Test
    fun `a message also replaces the text of built-in rules`() {
        val behavior = number(minimum = 10, message = { "more please" })

        assertEquals(KuteValidationError.Message("more please"), behavior.validationError())
    }

    @Test
    fun `a regex that does not match is an error`() {
        val behavior = text(regex = "a+")

        behavior.onInputChanged("b")

        assertEquals(KuteValidationError.Invalid, behavior.validationError())
    }

    @Test
    fun `a number knows its range`() {
        assertEquals(KuteValidationError.OutOfRange(10, 20), number(minimum = 10, maximum = 20).validationError())
        assertEquals(KuteValidationError.OutOfRange(10, null), number(minimum = 10).validationError())
        assertEquals(KuteValidationError.OutOfRange(null, 2), number(maximum = 2).validationError())
        assertNull(number(minimum = 5, maximum = 5).validationError())
    }

    @Test
    fun `the default password rule explains the minimum length`() {
        val behavior = password()

        behavior.onInputChanged("short")
        assertEquals(KuteValidationError.TooShort(8), behavior.validationError())

        behavior.onInputChanged("")
        assertNull(behavior.validationError())

        behavior.onInputChanged("long enough")
        assertNull(behavior.validationError())
    }

    @Test
    fun `a custom password rule has a generic error`() {
        val behavior = password(regex = "a+", useDefaultRule = false)

        behavior.onInputChanged("b")

        assertEquals(KuteValidationError.Invalid, behavior.validationError())
    }

    @Test
    fun `a custom isValid still decides whether a value can be saved`() {
        val behavior = object : TextPreferenceBehavior(
            KuteTextPreference(key = "t", title = "T", defaultValue = "", dataProvider = dataProvider)
        ) {
            override fun isValid() = false
        }

        assertFalse(behavior.isValid())
        assertNull(behavior.validationError())
    }

}
