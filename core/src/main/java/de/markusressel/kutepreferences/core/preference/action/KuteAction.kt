package de.markusressel.kutepreferences.core.preference.action

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceActionListItemBindingModel_
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchable
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener

open class KuteAction(override val key: Int,
                      val icon: Drawable? = null,
                      val title: String,
                      val description: String,
                      val onClickAction: (Context, KuteAction) -> Unit) :
        KutePreferenceListItem, KutePreferenceClickListener, KuteSearchable {

    override fun createEpoxyModel(): EpoxyModel<*> {
        val viewModel = ActionViewModel()
        viewModel.title.value = title
        viewModel.description.value = description
        // TODO: default icon?
        viewModel.icon.value = icon
        viewModel.onClick = View.OnClickListener { v -> onListItemClicked(v!!.context!!) }
        viewModel.onLongClick = View.OnLongClickListener { v -> onListItemLongClicked(v!!.context!!) }

        return KutePreferenceActionListItemBindingModel_().viewModel(viewModel)
    }

    lateinit var nameTextView: TextView

//    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
//        val layout = layoutInflater.inflate(layoutRes, parent, false) as ViewGroup
//
//        iconView = layout.findViewById(R.id.kute_preference__preference__icon)
//        if (icon != null) {
//            iconView.setImageDrawable(icon)
//        } else {
//            iconView.setImageResource(R.drawable.ic_settings_black_24dp)
//            iconView.alpha = 0.5F
//        }
//
//        return layout
//    }

    override fun onListItemClicked(context: Context) = onClickAction(context, this)

    override fun getSearchableItems(): Set<String> = setOf(title)

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        nameTextView.text = highlighter(title)
    }

}