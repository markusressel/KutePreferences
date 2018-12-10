package de.markusressel.kutepreferences.core.preference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.R
import de.markusressel.kutepreferences.core.viewmodel.PreferenceItemViewModel

/**
 * Base class for KutePreferenceListItem implementations using a ViewModel
 */
abstract class KutePreferenceViewBase<
        PreferenceType : KutePreferenceItem<*>,
        ViewModelClass : PreferenceItemViewModel>(
        protected val prefenceItem: PreferenceType) :
        KutePreferenceListItem,
        KutePreferenceClickListener,
        KutePreferenceLongClickListener,
        KuteSearchProvider {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected abstract val viewModelClass: Class<ViewModelClass>

    @CallSuper
    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val layout = inflateLayout(parentFragment, layoutInflater, parent)

        val s = layout.findViewById<Switch>(R.id.kute_preferences__preference__title)
        s.isChecked = false

        val viewModel = getViewModel()
        viewModel.name.value = prefenceItem.title
        viewModel.description.value = prefenceItem.description
        viewModel.icon.value = prefenceItem.icon
                ?: ContextCompat.getDrawable(parent.context, R.drawable.ic_settings_black_24dp)

        updateDescription()

        return layout
    }

    abstract fun getViewModel(): ViewModelClass

    abstract fun inflateLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): View

    override fun onClick(context: Context) {
    }

    override fun onLongClick(context: Context) {
    }

    open fun updateDescription() {
        getViewModel().description.value = prefenceItem.description
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(prefenceItem.title, prefenceItem.description)
    }

    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
        getViewModel().highlight(highlighter)
    }

}