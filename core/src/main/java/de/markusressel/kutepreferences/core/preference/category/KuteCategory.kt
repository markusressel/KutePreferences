package de.markusressel.kutepreferences.core.preference.category

import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyModel
import com.eightbitlab.rxbus.Bus
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceCategoryListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.event.CategoryClickedEvent
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * The default implementation of a KutePreference Category
 */
open class KuteCategory(
        override val key: Int,
        private val icon: Drawable,
        override val title: String,
        override val description: String,
        override val children: List<KutePreferenceListItem>) :
        KutePreferenceCategory {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                onClick = { v ->
                    Bus.send(CategoryClickedEvent(this))
                },
                onLongClick = { false }
        )

        return KutePreferenceCategoryListItemBindingModel_().viewModel(dataModel)
    }

}
