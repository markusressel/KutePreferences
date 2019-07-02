package de.markusressel.kutepreferences.core.preference.action

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

open class KuteAction(
        override val key: Int,
        val icon: Drawable? = null,
        val title: String,
        val description: String,
        val onClickAction: (Context, KuteAction) -> Unit) :
        KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                onClick = View.OnClickListener { v ->
                    onClickAction(v!!.context!!, this)
                },
                onLongClick = View.OnLongClickListener { false }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

}