package de.markusressel.kutepreferences.ui.vm

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class KutePreferencesViewModel(
    val navigator: KuteNavigator
) : ViewModel() {

    private val preferencesUiState = MutableStateFlow(UiState())

    private val currentCategory = preferencesUiState.map { uiState ->
        uiState.preferenceItems.findRecursive { it.key == uiState.currentCategoryKey } as KutePreferenceCategory?
    }

    val title = currentCategory.map {
        it?.title ?: "Preferences"
    }

    val currentPreferenceItems = combine(preferencesUiState, currentCategory) { uiState, category ->
        when (category) {
            null -> uiState.preferenceItems
            else -> category.children
        }
    }

    init {
        KuteStyleManager.registerDefaultStyles(navigator)

        viewModelScope.launch {
            navigator.currentCategory.collectLatest { key ->
                preferencesUiState.value = preferencesUiState.value.copy(
                    currentCategoryKey = key
                )
            }
        }
    }

    /**
     * Set the full tree of [KutePreferenceListItem] entries
     */
    open fun initPreferencesTree(items: List<KutePreferenceListItem>) {
        preferencesUiState.value = preferencesUiState.value.copy(
            preferenceItems = items
        )
    }

}

private fun List<KutePreferenceListItem>.findRecursive(
    predicate: (KutePreferenceListItem) -> Boolean
): KutePreferenceListItem? {
    val topLevelResult = firstOrNull(predicate)
    if (topLevelResult != null) {
        return topLevelResult
    }

    forEach {
        val itemResult = it.findRecursive(predicate)
        if (itemResult != null) {
            return itemResult
        }
    }

    return null
}

private fun KutePreferenceListItem.findRecursive(
    predicate: (KutePreferenceListItem) -> Boolean
): KutePreferenceListItem? {
    return when (this) {
        predicate -> this
        is KuteParent -> children.findRecursive(predicate)
        else -> null
    }
}

data class UiState(
    val preferenceItems: List<KutePreferenceListItem> = emptyList(),
    @StringRes val currentCategoryKey: Int? = null
)