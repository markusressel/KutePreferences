package de.markusressel.kutepreferences.core.preference.number.range

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import kotlin.math.floor

open class KuteFloatRangePreference(
    key: Int,
    icon: Drawable? = null,
    title: String,
    minimum: Float,
    maximum: Float,
    decimalPlaces: Int = 1,
    defaultValue: RangePersistenceModel<Float>,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: ((oldValue: RangePersistenceModel<Float>, newValue: RangePersistenceModel<Float>) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) : KuteRangePreference<Float>(
    key,
    icon,
    title,
    minimum,
    maximum,
    decimalPlaces,
    defaultValue,
    dataProvider,
    onPreferenceChangedListener,
    onClick,
    onLongClick,
) {
    override fun createDescription(currentValue: RangePersistenceModel<Float>): String {
        val start = formatNumberForDescription(currentValue.min, decimalPlaces)
        val end = formatNumberForDescription(currentValue.max, decimalPlaces)

        return "$start .. $end"
    }

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