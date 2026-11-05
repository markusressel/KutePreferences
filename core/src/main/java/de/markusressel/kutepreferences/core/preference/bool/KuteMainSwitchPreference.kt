package de.markusressel.kutepreferences.core.preference.bool

import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition

/**
 * A prominent switch that turns a whole feature on or off. While it is off, all other items
 * of the same category or section are disabled, including their children.
 *
 * Put it at the top of the category or section whose items depend on it. Those items keep their saved values.
 */
open class KuteMainSwitchPreference(
    key: String,
    title: String,
    descriptionFunction: ((Boolean) -> String)? = null,
    defaultValue: Boolean,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    visibleWhen: KuteCondition? = null,
    enabledWhen: KuteCondition? = null,
    keywords: List<String> = emptyList(),
) : KuteBooleanPreference(
    key = key,
    title = title,
    descriptionFunction = descriptionFunction,
    defaultValue = defaultValue,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
    onClick = onClick,
    onLongClick = onLongClick,
    visibleWhen = visibleWhen,
    enabledWhen = enabledWhen,
    keywords = keywords,
)
