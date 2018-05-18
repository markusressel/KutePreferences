package de.markusressel.kutepreferences.library.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

open class KuteMultiSelectPreference(
        val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        @StringRes
        private val defaultValue: Set<Int>,
        private val possibleValues: Map<Int, Int>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Set<String>, newValue: Set<String>) -> Unit)? = null) :
        KutePreferenceBase<Set<String>>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): Set<String> = defaultValue.map { context.getString(it) }.toSet()

    override fun onClick(context: Context) {
        val dialog = KuteMultiSelectPreferenceEditDialog(this, possibleValues)
        dialog
                .show(context)
    }

    override fun createDescription(currentValue: Set<String>): String {
        return possibleValues.filter {
            context.getString(it.key) in currentValue
        }.map {
            context.getString(it.value)
        }.joinToString(separator = ", ")
    }

}