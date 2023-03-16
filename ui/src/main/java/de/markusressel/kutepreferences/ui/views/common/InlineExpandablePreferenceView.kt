package de.markusressel.kutepreferences.ui.views.common

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors
import de.markusressel.kutepreferences.ui.theme.listItemMinHeight
import de.markusressel.kutepreferences.ui.views.listitems.DefaultPreferenceListItemCardContent

@Composable
fun InlineExpandablePreferenceView(
    modifier: Modifier = Modifier,
    icon: Drawable? = null,
    title: String = "Some Preference Label",
    subtitle: String = "some-value",
    header: @Composable () -> Unit = {
        DefaultPreferenceListItemCardContent(
            icon = icon,
            title = title,
            titleColor = LocalKuteColors.current.defaultItem.titleColor,
            subtitle = subtitle,
        )
    },
    content: @Composable () -> Unit,
    collapsed: Boolean,
    onToggleCollapsedState: () -> Unit,
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = listItemMinHeight)
            .then(modifier),
    ) {
        CollapsibleSection(
            header = header,
            content = content,
            collapsed = collapsed,
            onToggleCollapsedState = onToggleCollapsedState,
        )
    }
}