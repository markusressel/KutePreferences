package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import de.markusressel.kutepreferences.core.preference.category.shimmerLengthMillis
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class KuteSectionBehavior(
    val preferenceItem: KuteSection
) : KuteItemBehavior {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState

    fun collapse() {
        _uiState.update { old -> old.copy(isCollapsed = true) }
    }

    fun expand() {
        _uiState.update { old -> old.copy(isCollapsed = false) }
    }

    fun toggleCollapsed() {
        _uiState.update { old -> old.copy(isCollapsed = old.isCollapsed.not()) }
    }

    override fun highlight() {
        GlobalScope.launch {
            _uiState.value = _uiState.value.copy(isShimmering = true)
            delay(shimmerLengthMillis.toLong())
            _uiState.value = _uiState.value.copy(isShimmering = false)
        }
    }

    data class UiState(
        val isShimmering: Boolean = false,
        val isCollapsed: Boolean = false,
    )
}