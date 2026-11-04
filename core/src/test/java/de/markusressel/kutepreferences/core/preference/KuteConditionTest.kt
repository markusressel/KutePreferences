package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KuteConditionTest {

    private val source = KuteBooleanPreference(
        key = "source",
        title = "Source",
        defaultValue = false,
        dataProvider = DummyDataProvider(),
    )

    @Test
    fun `matches follows the saved value`() = runTest {
        val condition = source.matches { it }
        assertFalse(condition.isMet)
        assertFalse(condition.changes.first())

        source.persistValue(true)

        assertTrue(condition.isMet)
        assertTrue(condition.changes.first())
    }

    @Test
    fun `conditions can be combined`() = runTest {
        val first = MutableStateFlow(true)
        val second = MutableStateFlow(false)

        assertFalse((first.asCondition() and second.asCondition()).isMet)
        assertFalse((first.asCondition() and second.asCondition()).changes.first())
        assertTrue((first.asCondition() or second.asCondition()).isMet)
        assertTrue((first.asCondition() or second.asCondition()).changes.first())
        assertFalse((!first.asCondition()).isMet)
        assertFalse((!first.asCondition()).changes.first())
    }

    @Test
    fun `hidden items and their children are not found`() {
        val hidden = MutableStateFlow(false)
        val child = KuteAction(key = "child", title = "needle")
        val section = KuteSection(
            key = "section",
            title = "needle",
            children = listOf(child),
            visibleWhen = hidden.asCondition(),
        )

        assertEquals(emptyList<KutePreferenceListItem>(), listOf(section).filterRecursive("needle"))

        hidden.value = true

        assertEquals(listOf(section, child), listOf(section).filterRecursive("needle"))
    }

    @Test
    fun `disabled items are found`() {
        val item = KuteAction(key = "a", title = "needle", enabledWhen = MutableStateFlow(false).asCondition())

        assertEquals(listOf<KutePreferenceListItem>(item), listOf(item).filterRecursive("needle"))
    }

}
