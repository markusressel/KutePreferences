package de.markusressel.kutepreferences.core.viewmodel

import androidx.lifecycle.*
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.core.preference.section.KutePreferenceSection
import de.markusressel.kutepreferences.core.tree.TreeManager
import de.markusressel.kutepreferences.core.view.BackstackItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class MainFragmentViewModel : ViewModel() {

    private lateinit var treeManager: TreeManager

    private var preferenceTree: Array<KutePreferenceListItem>? = null

    val currentCategory = MutableLiveData<Int>(null)

    val isSearchExpanded = MutableLiveData(false)
    val currentSearchFilter = MutableLiveData<String>()

    private var searchFilterJob = Job()
    val searchFilterResultItems: LiveData<List<Int>> = Transformations.switchMap(currentSearchFilter) {
        searchFilterJob.cancel()
        searchFilterJob = Job()
        liveData(CoroutineScope(searchFilterJob + Dispatchers.IO).coroutineContext) {
            if (isSearching()) {
                val preferenceItemKeys = treeManager.findInSearchProviders(it).map { it.key }
                //showPreferenceItems(treeManager.findInSearchProviders(it).map { it.key },
                //     addToStack = false)
                emit(preferenceItemKeys)
            } else {
                //showTopLevel()
                emit(emptyList<Int>())
            }
        }
    }

    val currentPreferenceItems = MutableLiveData<List<KutePreferenceListItem>>()


    /**
     * Stack of previously visible preference items, including the current ones
     */
    private val backstack: Stack<BackstackItem> = Stack()

    init {
    }

    /**
     * Set the complete preference tree to use
     */
    fun setPreferenceTree(preferenceItems: Array<KutePreferenceListItem>) {
        treeManager = TreeManager(*preferenceItems)
        preferenceTree = preferenceItems
        showTopLevel()
    }

    /**
     * @return true when [setPreferenceTree] has been called at least once
     */
    fun hasPreferenceTree(): Boolean {
        return preferenceTree != null
    }

    /**
     * @return true if a search is currently open, false otherwise
     */
    fun isSearching(): Boolean {
        return currentSearchFilter.value?.isNotBlank() ?: false
    }

    /**
     * Show the top level preferences page
     */
    fun showTopLevel(keysToHighlight: List<Int> = emptyList()) {
        showPreferenceItems(treeManager.getTopLevelItems().map { it.key },
                // small workaround to allow searchView non-nullable type
                ignoreSearch = true, keysToHighlight = keysToHighlight)
    }

    /**
     * Show a specific category
     *
     * @param category the category to show
     */
    fun showCategory(category: KutePreferenceCategory) {
        currentCategory.value = category.key
        val categoryItems = treeManager
                .getCategoryItems(category.key)
                .map { it.key }

        clearSearch()
        showPreferenceItems(categoryItems)
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
        if (!isSearchExpanded.value!!) {
            return
        }

        clearSearch()
        if (categoryItems != null) {
            showPreferenceItems(categoryItems, keysToHighlight = listOf(section.key))
        } else {
            // if the section has no parent category it must be a root section
            showTopLevel(keysToHighlight = listOf(section.key))
        }
    }

    private fun showPreferenceItems(backstackItem: BackstackItem) {
        currentSearchFilter.value = backstackItem.searchText
//        searchView.setQuery(backstackItem.searchText, false)
        showPreferenceItems(backstackItem.preferenceItemIds, false)
    }

    private fun showPreferenceItems(preferenceKeys: List<Int>, addToStack: Boolean = true, ignoreSearch: Boolean = false, keysToHighlight: List<Int> = emptyList()) {
        if (addToStack) {
            val query = if (ignoreSearch) "" else currentSearchFilter.value ?: ""
            val newBackstackItem = BackstackItem(preferenceKeys, query)
            addToBackstack(newBackstackItem)
        }

        val preferenceItems: List<KutePreferenceListItem> =
                treeManager.findInTree { it.key in preferenceKeys }
                        .mapNotNull { it.item }.distinctBy { it.key }
        currentPreferenceItems.value = preferenceItems
    }

    /**
     * Navigate up the section backstack
     *
     * @return true, when there was an item on the backstack and a navigation was done, false otherwise
     */
    fun navigateUp(): Boolean {
        if (setSearch("")) return true

        return when {
            backstack.size > 1 -> {
                backstack.pop()
                showPreferenceItems(backstack.peek())
                true
            }
            else -> false
        }
    }

    /**
     * Set the search string
     *
     * @return true if the value has changed, false otherwise
     */
    fun setSearch(text: String): Boolean {
        return if (currentSearchFilter.value != text) {
            currentSearchFilter.value = text
            true
        } else false
    }

    private fun clearSearch() {
        setSearch("")
        if (isSearchExpanded.value != false) {
            isSearchExpanded.value = false
        }
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

}