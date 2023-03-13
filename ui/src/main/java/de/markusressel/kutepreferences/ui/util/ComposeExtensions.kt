package de.markusressel.kutepreferences.ui.util

import androidx.compose.ui.Modifier

fun Modifier.modifyIf(predicate: () -> Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return when {
        predicate() -> modifier()
        else -> this
    }
}