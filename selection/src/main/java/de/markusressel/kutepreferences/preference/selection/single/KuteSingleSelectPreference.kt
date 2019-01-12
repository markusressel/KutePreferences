package de.markusressel.kutepreferences.preference.selection.single

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialog
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> : KutePreferenceItem<T>, KutePreferenceListItem {
    abstract val context: Context
    abstract val possibleValues: Map<Int, T>

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        // TODO
    }

    override fun onListItemClicked(context: Context) {
        createSingleSelectDialog(this, possibleValues).show(context)
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

    /**
     * Creates an instance of an edit dialog for this preference item
     *
     * @return the dialog instance
     */
    internal abstract fun createSingleSelectDialog(kuteSingleSelectPreference: KuteSingleSelectPreference<T>,
                                                   possibleValues: Map<Int, T>): KutePreferenceEditDialog<T>

}