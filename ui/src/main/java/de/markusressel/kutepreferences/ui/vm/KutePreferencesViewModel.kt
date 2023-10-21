package de.markusressel.kutepreferences.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.ui.views.BehaviorStore
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class KutePreferencesViewModel(
    val navigator: KuteNavigator,
) : ViewModel() {

    private val findCategoryByKeyUseCase: FindCategoryByKeyUseCase = FindCategoryByKeyUseCase()
    private val findItemStackUseCase: FindItemStackUseCase = FindItemStackUseCase()
    private val searchItemsUseCase: SearchItemsUseCase = SearchItemsUseCase()

    val preferencesUiState = MutableStateFlow(UiState())

    private val uiActionChannel = Channel<UiAction>(Channel.BUFFERED)
    val uiActions = uiActionChannel.receiveAsFlow()

    private val preferenceItems = MutableStateFlow<List<KutePreferenceListItem>>(emptyList())

    init {
        KuteStyleManager.registerDefaultStyles(navigator)

        viewModelScope.launch {
            navigator.currentCategory.collectLatest {
                uiActionChannel.send(UiAction.ScrollToTop(false))
            }
        }

        viewModelScope.launch {
            combine(
                navigator.currentCategory,
                preferenceItems,
            ) { category, allItems ->
                computeOverviewState(category, allItems)
            }.collectLatest { newState ->
                preferencesUiState.update { newState }
            }
        }
    }

    private fun computeOverviewState(category: Int?, allItems: List<KutePreferenceListItem>): UiState {
        val items = when {
            category != null -> findCategoryByKeyUseCase(allItems, category)?.children ?: emptyList()
            else -> allItems
        }

        return UiState(
            isSearchActive = false,
            searchTerm = "",
            currentCategory = category,
            preferenceItems = items
        )
    }


    /**
     * Set the full tree of [KutePreferenceListItem] entries
     */
    open fun initPreferencesTree(items: List<KutePreferenceListItem>) {
        preferenceItems.value = items
    }

    /**
     * Navigates through the [preferenceItems] tree to the given [KutePreferenceListItem].
     *
     * The navigator will navigate to the items parent category (if any),
     * the view will scroll to the target item and the item will be highlighted temporarily.
     */
    private suspend fun navigateTo(item: KutePreferenceListItem) {
        val itemStack = try {
            findItemStackUseCase(preferenceItems.value, item)
        } catch (ex: Exception) {
            Log.w(
                "KutePreferences",
                "Could not find category with key ${item.key} in preference tree"
            )
            return
        }

        val lastItem = itemStack.last()

        val categoryStack = itemStack.filterIsInstance<KuteCategory>()
        navigator.backToTop()
        navigator.setCategories(categoryStack.map { it.key })

        delay(300)

        val behavior = BehaviorStore.get(lastItem)
        behavior?.highlight()
    }

    open fun onUiEvent(event: KuteUiEvent) {
        viewModelScope.launch {
            when (event) {
                is KuteUiEvent.SearchFieldSelected -> {
                    preferencesUiState.update { old ->
                        old.copy(
                            isSearchActive = true,
                            preferenceItems = emptyList()
                        )
                    }
                }

                is KuteUiEvent.SearchTermChanged -> {
                    preferencesUiState.update { oldState ->
                        oldState.copy(
                            searchTerm = event.searchTerm,
                            preferenceItems = searchItemsUseCase(preferenceItems.value, event.searchTerm)
                        )
                    }
                }

                is KuteUiEvent.SearchResultSelected -> {
                    navigateTo(event.item)
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
                        if (navigator.goBack().not()) {

                        }
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

sealed class UiAction {
    data class ScrollToTop(val animate: Boolean) : UiAction()
}

data class UiState(
    val loading: Boolean = false,

    val isSearchActive: Boolean = false,
    val searchTerm: String = "",

    val currentCategory: Int? = null,
    val preferenceItems: List<KutePreferenceListItem> = emptyList(),
)