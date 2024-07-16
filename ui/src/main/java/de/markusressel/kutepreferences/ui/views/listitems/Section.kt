package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.section.KuteSectionBehavior
import de.markusressel.kutepreferences.ui.theme.KuteColors
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors
import de.markusressel.kutepreferences.ui.theme.SectionTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CollapsibleCard
import de.markusressel.kutepreferences.ui.views.search.Composable

@CombinedPreview
@Composable
private fun KuteSectionPreview() {
    val preferenceItem = KuteSection(
        key = 0,
        title = "Section Title",
        children = emptyList(),
    )

    KutePreferencesTheme(
        colors = KuteColors(
            section = SectionTheme(
                //titleTextColor = Color.Blue
            ),
        )
    ) {
        KuteSectionView(
            behavior = KuteSectionBehavior(preferenceItem),
        )
    }
}

@Composable
fun KuteSectionView(
    behavior: KuteSectionBehavior
) {
    val uiState by behavior.uiState.collectAsState()
    var collapsed by remember { mutableStateOf(false) }

    CollapsibleCard(
        modifier = Modifier
            .modifyIf(uiState.shimmering) {
                highlightingShimmer()
            }
            .padding(8.dp),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LocalKuteColors.current.section.titleBackgroundColor
                    )
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(
                        horizontal = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = behavior.preferenceItem.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = LocalKuteColors.current.section.titleTextColor
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LocalKuteColors.current.section.contentBackgroundColor)
            ) {
                behavior.preferenceItem.children.forEach {
                    it.Composable()
                }
            }
        },
        collapsed = collapsed,
        onToggleCollapsedState = { collapsed = collapsed.not() },
    )
}
