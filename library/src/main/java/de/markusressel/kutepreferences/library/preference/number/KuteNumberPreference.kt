package de.markusressel.kutepreferences.library.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteNumberPreference(override val key: Int,
                           override val icon: Drawable? = null,
                           override val title: String,
                           val mininum: Int? = null,
                           val maximum: Int? = null,
                           override val defaultValue: Long,
                           override val dataProvider: KutePreferenceDataProvider,
                           override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__number__list_item

    override fun onClick(context: Context) {
        val dialog = KuteNumberPreferenceEditDialog(this)
        dialog
                .show(context)
    }

}