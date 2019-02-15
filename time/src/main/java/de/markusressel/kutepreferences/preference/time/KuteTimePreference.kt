package de.markusressel.kutepreferences.preference.time

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

open class KuteTimePreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val defaultValue: TimePersistenceModel,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: TimePersistenceModel, newValue: TimePersistenceModel) -> Unit)? = null) :
        KutePreferenceItem<TimePersistenceModel>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun getDefaultValue(): TimePersistenceModel = defaultValue

    override fun onListItemClicked(context: Context) {
        KuteTimePreferenceEditDialog(this).show(context)
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

    override fun createDescription(currentValue: TimePersistenceModel): String {
        return "${currentValue.hourOfDay}:${currentValue.minute}"
    }

}