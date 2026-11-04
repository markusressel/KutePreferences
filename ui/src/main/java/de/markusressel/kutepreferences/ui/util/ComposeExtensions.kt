package de.markusressel.kutepreferences.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.*
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors
import kotlinx.coroutines.delay

/**
 * @return the [KutePreferenceListItem.onClick] override of this item if it has one, otherwise [default]
 */
internal fun KutePreferenceListItem.clickAction(default: () -> Unit): () -> Unit = onClick ?: default

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return when {
        condition -> modifier()
        else -> this
    }
}

fun Modifier.modifyIf(predicate: () -> Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return when {
        predicate() -> modifier()
        else -> this
    }
}

private const val SHIMMER_DURATION_MILLIS = 800
private const val SHIMMER_DELAY_MILLIS = 300
private const val SHIMMER_ITERATIONS = 2
private const val SHIMMER_LENGTH_MILLIS = (SHIMMER_DURATION_MILLIS + SHIMMER_DELAY_MILLIS) * SHIMMER_ITERATIONS

/**
 * While [active] this scrolls the element into view and plays the highlight animation,
 * and calls [onFinished] when the animation is over.
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.highlightingShimmer(active: Boolean, onFinished: () -> Unit) = composed {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(active) {
        if (active) {
            bringIntoViewRequester.bringIntoView()
            delay(SHIMMER_LENGTH_MILLIS.toLong())
            onFinished()
        }
    }

    bringIntoViewRequester(bringIntoViewRequester)
        .modifyIf(active) { highlightingShimmer() }
}

fun Modifier.highlightingShimmer() = composed {
    val highlightingShimmerTheme = defaultShimmerTheme.copy(
        animationSpec = infiniteRepeatable(
            animation = shimmerSpec(
                durationMillis = SHIMMER_DURATION_MILLIS,
                easing = LinearEasing,
                delayMillis = SHIMMER_DELAY_MILLIS,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        blendMode = BlendMode.Overlay,
        shaderColors = listOf(
            Color.Unspecified.copy(alpha = 0.00f),
            LocalKuteColors.current.search.shimmerColor.copy(alpha = 0.75f),
            LocalKuteColors.current.search.shimmerColor.copy(alpha = 0f),
            LocalKuteColors.current.search.shimmerColor.copy(alpha = 0.75f),
            Color.Unspecified.copy(alpha = 0.00f),
        ),
        shaderColorStops = listOf(
            0.0f,
            0.3f,
            0.5f,
            0.8f,
            1.0f,
        ),
        shimmerWidth = 600.dp,
    )
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = highlightingShimmerTheme,
    )
    shimmer(customShimmer = shimmer)
}

@Preview
@Composable
private fun ShimmerPreview() {
    KutePreferencesTheme {
        Card(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .highlightingShimmer()
                    .padding(16.dp)
            ) {
                Text(text = "Hello World!")
            }
        }
    }
}