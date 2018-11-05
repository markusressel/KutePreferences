package de.markusressel.kutepreferences.core.preference.category

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.R

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

    override fun inflateListLayout(layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__category, parent, false) as ViewGroup

        val iconView: ImageView = layout
                .findViewById(R.id.kute_preference__category__icon)
        iconView
                .setImageDrawable(icon)

        val nameView: TextView = layout
                .findViewById(R.id.kute_preference__category__title)
        nameView
                .text = title

        val descriptionView: TextView = layout
                .findViewById(R.id.kute_preference__category__description)
        descriptionView
                .text = description

        return layout
    }

    override fun onClick(context: Context) {
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMathes(regex: String) {
    }

}
