package de.markusressel.kutepreferences.core.preference.action

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceActionListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchable
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

open class KuteAction(
        private val context: Context,
        override val key: Int,
        val icon: Drawable? = null,
        val title: String,
        val description: String,
        val onClickAction: (Context, KuteAction) -> Unit) :
        KutePreferenceListItem, KuteSearchable {

    override fun createEpoxyModel(): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = title,
                description = description,
                icon = icon,
                onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) },
                onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }
        )

        return KutePreferenceActionListItemBindingModel_().viewModel(dataModel)
    }

    override fun onListItemClicked(context: Context) = onClickAction(context, this)

    override fun getSearchableItems(): Set<String> = setOf(title)

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
    }

}