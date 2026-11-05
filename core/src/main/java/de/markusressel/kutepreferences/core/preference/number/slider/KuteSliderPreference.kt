package de.markusressel.kutepreferences.core.preference.number.slider

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.KuteSearchContent

abstract class KuteSliderPreference<T : Number>(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    val minimum: T,
    val maximum: T,
    private val defaultValue: T,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: T, newValue: T) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val keywords: List<String> = emptyList(),
) : KutePreferenceItem<T>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): T = defaultValue

    override val searchContent
        get() = KuteSearchContent(title = title, keywords = keywords, description = description)

}
