package de.markusressel.kutepreferences.preference.number.range

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase
import de.markusressel.kutepreferences.preference.number.R

abstract class KuteRangePreference<T : Number>(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        protected val minimum: T,
        protected val maximum: T,
        private val defaultValue: RangePersistenceModel<T>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: RangePersistenceModel<T>, newValue: RangePersistenceModel<T>) -> Unit)? = null) :
        KutePreferenceBase<RangePersistenceModel<T>>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): RangePersistenceModel<T> = defaultValue

    override fun createDescription(currentValue: RangePersistenceModel<T>): String {
        val start = formatNumberForDescription(currentValue.min)
        val end = formatNumberForDescription(currentValue.max)

        return "$start .. $end"
    }

    protected open fun formatNumberForDescription(number: T): String {
        val decimalPlaces = if (number == Math.floor(number.toDouble())) {
            // number is a whole number
            0
        } else {
            1
        }

        return "%.${decimalPlaces}f".format(number.toFloat())
    }

}