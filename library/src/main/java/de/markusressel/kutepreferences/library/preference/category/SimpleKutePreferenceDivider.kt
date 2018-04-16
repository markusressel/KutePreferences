package de.markusressel.kutepreferences.library.preference.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.markusressel.kutepreferences.library.R

/**
 * The default implementation of a KutePreferenceCategory
 */
class SimpleKutePreferenceDivider(override val name: String)
    : KutePreferenceDivider {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__divider, null, false) as ViewGroup

        val nameView: TextView = layout.findViewById(R.id.kute_preference_divider__name)
        nameView.text = name

        return layout
    }

}
