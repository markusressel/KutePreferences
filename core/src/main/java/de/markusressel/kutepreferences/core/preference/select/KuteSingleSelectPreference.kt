package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> : KutePreferenceItem<T>, KutePreferenceListItem {
    abstract val possibleValues: Map<Int, T>

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}