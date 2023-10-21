package de.markusressel.kutepreferences.core

import androidx.annotation.StringRes
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import kotlinx.coroutines.flow.StateFlow

interface KuteNavigator {

    val currentCategory: StateFlow<Int?>

    /**
     * Navigates to the given [KutePreferenceListItem].
     *
     * @param key the key of a [KutePreferenceListItem]
     */
    fun enterCategory(@StringRes key: Int)

    /**
     * Set a specific set of categories.
     */
    fun setCategories(keys: List<Int>)

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