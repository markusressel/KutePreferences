package de.markusressel.kutepreferences.core.preference.section

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.eightbitlab.rxbus.Bus
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KutePreferenceSectionListItemBindingModel_
import de.markusressel.kutepreferences.core.event.SectionClickedEvent
import de.markusressel.kutepreferences.core.viewmodel.SectionViewModel

/**
 * The default implementation of a KutePreference Section
 */
open class KuteSection(
        override val key: Int,
        override val title: String,
        override val children: List<KutePreferenceListItem>) : KutePreferenceSection {

    override fun getSearchableItems(): Set<String> = setOf(title)

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val viewModel = SectionViewModel(
                title = highlighterFunction.invoke(title),
                onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) },
                onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) })

        return KutePreferenceSectionListItemBindingModel_().viewModel(viewModel)
    }

    override fun onListItemClicked(context: Context) {
        Bus.send(SectionClickedEvent(this))
    }

}
