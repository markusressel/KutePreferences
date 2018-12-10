package de.markusressel.kutepreferences.core.preference.category

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.markusressel.kutepreferences.core.HighlighterFunction
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

    lateinit var iconView: ImageView
    lateinit var nameView: TextView
    lateinit var descriptionView: TextView

    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__category, parent, false) as ViewGroup

        iconView = layout.findViewById(R.id.kute_preference__category__icon)
        nameView = layout.findViewById(R.id.kute_preference__category__title)

        iconView.setImageDrawable(icon)
        nameView.text = title

        descriptionView = layout.findViewById(R.id.kute_preference__category__description)
        descriptionView.text = description

        return layout
    }

    override fun onClick(context: Context) {
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        nameView.text = highlighter(title)
        descriptionView.text = highlighter(description)
    }

}
