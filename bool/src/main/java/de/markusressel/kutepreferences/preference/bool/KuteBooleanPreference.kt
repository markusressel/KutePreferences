package de.markusressel.kutepreferences.preference.bool

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
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

    override fun getSearchableItems(): Set<String> = setOf(title, description)

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

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = BooleanPreferenceDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                checked = persistedValue,
                onCheckedChange = CompoundButton.OnCheckedChangeListener { _, isChecked ->
                    persistedValue = isChecked
                },
                onClick = View.OnClickListener {
                    persistedValue = !persistedValue
                },
                onLongClick = View.OnLongClickListener { false }
        )

        return KutePreferenceBooleanListItemBindingModel_().viewModel(dataModel)
    }

}
