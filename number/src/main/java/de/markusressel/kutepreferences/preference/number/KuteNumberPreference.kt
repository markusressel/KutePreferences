package de.markusressel.kutepreferences.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase
import de.markusressel.kutepreferences.preference.R

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
        KutePreferenceBase<Long>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): Long = defaultValue

    override fun createDescription(currentValue: Long): String {
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