package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.typedpreferences.PreferenceItem
import de.markusressel.typedpreferences.PreferencesHandlerBase

class PreferenceHandler(context: Context) : PreferencesHandlerBase(context) {

    // be sure to override the get() method
    override var sharedPreferencesName: String? = null
        get() = "kute_preferences"

    fun <PreferenceType : Any> getPreferenceItem(
            kutePreference: KutePreferenceItem<PreferenceType>): PreferenceItem<PreferenceType> {
        val key = context.getString(kutePreference.key)

        return currentPreferenceItems.getOrPut(key) {
            PreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        } as PreferenceItem<PreferenceType>
    }

    private val currentPreferenceItems: MutableMap<String, PreferenceItem<*>> = mutableMapOf()

    override val allPreferenceItems: Set<PreferenceItem<*>>
        get() {
            return currentPreferenceItems.values.toSet()
        }

}
