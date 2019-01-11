package de.markusressel.kutepreferences.core.preference.section

import android.content.Context
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KutePreferenceSectionListItemBindingModel_
import de.markusressel.kutepreferences.core.viewmodel.SectionViewModel

/**
 * The default implementation of a KutePreference Section
 */
open class KuteSection(
        override val key: Int,
        override val title: String,
        override val children: List<KutePreferenceListItem>) : KutePreferenceSection {

    lateinit var nameView: TextView

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = SectionViewModel()
        viewModel.title.value = title

        return KutePreferenceSectionListItemBindingModel_().viewModel(viewModel)
    }

    override fun onListItemClicked(context: Context) {
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        nameView.text = highlighter(title)
    }

}
