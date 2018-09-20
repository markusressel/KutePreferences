package de.markusressel.kutepreferences.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase
import de.markusressel.kutepreferences.preference.R

open class KuteSliderPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Int? = null,
        private val maximum: Int? = null,
        private val defaultValue: Int,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceBase<Int>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): Int = defaultValue

    override fun onClick(context: Context) {
        val dialog = KuteSliderPreferenceEditDialog(this, minimum, maximum)
        dialog
                .show(context)
    }

}