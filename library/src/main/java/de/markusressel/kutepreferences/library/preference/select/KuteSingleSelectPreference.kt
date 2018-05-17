package de.markusressel.kutepreferences.library.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.number.KuteNumberPreferenceEditDialog

open class KuteSingleSelectPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        override val defaultValue: Long,
        private val possibleValues: Map<String, Int>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun onClick(context: Context) {
        val dialog = KuteNumberPreferenceEditDialog(this)
        dialog
                .show(context)
    }

}