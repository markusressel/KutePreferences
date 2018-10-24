package de.markusressel.kutepreferences.core.preference.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.markusressel.kutepreferences.core.R

/**
 * The default implementation of a KutePreference Divider
 */
open class KuteDivider(
        override val key: Int,
        override val title: String) : KutePreferenceDivider {

    override fun inflateListLayout(layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__divider, parent, false) as ViewGroup

        val nameView: TextView = layout
                .findViewById(R.id.kute_preference_divider__title)
        nameView
                .text = title

        return layout
    }

    override fun onClick(context: Context) {
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title)
    }
}
