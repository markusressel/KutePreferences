package de.markusressel.kutepreferences.library.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
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

    lateinit var kutePreferencesTree: KutePreferencesTree

    /**
     * Stack of previously visible preference items, including the current ones
     */
    internal var stack: Stack<List<Int>> by savedInstanceState(Stack())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater
                .inflate(R.layout.kute_preference__main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        kutePreferencesTree = initPreferenceTree()

        if (stack.isEmpty()) {
            showTopLevel()
        } else {
            replaceContent(stack.peek())
        }
    }

    override fun onResume() {
        super.onResume()

        RxSearchView
                .queryTextChanges(kute_preferences__search)
                .skipInitialValue()
                .bindToLifecycle(kute_preferences__search)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    if (it.isBlank()) {
                        showTopLevel()
                    } else {
                        val preferenceIds = kutePreferencesTree
                                .findInSearchProviders(it.toString())
                                .map {
                                    it
                                            .key
                                }
                        replaceContent(preferenceIds)
                    }
                }, onError = {
                    Log.e(TAG, "Search error", it)
                })
    }

    private fun showSearchResults() {
        // TODO:
    }

    private fun hideSearchResults() {
        // TODO:
    }

    private fun showTopLevel() {
        replaceContent(kutePreferencesTree.getTopLevelItems().map {
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

        replaceContent(categoryItems)
    }

    private fun replaceContent(preferenceIds: List<Int>, addToStack: Boolean = true) {
        if (addToStack) {
            stack.push(preferenceIds)
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
    private fun generatePage(kutePreference: List<KutePreferenceListItem>) {
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

    private fun inflate(kutePreferenceListItem: KutePreferenceListItem, layoutToAppendTo: ViewGroup,
                        keySet: MutableSet<Int>) {
        when (kutePreferenceListItem) {
            is KutePreferenceItem<*> -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
            is KutePreferenceCategory -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
        }

        val layout: ViewGroup = inflate(kutePreferenceListItem)
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

    private fun inflate(it: KutePreferenceListItem): ViewGroup {
        return it
                .inflateListLayout(layoutInflater)
    }

    /**
     * Initialize your preferences tree here
     */
    abstract fun initPreferenceTree(): KutePreferencesTree

    /**
     * Call this from your activity's {@link AppCompatActivity.onBackPressed()} to ensure
     * back navigation behaviour is working es expected
     * @return true if a navigation happened, false otherwise
     */
    open fun onBackPressed(): Boolean {
        return if (stack.size > 1) {
            stack.pop()
            replaceContent(stack.peek(), false)
            true
        } else {
            false
        }
    }

    companion object {
        const val TAG: String = "MainFragment"
    }

}