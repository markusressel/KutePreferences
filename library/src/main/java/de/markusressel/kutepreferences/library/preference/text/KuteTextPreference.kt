package de.markusressel.kutepreferences.library.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

open class KuteTextPreference(override val key: Int,
                              override val icon: Drawable? = null,
                              override val title: String,
                              private val minLength: Int? = null,
                              private val maxLength: Int? = null,
                              private val regex: String? = null,
                              private val isPassword: Boolean = false,
                              private val defaultValue: String,
                              override val dataProvider: KutePreferenceDataProvider,
                              override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceBase<String>() {

    override fun getDefaultValue(): String = defaultValue

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun onClick(context: Context) {
        val dialog = KuteTextPreferenceEditDialog(this, minLength, maxLength, regex, isPassword)
        dialog
                .show(context)
    }

    override fun createDescription(currentValue: String): String {
        return if (isPassword) {
            "*".repeat(currentValue.length)
        } else {
            super.createDescription(currentValue)
        }
    }

}