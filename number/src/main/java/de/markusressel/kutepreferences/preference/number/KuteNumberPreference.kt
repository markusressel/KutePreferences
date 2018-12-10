package de.markusressel.kutepreferences.preference.number

import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel

/**
 * Implementation of a Long preference for selecting a number
 */
open class KuteNumberPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Int? = null,
        private val maximum: Int? = null,
        private val defaultValue: Long,
        val unit: String? = null,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceItem<Long> {

    override fun getDefaultValue(): Long = defaultValue

    override fun createDescription(currentValue: Long): String {
        unit?.let {
            return "$currentValue $it"
        }

        return "$currentValue"
    }

}