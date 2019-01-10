package de.markusressel.kutepreferences.core.preference.category

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.eightbitlab.rxbus.Bus
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.event.CategoryClickedEvent
import de.markusressel.kutepreferences.core.viewmodel.DefaultItemViewModel

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

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = DefaultItemViewModel()
        viewModel.title.value = title
        viewModel.description.value = description
        viewModel.icon.value = icon
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }
        viewModel.onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }

        return KutePreferenceDefaultListItemBindingModel_().viewModel(viewModel)
    }

//    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
//        val layout = layoutInflater.inflate(R.layout.kute_preference__category, parent, false) as ViewGroup
//
//        iconView = layout.findViewById(R.id.kute_preference__category__icon)
//        nameView = layout.findViewById(R.id.kute_preference__category__title)
//
//        iconView.setImageDrawable(icon)
//        nameView.text = title
//
//        descriptionView = layout.findViewById(R.id.kute_preference__category__description)
//        descriptionView.text = description
//
//        return layout
//    }

    override fun onListItemClicked(context: Context) {
        Bus.send(CategoryClickedEvent(this))
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
    }

}
