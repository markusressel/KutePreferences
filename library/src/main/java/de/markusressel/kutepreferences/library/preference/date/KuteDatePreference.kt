package de.markusressel.kutepreferences.library.preference.date

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import java.text.DateFormat
import java.util.*

open class KuteDatePreference(override val key: Int,
                              override val icon: Drawable? = null,
                              override val title: String,
                              private val mininum: Long? = null,
                              private val maximum: Long? = null,
                              override val defaultValue: Long,
                              override val dataProvider: KutePreferenceDataProvider,
                              override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG)

    override fun onClick(context: Context) {
        val dialog = KuteDatePreferenceEditDialog(this, mininum, maximum)
        dialog
                .show(context)
    }

    override fun constructDescription(currentValue: Long): String {
        return dateFormatter.format(Date(currentValue))
    }

}