package de.markusressel.kutepreferences.library.view

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory
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

    internal lateinit var kutePreferencesTree: KutePreferencesTree

    /**
     * Stack of previously visible preference items, including the current ones
     */
    internal var backstack: Stack<BackstackItem> by savedInstanceState(Stack())

    private var searchView: SearchView? = null

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

        val searchMenuItem = menu?.findItem(R.id.search)
        searchMenuItem?.icon = ContextCompat.getDrawable(context as Context, R.drawable.ic_search_24px)

        searchView = searchMenuItem?.actionView as SearchView?
        searchView?.let {
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
                            val preferenceIds = kutePreferencesTree
                                    .findInSearchProviders(it.toString())
                                    .map {
                                        it
                                                .key
                                    }
                            showPreferenceItems(preferenceIds, false)
                        }
                    }, onError = {
                        Log.e(TAG, "Error filtering list", it)
                    })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        kutePreferencesTree = initPreferenceTree()

        when {
            backstack.isNotEmpty() -> showPreferenceItems(backstack.peek())
            else -> showTopLevel()
        }
    }

    fun showTopLevel() {
        showPreferenceItems(kutePreferencesTree.getTopLevelItems().map {
            it.key
        })
    }

    /**
     * Show a specific category
     *
     * @param category the category to show
     */
    fun showCategory(category: KutePreferenceListItem) {
        val categoryItems = kutePreferencesTree
                .getCategoryItems(category.key)
                .map {
                    it.key
                }

        showPreferenceItems(categoryItems)
    }

    internal fun showPreferenceItems(backstackItem: BackstackItem) {
        searchView?.setQuery(backstackItem.searchText, false)
        showPreferenceItems(backstackItem.preferenceItemIds, false)
    }

    internal fun showPreferenceItems(preferenceIds: List<Int>, addToStack: Boolean = true) {
        if (addToStack) {
            val query = if (searchView != null) {
                searchView?.query.toString()
            } else {
                ""
            }

            backstack.push(BackstackItem(preferenceIds, query))
        }

        val preferenceItems: List<KutePreferenceListItem> =
                kutePreferencesTree
                        .findInTree {
                            preferenceIds
                                    .contains(it.key)
                        }

        generatePage(preferenceItems)
    }

    /**
     * Generates a ViewGroup for the given preferences
     */
    internal fun generatePage(kutePreference: List<KutePreferenceListItem>) {
        // find the layout where list items should be inserted
        val listItemLayout: ViewGroup = kute_preferences__list_item_root
        listItemLayout
                .removeAllViews()

        val keySet: MutableSet<Int> = mutableSetOf()
        kutePreference
                .forEach {
                    // does NOT recurse through child items as we only want to inflate the current tree layer
                    inflate(it, listItemLayout, keySet)
                }
    }

    internal fun inflate(kutePreferenceListItem: KutePreferenceListItem, layoutToAppendTo: ViewGroup,
                         keySet: MutableSet<Int>) {
        when (kutePreferenceListItem) {
            is KutePreferenceItem<*> -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
            is KutePreferenceCategory -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
        }

        val layout: ViewGroup = kutePreferenceListItem.inflateListLayout(layoutInflater)
        layoutToAppendTo.addView(layout)

        if (kutePreferenceListItem is KutePreferenceClickListener) {
            layout
                    .setOnClickListener {
                        kutePreferenceListItem
                                .onClick(layoutInflater.context)

                        if (kutePreferenceListItem is KutePreferenceCategory) {
                            showCategory(kutePreferenceListItem)
                        }
                    }
        }
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
    abstract fun initPreferenceTree(): KutePreferencesTree

    /**
     * Call this from your activity's {@link AppCompatActivity.onBackPressed()} to ensure
     * back navigation behaviour is working es expected
     * @return true if a navigation happened (aka the back button event was consumed), false otherwise
     */
    open fun onBackPressed(): Boolean {
        searchView?.let {
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
        const val TAG: String = "MainFragment"
    }

}