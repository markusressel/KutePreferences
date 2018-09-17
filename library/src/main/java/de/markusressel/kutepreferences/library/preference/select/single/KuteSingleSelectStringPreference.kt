package de.markusressel.kutepreferences.library.preference.select.single

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialog

/**
 * Implementation of a single selection preference for selecting simple string objects
 */
open class KuteSingleSelectStringPreference(
        override val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        @StringRes
        val defaultValue: Int,
        possibleValues: Map<Int, Int>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KuteSingleSelectPreference<String>() {

    override val possibleValues = possibleValues.entries.associate { it.key to context.getString(it.value) }

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): String = context.getString(defaultValue)

    override fun createSingleSelectDialog(kuteSingleSelectPreference: KuteSingleSelectPreference<String>,
                                          possibleValues: Map<Int, String>): KutePreferenceEditDialog<String> {
        return KuteSingleSelectStringPreferenceEditDialog(
                this,
                possibleValues)
    }

    override fun createDescription(currentValue: String): String {
        return possibleValues.filter {
            context.getString(it.key) == currentValue
        }.map {
            it.value
        }.first()
    }

}

