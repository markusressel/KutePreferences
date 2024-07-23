package de.markusressel.kutepreferences.core.preference.number.slider

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import kotlin.math.floor

open class KuteFloatSliderPreference(
    key: Int,
    icon: Drawable? = null,
    title: String,
    minimum: Float,
    maximum: Float,
    val decimalPlacesWhenNotWhole: Int = 2,
    defaultValue: Float,
    dataProvider: KutePreferenceDataProvider,
    onPreferenceChangedListener: ((oldValue: Float, newValue: Float) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) : KuteSliderPreference<Float>(
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
), KutePreferenceListItem {

    override fun createDescription(currentValue: Float): String {
        return formatNumberForDescription(currentValue, decimalPlacesWhenNotWhole)
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
