package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors
import de.markusressel.kutepreferences.ui.theme.itemShape

@Composable
fun CollapsibleCard(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    collapsed: Boolean,
    onToggleCollapsedState: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = if (isSystemInDarkTheme()) Color.Black else Color.White,
) {
    Card(
        modifier = modifier,
        shape = itemShape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(LocalKuteColors.current.section.elevation),
    ) {
        CollapsibleSection(
            header = title,
            content = content,
            collapsed = collapsed,
            onToggleCollapsedState = onToggleCollapsedState,
        )
    }
}

@Composable
fun CollapsibleSection(
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    collapsed: Boolean,
    onToggleCollapsedState: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onToggleCollapsedState()
                }
        ) {
            header()
        }
        AnimatedVisibility(
            enter = expandVertically(),
            exit = shrinkVertically(),
            visible = collapsed.not()
        ) {
            content()
        }
    }
}
