package de.markusressel.kutepreferences.core.preference.section

import android.content.Context
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.R
import de.markusressel.kutepreferences.core.view.KutePreferencesMainFragment

/**
 * The default implementation of a KutePreference Section
 */
open class KuteSection(
        override val key: Int,
        override val title: String,
        override val children: List<KutePreferenceListItem>) : KutePreferenceSection {

    lateinit var nameView: TextView
    lateinit var preferenceItemList: LinearLayout

    override fun inflateListLayout(layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__section, parent, false) as ViewGroup

        nameView = layout.findViewById(R.id.kute_preference_section__title)
        preferenceItemList = layout.findViewById(R.id.kute_preference_section__item_list)

        nameView.text = title

        inflatePreferenceItems(layoutInflater, preferenceItemList)

        return layout
    }

    private fun inflatePreferenceItems(layoutInflater: LayoutInflater, preferenceItemList: LinearLayout) {
        for (child in children) {
            KutePreferencesMainFragment.inflateAndAttachClickListeners(layoutInflater, child, preferenceItemList)
        }
    }

    override fun onClick(context: Context) {
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title)
    }

    override fun highlightSearchMatches(highlighter: (String) -> Spanned) {
        nameView.text = highlighter(title)
    }

}
