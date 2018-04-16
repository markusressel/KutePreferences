package de.markusressel.kutepreferences.library.preference.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R

/**
 * The default implementation of a KutePreferenceCategory
 */
class SimpleKutePreferenceCategory(
        override val name: String,
        override val description: String,
        private val childPreferences: List<KutePreferenceListItem>)
    : KutePreferenceCategory {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__category, null, false) as ViewGroup

        val nameView: TextView = layout.findViewById(R.id.kute_preference_category__name)
        nameView.text = name

        val descriptionView: TextView = layout.findViewById(R.id.kute_preference_category__description)
        descriptionView.text = description

        return layout
    }


    override fun getChildren(): List<KutePreferenceListItem> {
        return childPreferences
    }

}
