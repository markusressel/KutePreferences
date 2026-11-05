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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.KutePreferencesScreenDefaults
import de.markusressel.kutepreferences.ui.views.KuteCategoryHeader
import de.markusressel.kutepreferences.ui.views.KuteOverview
import de.markusressel.kutepreferences.ui.views.LocalKuteRegistry
import de.markusressel.kutepreferences.ui.views.listitems.CategorySelection
import de.markusressel.kutepreferences.ui.views.listitems.LocalCategorySelection
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import de.markusressel.kutepreferences.ui.vm.UiState

/**
 * The preferences of the given [kuteViewModel], with search and navigation between categories.
 *
 * @param categoryHeader shows the open categories, ordered from root to leaf. It is only shown inside of a category.
 * Pass an empty lambda if your app shows this in its own top bar, using `UiState.categoryPath`.
 * @param maxContentWidth keeps the content readable on large screens by centering it at this width.
 * Pass [Dp.Infinity] to always use the full width.
 * @param twoPaneMinWidth shows the top level next to the open category from this width on, for tablets and unfolded
 * foldables. A foldable that is half open also does this, so that no content crosses its hinge. This only applies to
 * preferences that have categories. Pass [Dp.Infinity] to always show a single page.
 */
@Composable
fun KutePreferencesScreen(
    modifier: Modifier = Modifier,
    kuteViewModel: KutePreferencesViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    maxContentWidth: Dp = KutePreferencesScreenDefaults.MaxContentWidth,
    twoPaneMinWidth: Dp = KutePreferencesScreenDefaults.TwoPaneMinWidth,
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
        var layoutLeftPx by remember { mutableFloatStateOf(0f) }

        BoxWithConstraints(
            modifier = modifier.onGloballyPositioned { layoutLeftPx = it.positionInWindow().x },
        ) {
            val panes = rememberTwoPaneLayout(
                items = uiState.rootItems,
                width = maxWidth,
                twoPaneMinWidth = twoPaneMinWidth,
                layoutLeftPx = layoutLeftPx,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                    // NOTE: an active search covers the whole screen, and only centers its results
                    modifier = when {
                        uiState.isSearchActive -> Modifier
                        // NOTE: above two panes it lines up with both of them
                        panes != null -> Modifier
                        else -> Modifier.widthIn(max = maxContentWidth)
                    }
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
                    maxContentWidth = maxContentWidth,
                )

                val pageModifier = Modifier.graphicsLayer {
                    val scale = 1f - (animatedBackProgress * BACK_GESTURE_MAX_SHRINK)
                    scaleX = scale
                    scaleY = scale
                    alpha = 1f - (animatedBackProgress * BACK_GESTURE_MAX_FADE)
                }

                when (panes) {
                    null -> {
                        CategoryHeaderSlot(
                            modifier = Modifier.widthIn(max = maxContentWidth),
                            path = uiState.categoryPath,
                            categoryHeader = categoryHeader,
                        )
                        CategoryPages(
                            modifier = pageModifier,
                            uiState = uiState,
                            contentPadding = contentPadding,
                            maxContentWidth = maxContentWidth,
                            hasEmptyTopLevel = false,
                        )
                    }

                    else -> TwoPanes(
                        panes = panes,
                        uiState = uiState,
                        contentPadding = contentPadding,
                        maxContentWidth = maxContentWidth,
                        detailModifier = pageModifier,
                        categoryHeader = categoryHeader,
                        onSelectCategory = { key -> kuteViewModel.navigator.setCategories(listOf(key)) },
                    )
                }
            }
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

@Composable
private fun CategoryHeaderSlot(
    path: List<KutePreferenceCategory>,
    categoryHeader: @Composable (path: List<KutePreferenceCategory>) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = path,
        contentKey = { categories -> categories.map { it.key } },
        transitionSpec = {
            fadeIn(tween(NAVIGATION_ANIMATION_MILLIS)) togetherWith
                fadeOut(tween(NAVIGATION_ANIMATION_MILLIS)) using SizeTransform(clip = false)
        },
        label = "CategoryHeader",
    ) { categories ->
        if (categories.isNotEmpty()) {
            categoryHeader(categories)
        }
    }
}

/**
 * Shows the top level next to the open category
 */
@Composable
private fun TwoPanes(
    panes: TwoPaneLayout,
    uiState: UiState,
    contentPadding: PaddingValues,
    maxContentWidth: Dp,
    detailModifier: Modifier,
    categoryHeader: @Composable (path: List<KutePreferenceCategory>) -> Unit,
    onSelectCategory: (String) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    Row(modifier = Modifier.fillMaxWidth()) {
        CompositionLocalProvider(
            LocalCategorySelection provides CategorySelection(
                selectedKey = uiState.categoryPath.firstOrNull()?.key,
                onSelect = onSelectCategory,
            ),
        ) {
            KuteOverview(
                modifier = Modifier
                    .width(panes.listWidth)
                    .padding(
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        bottom = contentPadding.calculateBottomPadding(),
                    )
                    .imePadding()
                    .padding(top = 8.dp),
                items = uiState.rootItems,
                scrollState = rememberScrollState(),
            )
        }

        Column(
            modifier = Modifier
                .padding(start = panes.gap)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CategoryHeaderSlot(
                modifier = Modifier.widthIn(max = maxContentWidth),
                path = uiState.categoryPath,
                categoryHeader = categoryHeader,
            )
            CategoryPages(
                modifier = detailModifier,
                uiState = uiState,
                contentPadding = PaddingValues(
                    end = contentPadding.calculateEndPadding(layoutDirection),
                    bottom = contentPadding.calculateBottomPadding(),
                ),
                maxContentWidth = maxContentWidth,
                hasEmptyTopLevel = true,
            )
        }
    }
}

@Composable
private fun NoCategorySelected() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        text = stringResource(R.string.kute_preferences_no_category_selected),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}

/**
 * @return the panes to show, or null for a single page. A foldable only has a hinge that divides the window while it
 * is half open.
 */
@Composable
private fun rememberTwoPaneLayout(
    items: List<KutePreferenceListItem>,
    width: Dp,
    twoPaneMinWidth: Dp,
    layoutLeftPx: Float,
): TwoPaneLayout? {
    val hasCategories = remember(items) { items.hasCategories() }
    val windowHinge = currentWindowAdaptiveInfo().windowPosture.hingeList
        .firstOrNull { it.isVertical && it.isSeparating }
    val layoutDirection = LocalLayoutDirection.current

    return with(LocalDensity.current) {
        val hinge = windowHinge?.let { WindowHinge(leftPx = it.bounds.left, rightPx = it.bounds.right) }
            ?.relativeTo(
                layoutLeftPx = layoutLeftPx,
                layoutWidthPx = width.toPx(),
                layoutDirection = layoutDirection,
            )
            ?.let { it.start.toDp()..it.endInclusive.toDp() }

        when {
            hasCategories -> twoPaneLayout(width = width, twoPaneMinWidth = twoPaneMinWidth, hinge = hinge)
            else -> null
        }
    }
}

/**
 * @return true if any of the items is a category, or a section that contains one
 */
private fun List<KutePreferenceListItem>.hasCategories(): Boolean = any { item ->
    when (item) {
        is KutePreferenceCategory -> true
        is KuteParent -> item.children.hasCategories()
        else -> false
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
    maxContentWidth: Dp,
    hasEmptyTopLevel: Boolean,
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
            when {
                hasEmptyTopLevel && state.currentCategory == null -> NoCategorySelected()
                else -> KuteOverview(
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
                    maxContentWidth = maxContentWidth,
                )
            }
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
