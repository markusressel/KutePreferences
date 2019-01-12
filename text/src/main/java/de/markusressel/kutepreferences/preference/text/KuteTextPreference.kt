package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
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
        protected val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        protected val regex: String? = null,
        private val defaultValue: String,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KutePreferenceItem<String>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        // TODO
    }

    override fun getDefaultValue(): String = defaultValue

    override fun onListItemClicked(context: Context) {
        KuteTextPreferenceEditDialog(this, regex).show(context)
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = title,
                description = description,
                icon = icon,
                onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) },
                onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

}