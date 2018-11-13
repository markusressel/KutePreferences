package de.markusressel.kutepreferences.preference.number.range

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

open class KuteFloatRangePreference(
        key: Int,
        icon: Drawable? = null,
        title: String,
        minimum: Float,
        maximum: Float,
        decimalPlaces: Int = 1,
        defaultValue: RangePersistenceModel<Float>,
        dataProvider: KutePreferenceDataProvider,
        onPreferenceChangedListener: ((oldValue: RangePersistenceModel<Float>, newValue: RangePersistenceModel<Float>) -> Unit)? = null) :
        KuteRangePreference<Float>(key, icon, title, minimum, maximum, decimalPlaces, defaultValue, dataProvider, onPreferenceChangedListener) {

    override fun onClick(context: Context) {
        val dialog = KuteFloatRangePreferenceEditDialog(this, minimum, maximum, decimalPlaces)
        dialog.show(context)
    }

}