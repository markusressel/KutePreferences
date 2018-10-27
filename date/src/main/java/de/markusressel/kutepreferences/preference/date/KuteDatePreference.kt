package de.markusressel.kutepreferences.preference.date

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase
import java.text.DateFormat
import java.util.*

open class KuteDatePreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Long? = null,
        private val maximum: Long? = null,
        private val defaultValue: Long,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG)

    override fun getDefaultValue(): Long = defaultValue

    override fun onClick(context: Context) {
        val dialog = KuteDatePreferenceEditDialog(this, minimum, maximum)
        dialog
                .show(context)
    }

    override fun createDescription(currentValue: Long): String {
        return dateFormatter.format(Date(currentValue))
    }

}