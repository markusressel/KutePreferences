package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KutePreferenceSearch(
    modifier: Modifier = Modifier,
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearSearchTerm: () -> Unit,
    placeholder: String = stringResource(id = R.string.kute_preferences_search_label),
    focusRequester: FocusRequester = FocusRequester(),
) {
    var active by remember {
        mutableStateOf(false)
    }

    val leadingIcon = @Composable {
        val visibilityState = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }

        AnimatedVisibility(
            visibleState = visibilityState,
            enter = expandHorizontally(),
            exit = shrinkHorizontally(),
        ) {
            IconButton(
                onClick = {
                    focusRequester.freeFocus()
                    onClearSearchTerm()
                    active = false
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
//        AnimatedContent(targetState = visibilityState, label = "leadingIconAnimation") { targetState ->
//            if (targetState.targetState) {
//                IconButton(
//                    onClick = {
//                        focusRequester.freeFocus()
//                        onClearSearchTerm()
//                        active = false
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onBackground,
//                    )
//                }
//            }
//        }
    }

    SearchBar(
        modifier = Modifier
            .focusRequester(focusRequester)
            .then(modifier),
        placeholder = {
            Text(text = placeholder)
        },
        query = searchTerm,
        onQueryChange = onSearchTermChanged,
        onSearch = onSearchTermChanged,
        active = active,
        onActiveChange = {
            active = it
            if (!it) {
                onClearSearchTerm()
            }
        },
        leadingIcon = leadingIcon.takeIf { active },
    ) {
        Text("Search Content")
    }

//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(64.dp)
//            .padding(8.dp)
//            .then(modifier),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        BasicTextField(
//            modifier = Modifier
//                .weight(1f)
//                .focusRequester(focusRequester),
//            singleLine = true,
//            value = searchTerm,
//            textStyle = TextStyle.Default.copy(color = LocalKuteColors.current.searchBar.textColor),
//            cursorBrush = SolidColor(LocalKuteColors.current.searchBar.textColor),
//            onValueChange = onSearchTermChanged,
//            decorationBox = { innerTextField ->
//                Card(
//                    modifier = Modifier.defaultMinSize(minHeight = 64.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = LocalKuteColors.current.searchBar.backgroundColor,
//                    ),
//                    shape = RoundedCornerShape(percent = 100),
//                    elevation = CardDefaults.cardElevation(LocalKuteColors.current.searchBar.elevation),
//                ) {
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            .padding(
//                                start = 16.dp,
//                                end = 8.dp
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        val verticalPadding = 8.dp
//
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .wrapContentHeight()
//                                    .padding(vertical = verticalPadding),
//                                text = if (searchTerm.isEmpty()) placeholder else "",
//                                color = LocalKuteColors.current.searchBar.hintColor,
//                            )
//
//                            Icon(
//                                modifier = Modifier
//                                    .size(36.dp)
//                                    .clip(RoundedCornerShape(100))
//                                    .clickable {
//                                        if (searchTerm.isNotBlank()) {
//                                            onClearSearchTerm()
//                                        } else {
//                                            onSearchClicked()
//                                        }
//                                    }
//                                    .padding(2.dp),
//                                imageVector = when {
//                                    searchTerm.isEmpty() -> Icons.Default.Search
//                                    else -> Icons.Default.Clear
//                                },
//                                contentDescription = null,
//                                tint = LocalKuteColors.current.searchBar.iconColor,
//                            )
//                        }
//                        Row(
//                            modifier = Modifier
//                                .align(Alignment.CenterStart)
//                                .padding(vertical = verticalPadding)
//                                .padding(end = 56.dp),
//                            verticalAlignment = Alignment.CenterVertically,
//                        ) {
//                            innerTextField()
//                        }
//                    }
//                }
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//        )
//    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KutePreferenceSearchPreview() {
    var searchTerm by remember { mutableStateOf("") }

    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    KutePreferencesTheme {
        KutePreferenceSearch(
            searchTerm = searchTerm,
            onSearchClicked = {},
            onSearchTermChanged = {},
            onClearSearchTerm = { searchTerm = "" }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KutePreferenceSearchWithInputPreview() {
    var searchTerm by remember { mutableStateOf("") }

    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    KutePreferencesTheme {
        KutePreferenceSearch(
            searchTerm = searchTerm,
            onSearchClicked = {},
            onSearchTermChanged = {},
            onClearSearchTerm = { searchTerm = "" }
        )
    }
}
