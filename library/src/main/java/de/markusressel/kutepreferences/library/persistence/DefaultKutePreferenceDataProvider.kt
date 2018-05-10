package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.typedpreferences.PreferenceItem

class DefaultKutePreferenceDataProvider(context: Context) : KutePreferenceDataProvider {

    private val preferenceHandler = PreferenceHandler(context)

    override fun <DataType : Any> storeValue(kutePreference: KutePreferenceItem<DataType>, newValue: DataType) {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .getPreferenceItem(kutePreference)
        preferenceHandler
                .setValue(preferenceItem, newValue)
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .getPreferenceItem(kutePreference)
        return preferenceHandler
                .getValue(preferenceItem)
    }

}