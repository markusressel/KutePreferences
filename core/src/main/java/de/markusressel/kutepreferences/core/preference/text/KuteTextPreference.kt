package de.markusressel.kutepreferences.core.preference.text

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

open class KuteTextPreference(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    val regex: String? = null,
    private val defaultValue: String,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val validator: Validator<String>? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
) : KutePreferenceItem<String>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue() = defaultValue

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}