package de.markusressel.kutepreferences.ui.views.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.KuteStyleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KutePreferenceSearch(
    modifier: Modifier = Modifier,
    searchTerm: String,
    onSearchTermChanged: (String) -> Unit,
    onCloseSearch: () -> Unit,
    placeholder: String = stringResource(id = R.string.kute_preferences_search_label),
    focusRequester: FocusRequester = FocusRequester(),
    searchContent: @Composable () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
) {
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
                    onCloseSearch()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
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
        onActiveChange = { newActive ->
            if (newActive) {
                onActiveChange(newActive)
            } else {
                onCloseSearch()
            }
        },
        leadingIcon = leadingIcon.takeIf { active },
    ) {
        searchContent()
    }
}


@CombinedPreview
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
            onSearchTermChanged = {},
            onCloseSearch = { searchTerm = "" },
            searchContent = {},
            active = true,
            onActiveChange = { },
        )
    }
}

@CombinedPreview
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
            onSearchTermChanged = {},
            onCloseSearch = { searchTerm = "" },
            searchContent = {},
            active = true,
            onActiveChange = {},
        )
    }
}
