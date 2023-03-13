package de.markusressel.kutepreferences.core.preference.bool

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * Implementation of a boolean preference
 */
open class KuteBooleanPreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    open val descriptionFunction: ((Boolean) -> String)? = null,
    private val defaultValue: Boolean,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null
) : KutePreferenceItem<Boolean>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Boolean = defaultValue

    override fun createDescription(currentValue: Boolean): String {
        descriptionFunction?.let {
            return it(currentValue)
        }

        // if no specific description is given
        // there is no additional value in a "true" or "false" description
        // since it is already visible on the toggle
        return ""
    }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}