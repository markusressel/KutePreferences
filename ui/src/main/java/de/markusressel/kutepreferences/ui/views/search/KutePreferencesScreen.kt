package de.markusressel.kutepreferences.ui.views.search

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.ui.views.KuteCategoryHeader
import de.markusressel.kutepreferences.ui.views.KuteOverview
import de.markusressel.kutepreferences.ui.views.LocalKuteRegistry
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import de.markusressel.kutepreferences.ui.vm.UiState

/**
 * The preferences of the given [kuteViewModel], with search and navigation between categories.
 *
 * @param categoryHeader shows the open categories, ordered from root to leaf. It is only shown inside of a category.
 * Pass an empty lambda if your app shows this in its own top bar, using `UiState.categoryPath`.
 */
@Composable
fun KutePreferencesScreen(
    modifier: Modifier = Modifier,
    kuteViewModel: KutePreferencesViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    categoryHeader: @Composable (path: List<KutePreferenceCategory>) -> Unit = { path ->
        KuteCategoryHeader(
            path = path,
            onBackClicked = { kuteViewModel.onUiEvent(KuteUiEvent.BackPressed) },
        )
    },
) {
    val uiState by kuteViewModel.preferencesUiState.collectAsStateWithLifecycle()

    val searchFocusRequester = remember { FocusRequester() }

    RestoreOpenCategories(kuteViewModel)

    var backProgress by remember { mutableFloatStateOf(0f) }
    val animatedBackProgress by animateFloatAsState(targetValue = backProgress, label = "BackProgress")

    CompositionLocalProvider(LocalKuteRegistry provides kuteViewModel.registry) {
        Column(modifier) {
            PredictiveBackHandler(enabled = uiState.isSearchActive || uiState.currentCategory != null) { progress ->
                try {
                    progress.collect { event ->
                        backProgress = when {
                            uiState.isSearchActive -> 0f
                            else -> event.progress
                        }
                    }
                    searchFocusRequester.freeFocus()
                    kuteViewModel.onUiEvent(KuteUiEvent.BackPressed)
                } finally {
                    backProgress = 0f
                }
            }

            // NOTE: a window gives initial focus to its first focusable, which must not be the
            // search bar, because that would open the search and the keyboard on launch
            Spacer(modifier = Modifier.focusable())

            KuteSearch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                active = uiState.isSearchActive,
                onActiveChange = { newActive ->
                    when {
                        newActive -> kuteViewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
                        else -> kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch)
                    }
                },
                searchTerm = uiState.searchTerm,
                onSearchTermChanged = {
                    kuteViewModel.onUiEvent(KuteUiEvent.SearchTermChanged(it))
                },
                items = uiState.searchResults,
                searchFocusRequester = searchFocusRequester,
                onCancelSearch = { kuteViewModel.onUiEvent(KuteUiEvent.CloseSearch) },
                onSearchResultSelected = { kuteViewModel.onUiEvent(KuteUiEvent.SearchResultSelected(it)) },
            )

            AnimatedContent(
                targetState = uiState.categoryPath,
                contentKey = { path -> path.map { it.key } },
                transitionSpec = {
                    fadeIn(tween(NAVIGATION_ANIMATION_MILLIS)) togetherWith
                        fadeOut(tween(NAVIGATION_ANIMATION_MILLIS)) using SizeTransform(clip = false)
                },
                label = "CategoryHeader",
            ) { path ->
                if (path.isNotEmpty()) {
                    categoryHeader(path)
                }
            }

            CategoryPages(
                modifier = Modifier
                    .graphicsLayer {
                        val scale = 1f - (animatedBackProgress * BACK_GESTURE_MAX_SHRINK)
                        scaleX = scale
                        scaleY = scale
                        alpha = 1f - (animatedBackProgress * BACK_GESTURE_MAX_FADE)
                    },
                uiState = uiState,
                contentPadding = contentPadding,
            )
        }
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

/**
 * Keeps the open categories across the death of the process, which also ends the ViewModel.
 */
@Composable
private fun RestoreOpenCategories(viewModel: KutePreferencesViewModel) {
    var openCategories by rememberSaveable { mutableStateOf(ArrayList<String>()) }

    LaunchedEffect(viewModel) {
        viewModel.restoreCategories(openCategories)
        viewModel.navigator.currentCategory.collect {
            openCategories = ArrayList(viewModel.navigator.categoryStack)
        }
    }
}

/**
 * Animates between categories. Each open category keeps its scroll position, so going back
 * returns to the same place, while a category that was left starts at the top the next time.
 */
@Composable
private fun CategoryPages(
    uiState: UiState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val pageStates = rememberSaveableStateHolder()

    val openKeys = uiState.categoryPath.map { it.key }
    var previousKeys by remember { mutableStateOf(openKeys) }
    LaunchedEffect(openKeys) {
        (previousKeys - openKeys.toSet()).forEach { pageStates.removeState(it) }
        previousKeys = openKeys
    }

    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = uiState,
        contentKey = { it.currentCategory },
        transitionSpec = { categoryTransition() },
        label = "CategoryPages",
    ) { state ->
        pageStates.SaveableStateProvider(key = state.currentCategory ?: TOP_LEVEL_PAGE_KEY) {
            KuteOverview(
                modifier = Modifier
                    .padding(
                        start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = contentPadding.calculateBottomPadding(),
                    )
                    .consumeWindowInsets(contentPadding)
                    .imePadding()
                    .padding(top = 8.dp),
                items = state.preferenceItems,
                scrollState = rememberScrollState(),
            )
        }
    }
}

/**
 * Slides towards the end when entering a category and back when leaving it. Jumps to an
 * unrelated category of the same depth, which only search results can do, have no direction.
 */
private fun AnimatedContentTransitionScope<UiState>.categoryTransition(): ContentTransform {
    val spec = tween<Float>(NAVIGATION_ANIMATION_MILLIS)
    val depthChange = targetState.categoryPath.size.compareTo(initialState.categoryPath.size)
    val direction = when {
        depthChange > 0 -> AnimatedContentTransitionScope.SlideDirection.Start
        depthChange < 0 -> AnimatedContentTransitionScope.SlideDirection.End
        else -> null
    }

    return when (direction) {
        null -> fadeIn(spec) togetherWith fadeOut(spec)
        else -> {
            val slideSpec = tween<IntOffset>(NAVIGATION_ANIMATION_MILLIS)
            val enter = slideIntoContainer(direction, slideSpec, initialOffset = { it / SLIDE_OFFSET_DIVISOR })
            val exit = slideOutOfContainer(direction, slideSpec, targetOffset = { it / SLIDE_OFFSET_DIVISOR })
            (enter + fadeIn(spec)) togetherWith (exit + fadeOut(spec))
        }
    }
}

private const val TOP_LEVEL_PAGE_KEY = "de.markusressel.kutepreferences.top_level"
private const val NAVIGATION_ANIMATION_MILLIS = 250
private const val SLIDE_OFFSET_DIVISOR = 4
private const val BACK_GESTURE_MAX_SHRINK = 0.08f
private const val BACK_GESTURE_MAX_FADE = 0.3f
