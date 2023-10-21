package de.markusressel.kutepreferences.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.ui.views.BehaviorStore
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import kotlinx.coroutines.channels.Channel
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
    fun navigateTo(item: KutePreferenceListItem) {
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

        val categoryStack = itemStack.run {
            when (lastItem) {
                !is KuteParent -> dropLast(1)
                else -> this
            }
        }.map { it.key }
        navigator.setStack(categoryStack)
        BehaviorStore.get(lastItem)?.highlight()
    }

    open fun onUiEvent(event: KuteUiEvent) {
        when (event) {
            is KuteUiEvent.StartSearch -> {
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

            is KuteUiEvent.CloseSearch -> {
                preferencesUiState.update { _ ->
                    computeOverviewState(navigator.currentCategory.value, preferenceItems.value)
                }
            }
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