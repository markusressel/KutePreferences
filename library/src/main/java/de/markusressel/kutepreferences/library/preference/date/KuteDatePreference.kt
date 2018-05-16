package de.markusressel.kutepreferences.library.preference.date

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteDatePreference(override val key: Int,
                         override val icon: Drawable? = null,
                         override val title: String,
                         val mininum: Long? = null,
                         val maximum: Long? = null,
                         override val defaultValue: Long,
                         override val dataProvider: KutePreferenceDataProvider,
                         override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__date__list_item

    override fun onClick(context: Context) {
        val dialog = KuteDatePreferenceEditDialog(this)
        dialog
                .show(context)
    }

}