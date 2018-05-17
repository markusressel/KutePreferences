package de.markusressel.kutepreferences.library.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

open class KuteSingleSelectPreference(
        val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        @StringRes
        val defaultValue: Int,
        private val possibleValues: Map<Int, Int>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceBase<String>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): String = context.getString(defaultValue)

    override fun onClick(context: Context) {
        val dialog = KuteSingleSelectPreferenceEditDialog(this, possibleValues)
        dialog
                .show(context)
    }

}