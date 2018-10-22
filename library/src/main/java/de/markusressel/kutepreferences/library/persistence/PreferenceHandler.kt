package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import androidx.annotation.StringRes
import de.markusressel.typedpreferences.PreferenceItem
import de.markusressel.typedpreferences.PreferencesHandlerBase

class PreferenceHandler(context: Context) : PreferencesHandlerBase(context) {

    // be sure to override the get() method
    override var sharedPreferencesName: String? = null
        get() = "kute_preferences"

    /**
     * Creates a PreferenceItem for the given key and default value
     *
     * @param key the preference key
     * @param defaultValue the default value for the preference
     * @return a PreferenceItem
     */
    fun <PreferenceType : Any> createPreferenceItem(@StringRes key: Int,
                                                    defaultValue: PreferenceType): PreferenceItem<PreferenceType> {
        val keyString = context.getString(key)

        return currentPreferenceItems.getOrPut(keyString) {
            PreferenceItem(key, defaultValue)
        } as PreferenceItem<PreferenceType>
    }

    private val currentPreferenceItems: MutableMap<String, PreferenceItem<*>> = mutableMapOf()

    override val allPreferenceItems: Set<PreferenceItem<*>>
        get() {
            return currentPreferenceItems.values.toSet()
        }

}
