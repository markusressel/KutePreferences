package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteCategoryBehavior
import de.markusressel.kutepreferences.ui.theme.*


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
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
                    key = 0,
                    icon = icon,
                    title = "Category Title",
                    description = "This is a long description of this category.",
                ),
                navigator = DefaultKuteNavigator()
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuteCategoryView(
    behavior: KuteCategoryBehavior,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = listItemMinHeight)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(LocalKuteColors.current.category.elevation),
        shape = itemShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
        ),
        onClick = { behavior.onClick() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalKuteColors.current.category.cardBackgroundColor)
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