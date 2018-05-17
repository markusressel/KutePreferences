package de.markusressel.kutepreferences.library.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

open class KuteNumberPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Int? = null,
        private val maximum: Int? = null,
        override val defaultValue: Long,
        val unit: String? = null, override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun constructDescription(currentValue: Long): String {
        unit?.let {
            return "$currentValue $it"
        }

        return "$currentValue"
    }

    override fun onClick(context: Context) {
        val dialog = KuteNumberPreferenceEditDialog(this)
        dialog
                .show(context)
    }

}