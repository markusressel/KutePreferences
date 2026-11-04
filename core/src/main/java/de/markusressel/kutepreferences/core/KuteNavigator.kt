package de.markusressel.kutepreferences.core

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import kotlinx.coroutines.flow.StateFlow

interface KuteNavigator {

    /**
     * The key of the category that is currently shown, or null for the top level
     */
    val currentCategory: StateFlow<String?>

    /**
     * Navigates to the given [KutePreferenceListItem].
     *
     * @param key the key of a [KutePreferenceListItem]
     */
    fun enterCategory(key: String)

    /**
     * Replaces the current category stack with the given categories, ordered from root to leaf.
     */
    fun setCategories(keys: List<String>)

    /**
     * Navigates back to the previous category, or overview screen, or the previous backstack entry.
     *
     * @return true, if the event was consumed, false otherwise
     */
    fun goBack(): Boolean

    /**
     * Clears the current category stack.
     */
    fun backToTop()

}
