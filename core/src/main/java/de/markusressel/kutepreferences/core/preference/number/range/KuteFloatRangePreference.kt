package de.markusressel.kutepreferences.core.preference.number.range

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import kotlin.math.floor

open class KuteFloatRangePreference(
    key: String,
    icon: Drawable? = null,
    title: String,
    minimum: Float,
    maximum: Float,
    decimalPlaces: Int = 1,
    defaultValue: RangePersistenceModel<Float>,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: ((oldValue: RangePersistenceModel<Float>, newValue: RangePersistenceModel<Float>) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    visibleWhen: KuteCondition? = null,
    enabledWhen: KuteCondition? = null,
    /**
     * The number of values in between [minimum] and [maximum] that the slider snaps to, or 0 for no snapping.
     * Each of them is shown as a dot.
     */
    val steps: Int = 0,
    keywords: List<String> = emptyList(),
) : KuteRangePreference<Float>(
    key = key,
    icon = icon,
    title = title,
    minimum = minimum,
    maximum = maximum,
    decimalPlaces = decimalPlaces,
    defaultValue = defaultValue,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
    onClick = onClick,
    onLongClick = onLongClick,
    visibleWhen = visibleWhen,
    enabledWhen = enabledWhen,
    keywords = keywords,
) {

    init {
        require(steps >= 0) { "The steps of \"$key\" must not be negative" }
    }

    override fun createDescription(currentValue: RangePersistenceModel<Float>): String {
        val start = formatValue(currentValue.min)
        val end = formatValue(currentValue.max)

        return "$start .. $end"
    }

    /**
     * @return the text for a single end of the range, which is also shown on the handles of the slider
     */
    open fun formatValue(value: Float): String = formatNumberForDescription(value, decimalPlaces)

    private fun formatNumberForDescription(
        number: Float,
        decimalPlacesWhenNotWhole: Int
    ): String {
        val decimalPlaces = if (number == floor(number)) {
            // number is a whole number
            0
        } else {
            decimalPlacesWhenNotWhole
        }

        return "%.${decimalPlaces}f".format(number)
    }
}