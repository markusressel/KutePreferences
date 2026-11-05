package de.markusressel.kutepreferences.core.preference.number.slider

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem

open class KuteIntSliderPreference(
    key: String,
    icon: Drawable? = null,
    title: String,
    minimum: Long,
    maximum: Long,
    defaultValue: Long,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    visibleWhen: KuteCondition? = null,
    enabledWhen: KuteCondition? = null,
    keywords: List<String> = emptyList(),
) : KuteSliderPreference<Long>(
    key = key,
    icon = icon,
    title = title,
    minimum = minimum,
    maximum = maximum,
    defaultValue = defaultValue,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
    onClick = onClick,
    onLongClick = onLongClick,
    visibleWhen = visibleWhen,
    enabledWhen = enabledWhen,
    keywords = keywords,
), KutePreferenceListItem {

}
