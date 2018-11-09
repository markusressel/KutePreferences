package de.markusressel.kutepreferences.core.view

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.backgroundColor
import androidx.lifecycle.Lifecycle
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import de.markusressel.commons.android.themes.getThemeAttrColor
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.R
import de.markusressel.kutepreferences.core.event.CategoryClickedEvent
import de.markusressel.kutepreferences.core.event.SectionClickedEvent
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.core.preference.section.KutePreferenceSection
import de.markusressel.kutepreferences.core.tree.TreeManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.kute_preference__main_fragment.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers is managed in here.
 */
abstract class KutePreferencesMainFragment : StateFragmentBase() {

    internal lateinit var treeManager: TreeManager

    /**
     * Stack of previously visible preference items, including the current ones
     */
    internal var backstack: Stack<BackstackItem> by savedInstanceState(Stack())

    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater
                .inflate(R.layout.kute_preference__main_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater
                ?.inflate(R.menu.kutepreferences__menu, menu)

        searchMenuItem = menu?.findItem(R.id.search)!!
        searchMenuItem.icon = ContextCompat.getDrawable(context as Context, R.drawable.ic_search_24px)

        searchView = searchMenuItem.actionView as SearchView
        searchView.let {
            RxSearchView
                    .queryTextChanges(it)
                    .skipInitialValue()
                    .bindUntilEvent(this, Lifecycle.Event.ON_DESTROY)
                    .debounce(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onNext = {
                        if (it.isBlank()) {
                            showPreferenceItems(backstack.peek())
                        } else {
                            val searchString = it.toString()
                            val preferenceIds = treeManager
                                    .findInSearchProviders(searchString)
                                    .map {
                                        it.key
                                    }
                            showPreferenceItems(preferenceIds, false, searchString = searchString)
                        }
                    }, onError = {
                        Log.e(TAG, "Error filtering list", it)
                    })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        treeManager = TreeManager(*initPreferenceTree())

        when {
            backstack.isNotEmpty() -> showPreferenceItems(backstack.peek())
            else -> showTopLevel()
        }
    }

    override fun onStart() {
        super.onStart()

        Bus
                .observe<CategoryClickedEvent>()
                .subscribe {
                    showCategory(it.category)
                }
                .registerInBus(this)

        Bus
                .observe<SectionClickedEvent>()
                .subscribe {
                    showCategory(it.section)
                }
                .registerInBus(this)
    }

    override fun onStop() {
        super.onStop()

        Bus.unregister(this)
    }

    fun showTopLevel() {
        showPreferenceItems(treeManager.getTopLevelItems().map {
            it.key
        },
                // small workaround to allow searchView non-nullable type
                ignoreSearch = true)
    }

    /**
     * Show the containing category of a section
     *
     * @param section the section that has been clicked
     */
    fun showCategory(section: KutePreferenceSection) {
        val categoryItems: List<Int>? = treeManager.findParentCategory(section)
                ?.children
                ?.map {
                    it.key
                }

        // don't switch the category if the search bar is closed (iconified)
        if (searchView.isIconified) {
            return
        }

        clearSearch()
        if (categoryItems != null) {
            showPreferenceItems(categoryItems)
        } else {
            // if the section has no parent category it must be the a root section
            showTopLevel()
        }
    }

    /**
     * Show a specific category
     *
     * @param category the category to show
     */
    fun showCategory(category: KutePreferenceCategory) {
        val categoryItems = treeManager
                .getCategoryItems(category.key)
                .map {
                    it.key
                }

        clearSearch()
        showPreferenceItems(categoryItems)
    }

    private fun clearSearch() {
        searchView.apply {
            setQuery("", false)
            clearFocus()
            searchMenuItem.collapseActionView()
        }
    }

    internal fun showPreferenceItems(backstackItem: BackstackItem) {
        searchView.setQuery(backstackItem.searchText, false)
        showPreferenceItems(backstackItem.preferenceItemIds, false)
    }

    internal fun showPreferenceItems(preferenceIds: List<Int>, addToStack: Boolean = true, ignoreSearch: Boolean = false, searchString: String? = null) {
        if (addToStack) {
            val query = if (ignoreSearch) "" else searchView.query.toString()
            val newBackstackItem = BackstackItem(preferenceIds, query)
            addToBackstack(newBackstackItem)
        }

        val preferenceItems: List<KutePreferenceListItem> =
                treeManager
                        .findInTree {
                            it.key in preferenceIds
                        }.mapNotNull {
                            it.item
                        }

        generatePage(preferenceItems, searchString)
    }

    private fun addToBackstack(newBackstackItem: BackstackItem) {
        if (backstack.isEmpty()) {
            backstack.push(newBackstackItem)
        } else {
            // only push new backstack item if something about is has changed
            if (backstack.peek() != newBackstackItem) {
                backstack.push(newBackstackItem)
            }
        }
    }

    /**
     * Generates a ViewGroup for the given preferences
     */
    internal fun generatePage(kutePreference: List<KutePreferenceListItem>, searchString: String?) {
        // find the layout where list items should be inserted
        val listItemLayout: ViewGroup = kute_preferences__list_item_root
        listItemLayout
                .removeAllViews()

        val keySet: MutableSet<Int> = mutableSetOf()
        kutePreference
                .forEach {
                    // does NOT recurse through child items as we only want to inflate the current tree layer
                    inflate(it, listItemLayout, keySet, searchString)
                }
    }

    internal fun inflate(kutePreferenceListItem: KutePreferenceListItem, layoutToAppendTo: ViewGroup,
                         keySet: MutableSet<Int>,
                         searchString: String?) {
        when (kutePreferenceListItem) {
            is KutePreferenceItem<*> -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
            is KutePreferenceCategory -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
            is KutePreferenceSection -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
        }

        inflateAndAttachClickListeners(layoutInflater, kutePreferenceListItem, layoutToAppendTo, searchString)
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
        searchView.let {
            if (it.query.isNotEmpty()) {
                it.setQuery("", false)
                return true
            }
        }

        return when {
            backstack.size > 1 -> {
                backstack.pop()
                showPreferenceItems(backstack.peek())
                true
            }
            else -> false
        }
    }

    companion object {
        fun inflateAndAttachClickListeners(layoutInflater: LayoutInflater, preferenceItem: KutePreferenceListItem, parent: ViewGroup, searchString: String? = null) {
            val layout = preferenceItem.inflateListLayout(layoutInflater, parent)
            parent.addView(layout)

            if (searchString != null && preferenceItem is KuteSearchProvider) {
                highlightSearchMatches(layoutInflater.context, preferenceItem, searchString)
            }

            if (preferenceItem is KutePreferenceClickListener) {
                layout
                        .setOnClickListener {
                            preferenceItem
                                    .onClick(layoutInflater.context)

                            when (preferenceItem) {
                                is KutePreferenceCategory -> {
                                    Bus.send(CategoryClickedEvent(preferenceItem))
                                }
                                is KutePreferenceSection -> {
                                    Bus.send(SectionClickedEvent(preferenceItem))
                                }
                            }
                        }
            }
        }

        private fun highlightSearchMatches(context: Context, preferenceItem: KuteSearchProvider, searchString: String) {
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

        const val TAG: String = "MainFragment"
    }

}