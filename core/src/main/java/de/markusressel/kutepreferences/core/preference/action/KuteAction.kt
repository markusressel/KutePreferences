package de.markusressel.kutepreferences.core.preference.action

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
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.R
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener

open class KuteAction(override val key: Int,
                      val icon: Drawable? = null,
                      val title: String,
                      val onClickAction: (Context, KuteAction) -> Unit) :
        KutePreferenceListItem, KutePreferenceClickListener, KuteSearchProvider {

    val layoutRes: Int
        get() = R.layout.kute_preference__action__list_item

    lateinit var iconView: ImageView
    lateinit var nameTextView: TextView

    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, parent, false) as ViewGroup

        iconView = layout.findViewById(R.id.kute_preference__preference__icon)
        if (icon != null) {
            iconView.setImageDrawable(icon)
        } else {
            iconView.setImageResource(R.drawable.ic_settings_black_24dp)
            iconView.alpha = 0.5F
        }

        nameTextView = layout.findViewById(R.id.kute_preferences__preference__title)
        nameTextView.text = title

        return layout
    }

    override fun onClick(context: Context) {
        onClickAction(context, this)
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        nameTextView.text = highlighter(title)
    }

}