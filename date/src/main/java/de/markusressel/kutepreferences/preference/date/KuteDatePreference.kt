package de.markusressel.kutepreferences.preference.date

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel
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

    override fun getDefaultValue(): Long = defaultValue

    override fun createDescription(currentValue: Long): String {
        return dateFormatter.format(Date(currentValue))
    }

    override fun onListItemClicked(context: Context) {
        KuteDatePreferenceEditDialog(this, minimum = minimum, maximum = maximum).show(context)
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = DefaultItemViewModel()
        viewModel.name.value = title
        viewModel.description.value = description
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }

        return KutePreferenceDefaultListItemBindingModel_().viewModel(viewModel)
    }

    companion object {
        private val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG)
    }

}