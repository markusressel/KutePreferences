package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase

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
        KutePreferenceBase<String>() {

    override fun getDefaultValue(): String = defaultValue

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun onClick(context: Context) {
        val dialog = KuteTextPreferenceEditDialog(this, regex)
        dialog
                .show(context)
    }

}