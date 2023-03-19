package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors

@Composable
fun KutePreferenceSearch(
    modifier: Modifier = Modifier,
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearSearchTerm: () -> Unit,
    label: String = stringResource(id = R.string.kute_preferences_search_label),
    focusRequester: FocusRequester = FocusRequester(),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(8.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            singleLine = true,
            value = searchTerm,
            textStyle = TextStyle.Default.copy(color = LocalKuteColors.current.searchBar.textColor),
            cursorBrush = SolidColor(LocalKuteColors.current.searchBar.textColor),
            onValueChange = onSearchTermChanged,
            decorationBox = { innerTextField ->
                Card(
                    modifier = Modifier.defaultMinSize(minHeight = 64.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LocalKuteColors.current.searchBar.backgroundColor,
                    ),
                    shape = RoundedCornerShape(percent = 100),
                    elevation = CardDefaults.cardElevation(LocalKuteColors.current.searchBar.elevation),
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(
                                start = 16.dp,
                                end = 8.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val verticalPadding = 8.dp

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .padding(vertical = verticalPadding),
                                text = if (searchTerm.isEmpty()) label else "",
                                color = LocalKuteColors.current.searchBar.hintColor,
                            )

                            Icon(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(100))
                                    .clickable {
                                        if (searchTerm.isNotBlank()) {
                                            onClearSearchTerm()
                                        } else {
                                            onSearchClicked()
                                        }
                                    }
                                    .padding(2.dp),
                                imageVector = when {
                                    searchTerm.isEmpty() -> Icons.Default.Search
                                    else -> Icons.Default.Clear
                                },
                                contentDescription = null,
                                tint = LocalKuteColors.current.searchBar.iconColor,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(vertical = verticalPadding)
                                .padding(end = 56.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            innerTextField()
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KutePreferenceSearchPreview() {
    var searchTerm by remember { mutableStateOf("") }

    KutePreferenceSearch(
        searchTerm = searchTerm,
        onSearchClicked = {},
        onSearchTermChanged = {},
        onClearSearchTerm = { searchTerm = "" }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KutePreferenceSearchWithInputPreview() {
    var searchTerm by remember { mutableStateOf("") }

    KutePreferenceSearch(
        searchTerm = searchTerm,
        onSearchClicked = {},
        onSearchTermChanged = {},
        onClearSearchTerm = { searchTerm = "" }
    )
}
