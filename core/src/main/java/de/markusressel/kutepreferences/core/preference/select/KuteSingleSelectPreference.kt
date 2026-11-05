package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.KuteSearchContent

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> : KutePreferenceItem<T>, KutePreferenceListItem {

    /**
     * The string resource of the value to save for each option, mapped to the title to show for it.
     * Not needed if [options] is implemented directly.
     */
    open val possibleValues: Map<Int, T>
        get() = emptyMap()

    /**
     * The value to save for each option, mapped to the title to show for it
     */
    open val options: Map<String, String>
        get() = emptyMap()

    override val persistedValue by lazy { getPersistedValueFlow() }

    override val searchContent
        get() = KuteSearchContent(title = title, keywords = keywords, description = description)

}