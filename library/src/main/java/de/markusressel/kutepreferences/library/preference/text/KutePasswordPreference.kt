package de.markusressel.kutepreferences.library.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider

/**
 * Specialized implementation of a text preference for usage with passwords
 */
open class KutePasswordPreference(key: Int,
                                  icon: Drawable? = null,
                                  title: String,
                                  regex: String? = null,
                                  defaultValue: String,
                                  dataProvider: KutePreferenceDataProvider,
                                  onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KuteTextPreference(
                key = key,
                icon = icon,
                title = title,
                regex = regex,
                defaultValue = defaultValue,
                dataProvider = dataProvider,
                onPreferenceChangedListener = onPreferenceChangedListener) {

    override fun onClick(context: Context) {
        KutePasswordPreferenceEditDialog(this, regex).show(context)
    }

    override fun createDescription(currentValue: String): String {
        return if (currentValue.isNotEmpty()) {
            "*".repeat(20)
        } else {
            ""
        }
    }

}