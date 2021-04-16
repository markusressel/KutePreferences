package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.typedpreferences.PreferenceItem

/**
 * Default implementation of a KutePreferenceDataProvider using a SharedPreferences backed store
 */
class DefaultKutePreferenceDataProvider(context: Context) : KutePreferenceDataProvider {

    private val preferenceHandler = PreferenceHandler(context)

    override fun <DataType : Any> storeValue(kutePreference: KutePreferenceItem<DataType>, newValue: DataType) {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .createPreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        preferenceHandler.setValue(preferenceItem, newValue)
    }

    override fun <DataType : Any> storeValueUnsafe(key: Int, newValue: DataType) {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .createPreferenceItem(key, newValue)

        preferenceHandler.setValue(preferenceItem, newValue)
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .createPreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        return preferenceHandler.getValue(preferenceItem)
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
                .createPreferenceItem(key, defaultValue)
        return preferenceHandler.getValue(preferenceItem)
    }

}