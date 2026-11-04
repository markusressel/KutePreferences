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
import de.markusressel.kutepreferences.ui.views.KuteRegistry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

    val preferencesUiState = MutableStateFlow(UiState())

    private val uiActionChannel = Channel<UiAction>(Channel.BUFFERED)
    val uiActions = uiActionChannel.receiveAsFlow()

    private val preferenceItems = MutableStateFlow<List<KutePreferenceListItem>>(emptyList())
    private val loading = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            navigator.currentCategory.collectLatest {
                uiActionChannel.send(UiAction.ScrollToTop(false))
            }
        }

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
            preferenceItems = items,
            searchResults = emptyList(),
        )
    }


    /**
     * Set the full tree of [KutePreferenceListItem] entries.
     *
     * The items are shown once the persisted values of all items have been loaded,
     * so that rendering them does not block the main thread.
     */
    open fun initPreferencesTree(items: List<KutePreferenceListItem>) {
        loading.value = true
        viewModelScope.launch {
            items.flatten()
                .filterIsInstance<KutePreferenceItem<*>>()
                .map { it.dataProvider }
                .distinct()
                .forEach { it.load() }

            preferenceItems.value = items
            loading.value = false
        }
    }

    private fun List<KutePreferenceListItem>.flatten(): List<KutePreferenceListItem> = flatMap {
        when (it) {
            is KuteParent -> listOf(it) + it.children.flatten()
            else -> listOf(it)
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
)