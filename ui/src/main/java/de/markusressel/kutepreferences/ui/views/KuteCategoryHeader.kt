package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview

/**
 * Shows which category is open, with a back button and the parent categories above its title.
 *
 * @param path the open categories, ordered from root to leaf, which must not be empty
 */
@Composable
fun KuteCategoryHeader(
    path: List<KutePreferenceCategory>,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.kute_preferences_back),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Column(modifier = Modifier.padding(start = 8.dp)) {
            if (path.size > 1) {
                Text(
                    text = path.dropLast(1).joinToString(separator = " › ") { it.title },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = path.last().title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@CombinedPreview
@Composable
private fun KuteCategoryHeaderPreview() {
    KutePreferencesTheme {
        KuteCategoryHeader(
            path = listOf(
                KuteCategory(key = "1", title = "Network"),
                KuteCategory(key = "2", title = "Proxy"),
            ),
            onBackClicked = {},
        )
    }
}
