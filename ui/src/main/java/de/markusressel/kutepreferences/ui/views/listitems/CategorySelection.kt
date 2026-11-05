package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.runtime.compositionLocalOf

/**
 * The categories of the list pane of a two pane layout, where selecting one replaces the open categories.
 *
 * @param selectedKey the key of the category that is shown in the other pane
 * @param onSelect called with the key of a category that was clicked
 */
internal class CategorySelection(
    val selectedKey: String?,
    val onSelect: (String) -> Unit,
)

internal val LocalCategorySelection = compositionLocalOf<CategorySelection?> { null }
