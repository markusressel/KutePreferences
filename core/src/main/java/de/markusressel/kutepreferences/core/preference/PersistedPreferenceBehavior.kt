package de.markusressel.kutepreferences.core.preference

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Generic behavior for [KutePreferenceItem]s which provides
 * compose compatible access to the [persistedValue] of the [KutePreferenceItem]
 * as well as a convenience [currentValue] for display.
 */
abstract class PersistedPreferenceBehavior<T : KutePreferenceItem<K>, K : Any>(
    val preferenceItem: T
) : KuteItemBehavior {

    data class UiState(
        val shimmering: Boolean = false
    )

    protected val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val persistedValue: StateFlow<K> = preferenceItem.getPersistedValueFlow()
    val currentValue: MutableStateFlow<K> = MutableStateFlow(persistedValue.value)

    /**
     * Called when the current value is changed
     */
    open fun onInputChanged(input: K) {
        currentValue.value = input
    }

    /**
     * Persists the current value
     */
    open fun persistCurrentValue() {
        preferenceItem.persistValue(currentValue.value)
    }

    /**
     * Resets the [currentValue] to the default one specified by the [preferenceItem]
     */
    open fun restoreDefaultValue() {
        currentValue.value = preferenceItem.getDefaultValue()
    }

    /**
     * Resets any changes made to the [currentValue] and resets it to the [persistedValue]
     */
    fun reset() {
        currentValue.value = persistedValue.value
    }

    override fun highlight() {
        _uiState.update { old -> old.copy(shimmering = true) }
    }

    override fun clearHighlight() {
        _uiState.update { old -> old.copy(shimmering = false) }
    }

}