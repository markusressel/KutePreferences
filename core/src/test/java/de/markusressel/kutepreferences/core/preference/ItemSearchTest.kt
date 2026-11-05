package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.bool.KuteMainSwitchPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemSearchTest {

    private val dataProvider = DummyDataProvider()
    private val keywords = listOf("synonym")

    private val allItems: List<KutePreferenceListItem> = listOf(
        KuteAction(key = "a", title = "T", keywords = keywords),
        KuteCategory(key = "b", title = "T", keywords = keywords),
        KuteSection(key = "c", title = "T", children = emptyList(), keywords = keywords),
        KuteBooleanPreference(key = "d", title = "T", defaultValue = true, dataProvider = dataProvider, keywords = keywords),
        KuteMainSwitchPreference(key = "e", title = "T", defaultValue = true, dataProvider = dataProvider, keywords = keywords),
        KuteTextPreference(key = "f", title = "T", defaultValue = "", dataProvider = dataProvider, keywords = keywords),
        KuteUrlPreference(key = "g", title = "T", defaultValue = "", dataProvider = dataProvider, keywords = keywords),
        KutePasswordPreference(key = "h", title = "T", defaultValue = "", dataProvider = dataProvider, keywords = keywords),
        KuteNumberPreference(key = "i", title = "T", defaultValue = 1, dataProvider = dataProvider, keywords = keywords),
        KuteIntSliderPreference(
            key = "j", title = "T", minimum = 0, maximum = 1, defaultValue = 0, dataProvider = dataProvider, keywords = keywords,
        ),
        KuteFloatSliderPreference(
            key = "k", title = "T", minimum = 0f, maximum = 1f, defaultValue = 0f, dataProvider = dataProvider, keywords = keywords,
        ),
        KuteIntRangePreference(
            key = "l", title = "T", minimum = 0, maximum = 1, defaultValue = RangePersistenceModel(0L, 1L),
            dataProvider = dataProvider, keywords = keywords,
        ),
        KuteFloatRangePreference(
            key = "m", title = "T", minimum = 0f, maximum = 1f, defaultValue = RangePersistenceModel(0f, 1f),
            dataProvider = dataProvider, keywords = keywords,
        ),
        KuteDatePreference(key = "n", title = "T", defaultValue = 0, dataProvider = dataProvider, keywords = keywords),
        KuteTimePreference(
            key = "o", title = "T", defaultValue = TimePersistenceModel(1, 2), dataProvider = dataProvider, keywords = keywords,
        ),
        KuteColorPreference(key = "p", title = "T", defaultValue = 0, dataProvider = dataProvider, keywords = keywords),
        KuteChoicePreference(
            key = "q", title = "T", defaultValue = "x", options = mapOf("x" to "X"), dataProvider = dataProvider, keywords = keywords,
        ),
        KuteMultiChoicePreference(
            key = "r", title = "T", options = mapOf("x" to "X"), dataProvider = dataProvider, keywords = keywords,
        ),
    )

    @Test
    fun `every item type can be found by its title and its keywords`() {
        allItems.forEach { item ->
            assertTrue("${item::class.simpleName} by title", item.search("t"))
            assertTrue("${item::class.simpleName} by keyword", item.search("synonym"))
            assertFalse("${item::class.simpleName} by unrelated word", item.search("unrelated"))
        }
    }

    @Test
    fun `a keyword does not show up in the description`() {
        allItems.filterIsInstance<KutePreferenceItem<*>>().forEach { item ->
            assertFalse(item.description.contains("synonym"))
        }
    }

    @Test
    fun `a text preference is found by its saved value`() {
        val item = KuteTextPreference(key = "t", title = "Server", defaultValue = "example.org", dataProvider = dataProvider)

        assertTrue(item.search("example"))
    }

    @Test
    fun `a choice is found by the title of the selected option and not by another option`() {
        val item = KuteChoicePreference(
            key = "c",
            title = "Theme",
            defaultValue = "dark",
            options = mapOf("dark" to "Night mode", "light" to "Day mode"),
            dataProvider = dataProvider,
        )

        assertTrue(item.search("night"))
        assertFalse(item.search("day"))
    }

    @Test
    fun `a number is found by its value and unit`() {
        val item = KuteNumberPreference(key = "n", title = "Timeout", defaultValue = 30, unit = "seconds", dataProvider = dataProvider)

        assertTrue(item.search("30"))
        assertTrue(item.search("seconds"))
    }

    @Test
    fun `a boolean is not found by its value`() {
        val item = KuteBooleanPreference(key = "b", title = "Wifi", defaultValue = true, dataProvider = dataProvider)

        assertFalse(item.search("true"))
        assertFalse(item.search("on"))
    }

    @Test
    fun `a boolean is found by the description of both states, whatever its value is`() {
        listOf(true, false).forEach { value ->
            val item = KuteBooleanPreference(
                key = "b$value",
                title = "Wifi",
                descriptionFunction = { isOn ->
                    when {
                        isOn -> "Scans for networks"
                        else -> "Saves battery"
                    }
                },
                defaultValue = value,
                dataProvider = dataProvider,
            )

            assertTrue(item.search("scans"))
            assertTrue(item.search("battery"))
        }
    }

    @Test
    fun `a password is not found by its value`() {
        val item = KutePasswordPreference(key = "p", title = "Secret", defaultValue = "hunter22", dataProvider = dataProvider)

        assertFalse(item.search("hunter22"))
    }

}
