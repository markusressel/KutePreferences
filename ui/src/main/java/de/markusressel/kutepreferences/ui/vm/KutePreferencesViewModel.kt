package de.markusressel.kutepreferences.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KuteExpandable
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.ui.views.KuteRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class KutePreferencesViewModel(
    val navigator: KuteNavigator = DefaultKuteNavigator(),
    /**
     * Defines how items are rendered. Use this to add custom item types or to replace the UI of existing ones.
     */
    val registry: KuteRegistry = KuteRegistry(navigator),
) : ViewModel() {

    private val findCategoryByKeyUseCase: FindCategoryByKeyUseCase = FindCategoryByKeyUseCase()
    private val findItemStackUseCase: FindItemStackUseCase = FindItemStackUseCase()
    private val searchItemsUseCase: SearchItemsUseCase = SearchItemsUseCase()
    private val checkPreferenceKeysUseCase: CheckPreferenceKeysUseCase = CheckPreferenceKeysUseCase()

    val preferencesUiState = MutableStateFlow(UiState())

    @Suppress("DEPRECATION")
    @Deprecated("KutePreferencesScreen keeps the scroll position of each category itself, so this never emits")
    val uiActions: Flow<UiAction> = emptyFlow()

    private val preferenceItems = MutableStateFlow<List<KutePreferenceListItem>>(emptyList())
    private val loading = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            combine(
                navigator.currentCategory,
                preferenceItems,
                loading,
            ) { category, allItems, loading ->
                computeOverviewState(category, allItems).copy(loading = loading)
            }.collectLatest { newState ->
                preferencesUiState.update { newState }
            }
        }
    }

    private fun computeOverviewState(category: String?, allItems: List<KutePreferenceListItem>): UiState {
        val items = when {
            category != null -> findCategoryByKeyUseCase(allItems, category)?.children ?: emptyList()
            else -> allItems
        }

        return UiState(
            isSearchActive = false,
            searchTerm = "",
            currentCategory = category,
            categoryPath = navigator.categoryStack.mapNotNull { findCategoryByKeyUseCase(allItems, it) },
            preferenceItems = items,
            searchResults = emptyList(),
            rootItems = allItems,
        )
    }


    /**
     * Set the full tree of [KutePreferenceListItem] entries.
     *
     * The items are shown once the persisted values of all items have been loaded,
     * so that rendering them does not block the main thread.
     *
     * @throws IllegalArgumentException if items conflict, see [CheckPreferenceKeysUseCase]
     */
    open fun initPreferencesTree(items: List<KutePreferenceListItem>) {
        val persistedItems = items.flatten().filterIsInstance<KutePreferenceItem<*>>()
        checkPreferenceKeysUseCase(persistedItems)

        loading.value = true
        viewModelScope.launch {
            persistedItems
                .map { it.dataProvider }
                .distinct()
                .forEach { it.load() }

            registry.setPreferenceTree(items)
            preferenceItems.value = items
            loading.value = false
        }
    }

    /**
     * Reopens the given categories once the tree is loaded, unless something else has been opened in the meantime.
     * Only the part of the path that still exists is opened, because the tree may have changed.
     */
    internal suspend fun restoreCategories(keys: List<String>) {
        loading.first { it.not() }

        if (navigator.categoryStack.isEmpty()) {
            navigator.setCategories(keys.takeWhile { findCategoryByKeyUseCase(preferenceItems.value, it) != null })
        }
    }

    private fun List<KutePreferenceListItem>.flatten(): List<KutePreferenceListItem> = flatMap {
        when (it) {
            is KuteParent -> listOf(it) + it.children.flatten()
            else -> listOf(it)
        }
    }

    /**
     * Shows the item with the given [key], f.ex. when the user opens a notification or an error message about it.
     *
     * Opens its category and expands all sections on the way. The item then scrolls into view and is highlighted
     * temporarily. This also closes the search, and can be called right after [initPreferencesTree].
     *
     * If items share the key, the first one in the tree is shown. Nothing happens if no item has the key.
     * A hidden item stays hidden, see [KutePreferenceListItem.visibleWhen].
     */
    open fun navigateTo(key: String) {
        viewModelScope.launch {
            loading.first { it.not() }

            val item = preferenceItems.value.flatten().firstOrNull { it.key == key }
            when (item) {
                null -> Log.w("KutePreferences", "Could not find item with key $key in preference tree")
                else -> {
                    navigateTo(item)
                    closeSearch()
                }
            }
        }
    }

    /**
     * Navigates through the [preferenceItems] tree to the given [KutePreferenceListItem].
     *
     * The navigator will navigate to the items parent category (if any), all sections on the way are expanded,
     * and the item will scroll into view and be highlighted temporarily.
     */
    private suspend fun navigateTo(item: KutePreferenceListItem) {
        val itemStack = try {
            findItemStackUseCase(preferenceItems.value, item)
        } catch (_: NoSuchElementException) {
            Log.w(
                "KutePreferences",
                "Could not find item with key ${item.key} in preference tree"
            )
            return
        }

        val target = itemStack.last()

        navigator.setCategories(itemStack.filterIsInstance<KuteCategory>().map { it.key })
        itemStack.forEach { (registry.behaviorFor(it) as? KuteExpandable)?.expand() }

        // give the UI some time to show the category and to expand its sections
        delay(NAVIGATION_SETTLE_MILLIS)

        // entering a category already shows the result of navigating to it
        if (target !is KuteCategory) {
            registry.behaviorFor(target)?.highlight()
        }
    }

    open fun onUiEvent(event: KuteUiEvent) {
        viewModelScope.launch {
            when (event) {
                is KuteUiEvent.SearchFieldSelected -> {
                    preferencesUiState.update { old ->
                        old.copy(
                            isSearchActive = true,
                            searchResults = emptyList(),
                        )
                    }
                }

                is KuteUiEvent.SearchTermChanged -> {
                    preferencesUiState.update { oldState ->
                        oldState.copy(
                            searchTerm = event.searchTerm,
                            searchResults = searchItemsUseCase(preferenceItems.value, event.searchTerm)
                        )
                    }
                }

                is KuteUiEvent.SearchResultSelected -> {
                    navigateTo(event.searchResult.item)
                    preferencesUiState.update { old ->
                        computeOverviewState(navigator.currentCategory.value, preferenceItems.value)
                    }
                }

                is KuteUiEvent.CloseSearch -> {
                    closeSearch()
                }

                is KuteUiEvent.BackPressed -> {
                    if (preferencesUiState.value.isSearchActive) {
                        closeSearch()
                    } else {
                        navigator.goBack()
                    }
                }
            }
        }
    }

    private fun closeSearch() {
        preferencesUiState.update { old ->
            computeOverviewState(navigator.currentCategory.value, preferenceItems.value)
        }
    }
}

private const val NAVIGATION_SETTLE_MILLIS = 300L

@Deprecated("KutePreferencesScreen keeps the scroll position of each category itself")
sealed class UiAction {
    data class ScrollToTop(val animate: Boolean) : UiAction()
}

data class UiState(
    val loading: Boolean = false,

    val isSearchActive: Boolean = false,
    val searchTerm: String = "",
    val searchResults: List<SearchItemsUseCase.KuteSearchResultItem> = emptyList(),

    val currentCategory: String? = null,
    val preferenceItems: List<KutePreferenceListItem> = emptyList(),

    /**
     * The open categories, ordered from root to leaf
     */
    val categoryPath: List<KutePreferenceCategory> = emptyList(),

    /**
     * The items at the top level, whichever category is open
     */
    val rootItems: List<KutePreferenceListItem> = emptyList(),
)