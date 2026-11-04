package de.markusressel.kutepreferences.core.preference.action

import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ActionPreferenceBehavior(
    val preferenceItem: KuteAction
) : KuteItemBehavior {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onClick() {
        preferenceItem.onClick?.invoke()
    }

    fun onLongClick() {
        preferenceItem.onLongClick?.invoke()
    }

    override fun highlight() {
        _uiState.update { old -> old.copy(shimmering = true) }
    }

    override fun clearHighlight() {
        _uiState.update { old -> old.copy(shimmering = false) }
    }

    data class UiState(
        val shimmering: Boolean = false,
    )
}
