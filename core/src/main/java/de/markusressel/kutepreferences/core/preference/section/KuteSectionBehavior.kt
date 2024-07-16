package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import de.markusressel.kutepreferences.core.preference.category.shimmerLengthMillis
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class KuteSectionBehavior(
    val preferenceItem: KuteSection
) : KuteItemBehavior {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState

    override fun highlight() {
        GlobalScope.launch {
            _uiState.value = _uiState.value.copy(shimmering = true)
            delay(shimmerLengthMillis.toLong())
            _uiState.value = _uiState.value.copy(shimmering = false)
        }
    }

    data class UiState(
        val shimmering: Boolean = false
    )
}