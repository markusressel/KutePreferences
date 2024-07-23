package de.markusressel.kutepreferences.ui.views.listitems

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import de.markusressel.kutepreferences.ui.theme.*
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.modifyIf

/**
 * Simple default UI to present a preference item
 * in the preferences list
 */


@Composable
internal fun DefaultPreferenceListItem(
    modifier: Modifier = Modifier,
    icon: Drawable?,
    title: String = "Some Preference Label",
    subtitle: String = "some-value",
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    DefaultPreferenceListItemCardContent(
        modifier = modifier,
        icon = icon,
        title = title,
        titleColor = LocalKuteColors.current.defaultItem.titleColor,
        subtitle = subtitle,
        onClick = onClick,
        onLongClick = onLongClick,
    )
}

@Composable
internal fun DefaultPreferenceListItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int?,
    title: String = "Some Preference Label",
    subtitle: String = "some-value",
    onClick: () -> Unit = {},
) {
    DefaultPreferenceListItem(
        modifier = modifier,
        icon = icon?.let { AppCompatResources.getDrawable(LocalContext.current, it) },
        title = title,
        subtitle = subtitle,
        onClick = onClick,
    )
}

@Composable
internal fun DefaultPreferenceListItemCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = itemShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .defaultMinSize(minHeight = listItemMinHeight)
            .padding(8.dp)
            .then(modifier),
        shape = shape,
        onClick = { onClick() },
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DefaultPreferenceListItemCardContent(
    modifier: Modifier = Modifier,
    icon: Drawable? = null,
    iconColor: Color = LocalKuteColors.current.defaultItem.iconColor,
    title: String = "Some Preference Label",
    titleColor: Color = LocalKuteColors.current.defaultItem.titleColor,
    subtitle: String = "some-value",
    subtitleColor: Color = LocalKuteColors.current.defaultItem.subtitleColor,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .modifyIf(onClick != null || onLongClick != null) {
                combinedClickable(
                    onClick = onClick ?: {},
                    onLongClick = onLongClick,
                )
            }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Image(
                modifier = Modifier
                    .size(28.dp)
                    .padding(4.dp),
                bitmap = icon.toBitmap().asImageBitmap(),
                contentDescription = "",
                colorFilter = ColorFilter.tint(iconColor)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))

        DefaultPreferenceListItemTitle(
            title = title,
            titleColor = titleColor,
            subtitle = subtitle,
            subtitleColor = subtitleColor,
        )
    }
}

/**
 * The default "title and subtitle" view
 */
@Composable
internal fun DefaultPreferenceListItemTitle(
    title: String,
    titleColor: Color = LocalKuteColors.current.defaultItem.titleColor,
    subtitle: String,
    subtitleColor: Color = LocalKuteColors.current.defaultItem.subtitleColor,
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = titleColor,
        )
        if (subtitle.isNotBlank()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = subtitleColor,
            )
        }
    }
}

@CombinedPreview
@Composable
private fun DefaultItemPreview() {
    KutePreferencesTheme(
        colors = KuteColors(
            defaultItem = DefaultItemTheme(
                titleColor = Color.Red,
                subtitleColor = Color.Green,
                iconColor = Color.Blue,
            )
        )
    ) {
        DefaultPreferenceListItem(
            icon = android.R.drawable.ic_menu_gallery,
        )
    }
}
