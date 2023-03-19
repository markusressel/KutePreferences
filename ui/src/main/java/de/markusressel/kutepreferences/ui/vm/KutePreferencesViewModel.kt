package de.markusressel.kutepreferences.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class KutePreferencesViewModel(
    val navigator: KuteNavigator,
) : ViewModel() {

    private val findCategoryByKeyUseCase: FindCategoryByKeyUseCase = FindCategoryByKeyUseCase()
    private val searchItemsUseCase: SearchItemsUseCase = SearchItemsUseCase()

    val preferencesUiState = MutableStateFlow<UiState>(UiState.Loading)

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

    private fun computeOverviewState(category: Int?, allItems: List<KutePreferenceListItem>): UiState.Overview {
        val items = when {
            category != null -> findCategoryByKeyUseCase(allItems, category)?.children ?: emptyList()
            else -> allItems
        }

        return UiState.Overview(
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

    open fun onUiEvent(event: KuteUiEvent) {
        when (event) {
            is KuteUiEvent.StartSearch -> {
                preferencesUiState.update { oldState ->
                    UiState.Searching()
                }
            }
            is KuteUiEvent.SearchTermChanged -> {
                preferencesUiState.update { oldState ->
                    (oldState as UiState.Searching).copy(
                        searchTerm = event.searchTerm,
                        preferenceItems = searchItemsUseCase(preferenceItems.value, event.searchTerm)
                    )
                }
            }
            is KuteUiEvent.CloseSearch -> {
                preferencesUiState.update { oldState ->
                    computeOverviewState(navigator.currentCategory.value, preferenceItems.value)
                }
            }
        }
    }
}

sealed class UiAction {
    data class ScrollToTop(val animate: Boolean) : UiAction()
}

sealed class UiState {
    object Loading : UiState()

    data class Searching(
        val searchTerm: String = "",
        val preferenceItems: List<KutePreferenceListItem> = emptyList(),
    ) : UiState()

    data class Overview(
        val currentCategory: Int? = null,
        val preferenceItems: List<KutePreferenceListItem> = emptyList(),
    ) : UiState()
}