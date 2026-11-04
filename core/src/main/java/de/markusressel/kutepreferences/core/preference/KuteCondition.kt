package de.markusressel.kutepreferences.core.preference

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A condition that can change over time, used to show or enable a [KutePreferenceListItem] only in some situations.
 *
 * Use [matches] to depend on the value of another preference, and [asCondition] to depend on anything else.
 *
 * @param read reads the current state
 * @param changes emits the state whenever it changes
 */
class KuteCondition(
    private val read: () -> Boolean,
    changes: Flow<Boolean>,
) {

    val changes: Flow<Boolean> = changes.distinctUntilChanged()

    /**
     * The current state
     */
    val isMet: Boolean
        get() = read()

    infix fun and(other: KuteCondition) = KuteCondition(
        read = { isMet && other.isMet },
        changes = combine(changes, other.changes) { first, second -> first && second },
    )

    infix fun or(other: KuteCondition) = KuteCondition(
        read = { isMet || other.isMet },
        changes = combine(changes, other.changes) { first, second -> first || second },
    )

    operator fun not() = KuteCondition(
        read = { isMet.not() },
        changes = changes.map { it.not() },
    )

}

/**
 * @return a condition that is met while the saved value of this preference matches the given [predicate]
 */
fun <T : Any> KutePreferenceItem<T>.matches(predicate: (T) -> Boolean) = KuteCondition(
    read = { predicate(persistedValue.value) },
    changes = persistedValue.map(predicate),
)

/**
 * @return a condition that is met while this flow holds true, f.ex. the state of a subscription
 */
fun StateFlow<Boolean>.asCondition() = KuteCondition(
    read = { value },
    changes = this,
)
