package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Implementation for a text preference
 */
open class KuteTextPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        protected val regex: String? = null,
        private val defaultValue: String,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceItem<String>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun getDefaultValue(): String = defaultValue

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                onClick = { v ->
                    onClick(v!!.context!!)
                },
                onLongClick = { false }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

    open fun onClick(context: Context) {
        KuteTextPreferenceEditDialog(this, regex).show(context)
    }

}