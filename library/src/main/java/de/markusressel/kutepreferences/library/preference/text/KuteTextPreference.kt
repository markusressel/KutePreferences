package de.markusressel.kutepreferences.library.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteTextPreference(override val key: Int,
                         override val icon: Drawable? = null,
                         override val title: String,
                         private val minLength: Int? = null,
                         private val maxLength: Int? = null,
                         private val regex: String? = null,
                         override val defaultValue: String,
                         override val dataProvider: KutePreferenceDataProvider,
                         override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceBase<String>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__text__list_item

    override fun onClick(context: Context) {
        val dialog = KuteTextPreferenceEditDialog(this, minLength, maxLength, regex)
        dialog
                .show(context)
    }

}