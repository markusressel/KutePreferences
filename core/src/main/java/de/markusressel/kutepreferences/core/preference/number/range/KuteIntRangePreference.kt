package de.markusressel.kutepreferences.core.preference.number.range

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition

/**
 * A range of whole numbers. Its slider shows a dot for each value, and has no values in between.
 */
open class KuteIntRangePreference(
    key: String,
    icon: Drawable? = null,
    title: String,
    minimum: Long,
    maximum: Long,
    defaultValue: RangePersistenceModel<Long>,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: (
        (oldValue: RangePersistenceModel<Long>, newValue: RangePersistenceModel<Long>) -> Unit
    )? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    visibleWhen: KuteCondition? = null,
    enabledWhen: KuteCondition? = null,
) : KuteRangePreference<Long>(
    key = key,
    icon = icon,
    title = title,
    minimum = minimum,
    maximum = maximum,
    decimalPlaces = 0,
    defaultValue = defaultValue,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
    onClick = onClick,
    onLongClick = onLongClick,
    visibleWhen = visibleWhen,
    enabledWhen = enabledWhen,
) {

    override fun createDescription(currentValue: RangePersistenceModel<Long>): String {
        return "${currentValue.min} .. ${currentValue.max}"
    }

}
