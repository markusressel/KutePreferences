package de.markusressel.kutepreferences.core.preference.number.slider

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

open class KuteSliderPreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    val minimum: Int,
    val maximum: Int,
    private val defaultValue: Long,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null
) : KutePreferenceItem<Long>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Long = defaultValue

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}
