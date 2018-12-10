package de.markusressel.kutepreferences.preference.selection.multi

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel

/**
 * Implementation of a multi selection preference
 */
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
        KutePreferenceItem<Set<String>> {

    override fun getDefaultValue(): Set<String> = defaultValue.asSequence().map { context.getString(it) }.toSet()

    override fun createDescription(currentValue: Set<String>): String {
        return possibleValues.filter {
            context.getString(it.key) in currentValue
        }.map {
            context.getString(it.value)
        }.joinToString(separator = ", ")
    }

}