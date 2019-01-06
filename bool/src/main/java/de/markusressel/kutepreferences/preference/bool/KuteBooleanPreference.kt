package de.markusressel.kutepreferences.preference.bool

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

/**
 * Implementation of a boolean preference
 */
open class KuteBooleanPreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        val descriptionFunction: ((Boolean) -> String)? = null,
        private val defaultValue: Boolean,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null) :
        KutePreferenceItem<Boolean>, KutePreferenceListItem {
    override fun getDefaultValue(): Boolean = defaultValue

    override fun createDescription(currentValue: Boolean): String {
        descriptionFunction?.let {
            return it(currentValue)
        }

        // if no specific description is given
        // there is no additional value in a "true" or "false" description
        // since it is already visible on the toggle
        return ""
    }

//    fun createListView(context: Context): KutePreferenceListItem {
//         TODO: extract everything view related to the BooleanPreferenceListView class
//        return BooleanPreferenceListView(1, context, this)
//    }

    override fun onListItemClicked(context: Context) {
        // TODO: switch checkbox state
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = BooleanPreferenceViewModel()
        viewModel.name.value = title
        viewModel.description.value = description
        viewModel.checked.value = persistedValue
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }
        viewModel.onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }

        return KutePreferenceBooleanListItemBindingModel_().viewModel(viewModel)
    }

}
