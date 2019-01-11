package de.markusressel.kutepreferences.preference.number.range

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

open class KuteIntRangePreference(
        key: Int,
        icon: Drawable? = null,
        title: String,
        minimum: Int,
        maximum: Int,
        defaultValue: RangePersistenceModel<Int>,
        dataProvider: KutePreferenceDataProvider,
        onPreferenceChangedListener: ((oldValue: RangePersistenceModel<Int>, newValue: RangePersistenceModel<Int>) -> Unit)? = null) :
        KuteRangePreference<Int>(key, icon, title, minimum, maximum, 0, defaultValue, dataProvider, onPreferenceChangedListener) {

    override fun onListItemClicked(context: Context) {
        KuteIntRangePreferenceEditDialog(this, minimum, maximum).show(context)
    }

}