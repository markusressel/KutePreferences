package de.markusressel.kutepreferences.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultKuteNavigatorTest {

    private val navigator = DefaultKuteNavigator()

    @Test
    fun `starts at the top level`() {
        assertNull(navigator.currentCategory.value)
    }

    @Test
    fun `goBack at the top level is not consumed`() {
        assertFalse(navigator.goBack())
    }

    @Test
    fun `enterCategory and goBack walk the stack`() {
        navigator.enterCategory(CATEGORY_A)
        navigator.enterCategory(CATEGORY_B)
        assertEquals(CATEGORY_B, navigator.currentCategory.value)

        assertTrue(navigator.goBack())
        assertEquals(CATEGORY_A, navigator.currentCategory.value)

        assertTrue(navigator.goBack())
        assertNull(navigator.currentCategory.value)

        assertFalse(navigator.goBack())
    }

    @Test
    fun `setCategories pushes every key`() {
        navigator.setCategories(listOf(CATEGORY_A, CATEGORY_B))
        assertEquals(CATEGORY_B, navigator.currentCategory.value)

        navigator.goBack()
        assertEquals(CATEGORY_A, navigator.currentCategory.value)
    }

    @Test
    fun `backToTop clears the stack`() {
        navigator.setCategories(listOf(CATEGORY_A, CATEGORY_B))

        navigator.backToTop()

        assertNull(navigator.currentCategory.value)
        assertFalse(navigator.goBack())
    }

    private companion object {
        const val CATEGORY_A = 1
        const val CATEGORY_B = 2
    }
}
