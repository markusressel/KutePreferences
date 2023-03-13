package de.markusressel.kutepreferences.core.preference.number

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * Implementation of a Long preference for selecting a number
 */
open class KuteNumberPreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    val minimum: Int? = null,
    val maximum: Int? = null,
    private val defaultValue: Long,
    val unit: String? = null,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KutePreferenceItem<Long>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Long = defaultValue

    override fun createDescription(currentValue: Long): String {
        unit?.let {
            return "$currentValue $it"
        }

        return "$currentValue"
    }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}