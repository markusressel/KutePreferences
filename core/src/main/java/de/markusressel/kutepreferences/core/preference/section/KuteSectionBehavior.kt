package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.preference.KuteExpandable
import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class KuteSectionBehavior(
    val preferenceItem: KuteSection
) : KuteItemBehavior, KuteExpandable {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun collapse() {
        _uiState.update { old -> old.copy(isCollapsed = true) }
    }

    override fun expand() {
        _uiState.update { old -> old.copy(isCollapsed = false) }
    }

    fun toggleCollapsed() {
        _uiState.update { old -> old.copy(isCollapsed = old.isCollapsed.not()) }
    }

    override fun highlight() {
        _uiState.update { old -> old.copy(isShimmering = true) }
    }

    override fun clearHighlight() {
        _uiState.update { old -> old.copy(isShimmering = false) }
    }

    data class UiState(
        val isShimmering: Boolean = false,
        val isCollapsed: Boolean = false,
    )
}