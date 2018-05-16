package de.markusressel.kutepreferences.library.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteSliderPreference(override val key: Int,
                           override val icon: Drawable? = null,
                           override val title: String,
                           private val mininum: Int? = null,
                           private val maximum: Int? = null,
                           override val defaultValue: Int,
                           override val dataProvider: KutePreferenceDataProvider,
                           override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceBase<Int>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun onClick(context: Context) {
        val dialog = KuteSliderPreferenceEditDialog(this, defaultValue, mininum, maximum)
        dialog
                .show(context)
    }

}