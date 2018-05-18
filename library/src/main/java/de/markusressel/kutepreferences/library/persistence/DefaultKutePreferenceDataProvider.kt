package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.typedpreferences.PreferenceItem
import de.markusressel.typedpreferences.PreferencesHandlerBase

/**
 * Default implementation of a KutePreferenceDataProvider using a SharedPreferences backed store
 */
class DefaultKutePreferenceDataProvider(context: Context) : PreferencesHandlerBase(context),
        KutePreferenceDataProvider {

    // be sure to override the get() method
    override var sharedPreferencesName: String? = null
        get() = "kute_preferences"

    private val currentPreferenceItems: MutableMap<String, PreferenceItem<*>> = mutableMapOf()

    override val allPreferenceItems: Set<PreferenceItem<*>>
        get() {
            return currentPreferenceItems.values.toSet()
        }

    fun <PreferenceType : Any> getPreferenceItem(
            kutePreference: KutePreferenceItem<PreferenceType>): PreferenceItem<PreferenceType> {
        val key = context.getString(kutePreference.key)

        return currentPreferenceItems.getOrPut(key) {
            PreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        } as PreferenceItem<PreferenceType>
    }

    override fun <DataType : Any> storeValue(kutePreference: KutePreferenceItem<DataType>, newValue: DataType) {
        val preferenceItem: PreferenceItem<DataType> = getPreferenceItem(kutePreference)
        setValue(preferenceItem, newValue)
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        val preferenceItem: PreferenceItem<DataType> = getPreferenceItem(kutePreference)
        return getValue(preferenceItem)
    }

}