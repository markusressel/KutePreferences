package de.markusressel.kutepreferences.library.preference.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import de.markusressel.kutepreferences.library.R

/**
 * The default implementation of a KutePreference Divider
 */
class KuteDivider(
        override val key: Int,
        override val title: String) : KutePreferenceDivider {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__divider, null, false) as ViewGroup

        val nameView: TextView = layout
                .findViewById(R.id.kute_preference_divider__title)
        nameView
                .text = title

        return layout
    }

    override fun onClick(context: Context) {
        Toast
                .makeText(context, "$title clicked!", Toast.LENGTH_SHORT)
                .show()
    }
}
