package de.markusressel.kutepreferences.core.preference.number.range

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord


/**
 * Base class for preferences defining some kind of range
 */
abstract class KuteRangePreference<T : Number>(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    val minimum: T,
    val maximum: T,
    val decimalPlaces: Int,
    private val defaultValue: RangePersistenceModel<T>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: RangePersistenceModel<T>, newValue: RangePersistenceModel<T>) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null
) : KutePreferenceItem<RangePersistenceModel<T>>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): RangePersistenceModel<T> = defaultValue

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}