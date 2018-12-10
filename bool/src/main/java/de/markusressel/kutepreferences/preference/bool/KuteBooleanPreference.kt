package de.markusressel.kutepreferences.preference.bool

import android.graphics.drawable.Drawable
import com.airbnb.epoxy.EpoxyModel
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
        KutePreferenceItem<Boolean> {
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

    override fun getEpoxyModel(): EpoxyModel<*> {
        val viewModel = BooleanPreferenceViewModel()
        viewModel.name.value = title
        viewModel.description.value = description
        viewModel.checked.value = persistedValue

        return KutePreferenceBooleanListItemBindingModel_().viewModel(viewModel)
    }

}
