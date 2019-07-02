package de.markusressel.kutepreferences.preference.color

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

/**
 * Preference item for a 4 channel color (rgba).
 * The color is stored as an integer.
 */
open class KuteColorPreference(
        private val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        @ColorRes
        private val defaultValue: Int,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceItem<Int>, KutePreferenceListItem {

    override fun getSearchableItems(): Set<String> = setOf(title, description)

    override fun getDefaultValue(): Int = ContextCompat.getColor(context, defaultValue)

    override fun createDescription(currentValue: Int): String {
        val a = Color.alpha(currentValue).toString(16).padStart(2, '0')
        val r = Color.red(currentValue).toString(16).padStart(2, '0')
        val g = Color.green(currentValue).toString(16).padStart(2, '0')
        val b = Color.blue(currentValue).toString(16).padStart(2, '0')

        return "#$a$r$g$b"
    }

    override fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*> {
        val dataModel = ColorPreferenceDataModel(
                title = highlighterFunction.invoke(title),
                description = highlighterFunction.invoke(description),
                icon = icon,
                color = persistedValue,
                onClick = View.OnClickListener {
                    KuteColorPreferenceEditDialog(this).show(context)
                },
                onLongClick = View.OnLongClickListener { false }
        )

        return KutePreferenceColorListItemBindingModel_().viewModel(dataModel)
    }

}
