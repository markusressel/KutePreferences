package de.markusressel.kutepreferences.preference.date

import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel
import java.text.DateFormat
import java.util.*

open class KuteDatePreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        val minimum: Long? = null,
        val maximum: Long? = null,
        private val defaultValue: Long,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceItem<Long>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun getDefaultValue(): Long = defaultValue

    override fun createDescription(currentValue: Long): String {
        return dateFormatter.format(Date(currentValue))
    }

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                onClick = { v ->
                    KuteDatePreferenceEditDialog(this, minimum = minimum, maximum = maximum).show(v.context)
                },
                onLongClick = { false }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

    companion object {
        private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG)
    }

}