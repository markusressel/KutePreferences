package de.markusressel.kutepreferences.preference.selection.multi

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

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
        KutePreferenceItem<Set<String>>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun getDefaultValue(): Set<String> = defaultValue.asSequence().map { context.getString(it) }.toSet()

    override fun onListItemClicked(context: Context) {
        KuteMultiSelectPreferenceEditDialog(this, possibleValues).show(context)
    }

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) },
                onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

    override fun createDescription(currentValue: Set<String>): String {
        return possibleValues.filter {
            context.getString(it.key) in currentValue
        }.map {
            context.getString(it.value)
        }.joinToString(separator = ", ")
    }

}