package de.markusressel.kutepreferences.ui.views.search

import androidx.compose.runtime.Composable
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.views.Composable as RenderItem

@Deprecated(
    message = "Moved to the util package",
    replaceWith = ReplaceWith("dummy", "de.markusressel.kutepreferences.ui.util.dummy"),
)
val dummy: DummyDataProvider
    get() = de.markusressel.kutepreferences.ui.util.dummy

@Deprecated(
    message = "Moved to the views package",
    replaceWith = ReplaceWith("Composable()", "de.markusressel.kutepreferences.ui.views.Composable"),
)
@Composable
fun KutePreferenceListItem.Composable() = RenderItem()
