package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import de.markusressel.kutepreferences.library.KutePreference
import de.markusressel.typedpreferences.PreferenceItem
import de.markusressel.typedpreferences.PreferencesHandlerBase

class PreferenceHandler
constructor(context: Context) : PreferencesHandlerBase(context) {

    // be sure to override the get() method
    override var sharedPreferencesName: String? = null
        get() = "kute_preferences"

    fun <PreferenceType : Any> getPreferenceItem(kutePreference: KutePreference<PreferenceType>): PreferenceItem<PreferenceType> {
        val key = context.getString(kutePreference.key)

        return currentPreferenceItems.getOrPut(key) {
            PreferenceItem(kutePreference.key, kutePreference.defaultValue)
        } as PreferenceItem<PreferenceType>
    }

    private val currentPreferenceItems: MutableMap<String, PreferenceItem<*>> = mutableMapOf()

    override val allPreferenceItems: Set<PreferenceItem<*>>
        get() {
            return setOf(*currentPreferenceItems.values.toTypedArray())
        }

    companion object {
//        val THEME = PreferenceItem(R.string.key_theme, 0)
//        val BOOLEAN_SETTING = PreferenceItem(R.string.key_boolean_setting, true)
//        val COMPLEX_SETTING = PreferenceItem(R.string.key_complex_setting, ComplexClass("Complex ^", 10, listOf(1, 2, 3)))
    }

}
