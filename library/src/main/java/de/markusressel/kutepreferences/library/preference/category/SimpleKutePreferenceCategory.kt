package de.markusressel.kutepreferences.library.preference.category

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R

/**
 * The default implementation of a KutePreferenceCategory
 */
class SimpleKutePreferenceCategory(override val key: Int, val icon: Drawable, override val name: String, override val description: String, private val childPreferences: List<KutePreferenceListItem>) : KutePreferenceCategory {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__category, null, false) as ViewGroup

        val iconView: ImageView = layout
                .findViewById(R.id.kute_preference__category__icon)
        iconView
                .setImageDrawable(icon)

        val nameView: TextView = layout
                .findViewById(R.id.kute_preference__category__name)
        nameView
                .text = name

        val descriptionView: TextView = layout
                .findViewById(R.id.kute_preference__category__description)
        descriptionView
                .text = description

        return layout
    }

    override fun getChildren(): List<KutePreferenceListItem> {
        return childPreferences
    }


    override fun onClick(context: Context) {
        Toast
                .makeText(context, "$name clicked!", Toast.LENGTH_SHORT)
                .show()
    }

}
