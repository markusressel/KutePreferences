package de.markusressel.kutepreferences.demo.preferences.custom

import android.graphics.Color
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.demo.CustomPreferenceItemBindingModel_

class CustomPreferenceItem(
        @StringRes
        override val key: Int
) : KutePreferenceListItem {

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val viewModel = CustomDataModel(
                backgroundColor = Color.BLUE
        )
        return CustomPreferenceItemBindingModel_().viewModel(viewModel)
    }

    override fun getSearchableItems(): Set<String> {
        return emptySet()
    }

}