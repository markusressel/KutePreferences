package de.markusressel.kutepreferences.library.preference.action

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

open class KuteAction(override val key: Int,
                      val icon: Drawable? = null,
                      val title: String,
                      val onClickAction: (Context) -> Unit) :
        KutePreferenceListItem, KutePreferenceClickListener {

    val layoutRes: Int
        get() = R.layout.kute_preference__action__list_item

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, null, false) as ViewGroup

        val iconView: ImageView = layout
                .findViewById(R.id.kute_preference__preference__icon)
        if (icon != null) {
            iconView.setImageDrawable(icon)
        } else {
            iconView.setImageResource(R.drawable.ic_settings_black_24dp)
            iconView.alpha = 0.5F
        }

        val nameTextView: TextView = layout
                .findViewById(R.id.kute_preferences__preference__title)
        nameTextView.text = title

        return layout
    }

    override fun onClick(context: Context) {
        onClickAction(context)
    }

}