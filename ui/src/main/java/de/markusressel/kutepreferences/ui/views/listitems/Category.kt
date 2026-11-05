package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteCategoryBehavior
import de.markusressel.kutepreferences.ui.theme.*
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KuteCategoryView(
    behavior: KuteCategoryBehavior,
    modifier: Modifier = Modifier,
) {
    val uiState by behavior.uiState.collectAsStateWithLifecycle()

    val selection = LocalCategorySelection.current
    val isSelected = selection?.selectedKey == behavior.preferenceItem.key
    val containerColor = when {
        isSelected -> MaterialTheme.colorScheme.secondaryContainer
        else -> LocalKuteColors.current.category.cardBackgroundColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = listItemMinHeight)
            .highlightingShimmer(uiState.shimmering, behavior::clearHighlight)
            .padding(horizontal = 8.dp)
            .then(modifier),
        elevation = CardDefaults.cardElevation(LocalKuteColors.current.category.elevation),
        shape = itemShape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = listItemMinHeight)
                .background(containerColor)
                .then(
                    when (selection) {
                        null -> Modifier
                        else -> Modifier.semantics { selected = isSelected }
                    }
                )
                .combinedClickable(
                    onClick = {
                        when {
                            selection != null && behavior.preferenceItem.onClick == null ->
                                selection.onSelect(behavior.preferenceItem.key)

                            else -> behavior.onClick()
                        }
                    },
                    onLongClick = behavior.preferenceItem.onLongClick,
                )
        ) {
            val icon = behavior.preferenceItem.icon

            DefaultPreferenceListItemCardContent(
                icon = icon,
                iconColor = LocalKuteColors.current.category.iconColor,
                title = behavior.preferenceItem.title,
                titleColor = LocalKuteColors.current.category.titleColor,
                subtitle = behavior.preferenceItem.description,
                subtitleColor = LocalKuteColors.current.category.subtitleColor,
            )
        }
    }
}

@CombinedPreview
@Composable
private fun KuteCategoryPreview() {
    val icon = AppCompatResources.getDrawable(
        LocalContext.current,
        android.R.drawable.ic_menu_gallery
    )

    KutePreferencesTheme(
        colors = KuteColors(
            category = CategoryTheme(
                cardBackgroundColor = Color.Gray,
                titleColor = Color.Blue,
                subtitleColor = Color.Green,
                iconColor = Color.Red,
            ),
        )
    ) {
        KuteCategoryView(
            behavior = KuteCategoryBehavior(
                KuteCategory(
                    key = "0",
                    icon = icon,
                    title = "Category Title",
                    description = "This is a long description of this category.",
                ),
                navigator = DefaultKuteNavigator()
            ),
        )
    }
}
