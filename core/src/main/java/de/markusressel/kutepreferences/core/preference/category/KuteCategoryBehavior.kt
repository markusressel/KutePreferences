package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


const val durationMillis = 800
const val delayMillis = 300
const val iterations = 2
const val shimmerLengthMillis = (durationMillis + delayMillis) * iterations


open class KuteCategoryBehavior(
    val preferenceItem: KuteCategory,
    val navigator: KuteNavigator,
) : KuteItemBehavior {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onClick() {
        navigator.enterCategory(preferenceItem.key)
    }

    override fun highlight() {
        GlobalScope.launch {
            _uiState.emit(uiState.value.copy(shimmering = true))
            delay(shimmerLengthMillis.toLong())
            _uiState.emit(uiState.value.copy(shimmering = false))
        }
    }

    data class UiState(
        val shimmering: Boolean = false,
    )
}
