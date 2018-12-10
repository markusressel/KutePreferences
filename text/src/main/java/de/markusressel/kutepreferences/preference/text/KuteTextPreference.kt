package de.markusressel.kutepreferences.preference.text

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

/**
 * Implementation for a text preference
 */
open class KuteTextPreference(override val key: Int,
                              override val icon: Drawable? = null,
                              override val title: String,
                              protected val regex: String? = null,
                              private val defaultValue: String,
                              override val dataProvider: KutePreferenceDataProvider,
                              override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceItem<String> {

    override fun getDefaultValue(): String = defaultValue

}