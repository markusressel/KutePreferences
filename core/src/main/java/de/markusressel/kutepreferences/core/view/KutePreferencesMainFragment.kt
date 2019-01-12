package de.markusressel.kutepreferences.core.view

import android.content.Context
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import androidx.annotation.CallSuper
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.backgroundColor
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.TypedEpoxyController
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import de.markusressel.commons.android.themes.getThemeAttrColor
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchable
import de.markusressel.kutepreferences.core.R
import de.markusressel.kutepreferences.core.databinding.KutePreferenceMainFragmentBinding
import de.markusressel.kutepreferences.core.event.CategoryClickedEvent
import de.markusressel.kutepreferences.core.event.PreferenceChangedEvent
import de.markusressel.kutepreferences.core.event.SectionClickedEvent
import de.markusressel.kutepreferences.core.extensions.children
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.viewmodel.MainFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.kute_preference__main_fragment.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers is managed in here.
 */
abstract class KutePreferencesMainFragment : LifecycleFragmentBase() {

    private val viewModel: MainFragmentViewModel by lazy { ViewModelProviders.of(activity!!).get(MainFragmentViewModel::class.java) }

    internal val epoxyController by lazy { createEpoxyController() }

    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: KutePreferenceMainFragmentBinding = DataBindingUtil.inflate(layoutInflater, R.layout.kute_preference__main_fragment, container, false)
        viewModel.currentPreferenceItems.observe(this, Observer {
            epoxyController.setData(it)
        })
        if (!viewModel.hasPreferenceTree()) {
            viewModel.setPreferenceTree(initPreferenceTree())
        }

        viewModel.currentSearchFilter.observe(this, Observer {
            // TODO: this has to be submitted at least somehow
            searchView?.setQuery(it, false)
        })
        viewModel.isSearchExpanded.observe(this, Observer { isExpanded ->
            if (!isExpanded) {
                searchView?.clearFocus()
                searchMenuItem?.collapseActionView()
            }
        })

        binding.let {
            it.setLifecycleOwner(this)
            it.viewModel = viewModel
        }

        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setController(epoxyController)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
    }

    private fun createEpoxyController(): TypedEpoxyController<List<KutePreferenceListItem>> {
        return object : TypedEpoxyController<List<KutePreferenceListItem>>() {
            override fun buildModels(data: List<KutePreferenceListItem>?) {
                data?.forEach {
                    createModel(it).addTo(this)
                    if (it is KuteSection) {
                        it.children.forEach { child ->
                            createModel(child).addTo(this)
                        }
                    }
                }
            }

            private fun createModel(kutePreferenceListItem: KutePreferenceListItem): EpoxyModel<*> {
                return kutePreferenceListItem.createEpoxyModel().apply {
                    id(kutePreferenceListItem.key)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.kutepreferences__menu, menu)

        searchMenuItem = menu?.findItem(R.id.search)
        searchMenuItem?.apply {
            icon = ContextCompat.getDrawable(context as Context, R.drawable.ic_search_24px)
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    val oldValue = viewModel.isSearchExpanded.value
                    if (oldValue == null || !oldValue) {
                        viewModel.isSearchExpanded.value = true
                    }
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    val oldValue = viewModel.isSearchExpanded.value
                    if (oldValue == null || oldValue) {
                        viewModel.isSearchExpanded.value = false
                    }
                    return true
                }
            })
        }

        searchView = searchMenuItem?.actionView as SearchView
        searchView?.let {
            RxSearchView
                    .queryTextChanges(it)
                    .skipInitialValue()
                    .bindUntilEvent(this, Lifecycle.Event.ON_DESTROY)
                    .debounce(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = { text ->
                        viewModel.setSearch(text.toString())
                    }, onError = { error ->
                        Log.e(TAG, "Error filtering list", error)
                    })
        }
    }

    override fun onStart() {
        super.onStart()

        Bus.observe<CategoryClickedEvent>()
                .subscribe {
                    viewModel.showCategory(it.category)
                }.registerInBus(this)

        Bus.observe<SectionClickedEvent>()
                .subscribe {
                    viewModel.showCategory(it.section)
                }.registerInBus(this)

        Bus.observe<PreferenceChangedEvent<*>>()
                .subscribe {
                    // force rebuilding of models
                    epoxyController.setData(epoxyController.currentData)
                }.registerInBus(this)
    }

    override fun onStop() {
        super.onStop()

        Bus.unregister(this)
    }

    private fun forceRippleAnimation(view: View) {
        val viewWithRippleDrawable = findViewWithRippleBackground(view)

        viewWithRippleDrawable?.let {
            it.postDelayed({
                it.background.apply {
                    setHotspot(it.width / 2F, it.height / 2F)
                    state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
                }

                val handler = Handler()
                handler.postDelayed({ it.background.state = intArrayOf() }, 5000)
            }, 1000)
        }
    }

    /**
     * Tries to find a view with a RippleBackground in the view hierarchy of the given view.
     * @param view the view to start searching
     */
    private fun findViewWithRippleBackground(view: View): View? {
        val visited = mutableListOf(view)
        val queue = LinkedBlockingQueue<View>()
        queue.put(view)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.background is RippleDrawable) {
                return current
            } else {
                if (current is ViewGroup) {
                    val notYetVisited = current.children.filter { it !in visited }
                    queue.addAll(notYetVisited)
                    visited.addAll(notYetVisited)
                }
            }
        }

        return null
    }

    private fun checkKeyDuplication(key: Int, keySet: MutableSet<Int>) {
        if (keySet.contains(key)) {
            Log
                    .w("KutePreferences",
                            "Duplicate key '$key' found! Did you accidentally add a KutePreference twice or reused an existing key?")
        } else {
            keySet
                    .add(key)
        }
    }

    /**
     * Initialize your preferences tree here
     */
    abstract fun initPreferenceTree(): Array<KutePreferenceListItem>

    /**
     * Call this from your activity's {@link AppCompatActivity.onBackPressed()} to ensure
     * back navigation behaviour is working es expected
     * @return true if a navigation happened (aka the back button event was consumed), false otherwise
     */
    open fun onBackPressed(): Boolean {
        return viewModel.navigateUp()
    }

    private fun highlightSearchMatches(context: Context, preferenceItem: KuteSearchable, searchString: String) {
        preferenceItem.highlightSearchMatches { text ->
            val regex = searchString.toRegex(RegexOption.IGNORE_CASE)

            val highlightedText = SpannableStringBuilder()

            var currentStartIndex = 0
            while (currentStartIndex < text.length) {
                val matchResult = regex.find(text, startIndex = currentStartIndex)

                if (matchResult != null) {
                    // append normal text
                    highlightedText.append(text.substring(currentStartIndex, matchResult.range.first))

                    val color = context.getThemeAttrColor(R.attr.kute_preferences__search__highlighted_text_color)
                    highlightedText.backgroundColor(color) {
                        append(text.substring(matchResult.range.first, matchResult.range.last + 1))
                    }

                    // set index for next iteration
                    currentStartIndex = matchResult.range.last + 1
                } else {
                    highlightedText.append(text.substring(currentStartIndex, text.length))
                    break
                }
            }

            highlightedText
        }

    }

    companion object {

        const val TAG: String = "MainFragment"
    }

}