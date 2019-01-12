package de.markusressel.kutepreferences.preference.number.range

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceDefaultListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Base class for preferences defining some kind of range
 */
abstract class KuteRangePreference<T : Number>(
        protected val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        protected val minimum: T,
        protected val maximum: T,
        protected val decimalPlaces: Int,
        private val defaultValue: RangePersistenceModel<T>,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: RangePersistenceModel<T>, newValue: RangePersistenceModel<T>) -> Unit)? = null) :
        KutePreferenceItem<RangePersistenceModel<T>>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        // TODO
    }

    override fun getDefaultValue(): RangePersistenceModel<T> = defaultValue

    override fun createDescription(currentValue: RangePersistenceModel<T>): String {
        val start = formatNumberForDescription(currentValue.min, decimalPlaces)
        val end = formatNumberForDescription(currentValue.max, decimalPlaces)

        return "$start .. $end"
    }

    protected open fun formatNumberForDescription(number: T, decimalPlacesWhenNotWhole: Int): String {
        val decimalPlaces = if (number == Math.floor(number.toDouble())) {
            // number is a whole number
            0
        } else {
            decimalPlacesWhenNotWhole
        }

        return "%.${decimalPlaces}f".format(number.toFloat())
    }

    override fun createEpoxyModel(): EpoxyModel<*> {
        val dataModel = PreferenceItemDataModel(
                title = title,
                description = description,
                icon = icon,
                onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) },
                onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }
        )

        return KutePreferenceDefaultListItemBindingModel_().viewModel(dataModel)
    }

}