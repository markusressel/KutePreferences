package de.markusressel.kutepreferences.preference.number.slider

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.IconHelper
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel

open class KuteSliderPreference(
        private val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        private val minimum: Int,
        private val maximum: Int,
        private val defaultValue: Int,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceItem<Int>, KutePreferenceListItem {

    override fun onListItemClicked(context: Context) {
        KuteSliderPreferenceEditDialog(this, minimum, maximum).show(context)
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = DefaultItemViewModel()
        viewModel.title.value = title
        viewModel.description.value = description
        viewModel.icon.value = IconHelper.getListItemIcon(context, icon)
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }
        viewModel.onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }

        return KutePreferenceDefaultListItemBindingModel_().viewModel(viewModel)
    }

    override fun getDefaultValue(): Int = defaultValue

}