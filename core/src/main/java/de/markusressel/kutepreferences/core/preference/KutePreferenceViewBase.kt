package de.markusressel.kutepreferences.core.preference

/**
 * Base class for KutePreferenceListItem implementations using a ViewModel
 */
//abstract class KutePreferenceViewBase<
//        PreferenceType : KutePreferenceItem<*>,
//        ViewModelClass : PreferenceItemViewModel>(
//        protected val prefenceItem: PreferenceType) :
//        KutePreferenceListItem,
//        KutePreferenceClickListener,
//        KutePreferenceLongClickListener,
//        KuteSearchable {
//
//    @get:LayoutRes
//    protected abstract val layoutRes: Int
//
//    protected abstract val viewModelClass: Class<ViewModelClass>
//
//    override fun createEpoxyModel(): EpoxyModel<*> {
//        val viewModel = getViewModel()
//
//        viewModel.title.value = prefenceItem.title
//        viewModel.description.value = prefenceItem.description
////        viewModel.icon.value = prefenceItem.icon
////                ?: ContextCompat.getDrawable(, R.drawable.ic_settings_black_24dp)
//
//        updateDescription()
//
//        return viewModel
//    }
//
//    @CallSuper
//    override fun inflateListLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): View {
//        val layout = inflateLayout(parentFragment, layoutInflater, parent)
//
//        return layout
//    }
//
//    abstract fun getViewModel(): ViewModelClass
//
//    abstract fun inflateLayout(parentFragment: Fragment, layoutInflater: LayoutInflater, parent: ViewGroup): View
//
//    override fun onListItemClicked(context: Context) {
//    }
//
//    override fun onListItemLongClicked(context: Context) {
//    }
//
//    open fun updateDescription() {
//        getViewModel().description.value = prefenceItem.description
//    }
//
//    override fun getSearchableItems(): Set<String> {
//        return setOf(prefenceItem.title, prefenceItem.description)
//    }
//
//    override fun highlightSearchMatches(highlighter: HighlighterFunction) {
//        getViewModel().highlight(highlighter)
//    }
//
//}