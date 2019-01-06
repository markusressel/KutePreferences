package de.markusressel.kutepreferences.preference.selection.single

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialog
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> : KutePreferenceItem<T>, KutePreferenceListItem {
    abstract val context: Context
    abstract val possibleValues: Map<Int, T>

    override fun onListItemClicked(context: Context) {
        createSingleSelectDialog(this, possibleValues).show(context)
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = DefaultItemViewModel()
        viewModel.name.value = title
        viewModel.description.value = description
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }
        viewModel.onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }

        return KutePreferenceDefaultListItemBindingModel_().viewModel(viewModel)
    }

    /**
     * Creates an instance of an edit dialog for this preference item
     *
     * @return the dialog instance
     */
    internal abstract fun createSingleSelectDialog(kuteSingleSelectPreference: KuteSingleSelectPreference<T>,
                                                   possibleValues: Map<Int, T>): KutePreferenceEditDialog<T>

}