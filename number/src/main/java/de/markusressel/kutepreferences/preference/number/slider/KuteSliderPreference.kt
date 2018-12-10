package de.markusressel.kutepreferences.preference.number.slider

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

open class KuteSliderPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Int,
        private val maximum: Int,
        private val defaultValue: Int,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceItem<Int> {

    override fun getDefaultValue(): Int = defaultValue

}