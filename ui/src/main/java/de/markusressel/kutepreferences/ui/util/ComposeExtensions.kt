package de.markusressel.kutepreferences.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import de.markusressel.kutepreferences.core.preference.category.delayMillis
import de.markusressel.kutepreferences.core.preference.category.durationMillis
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors

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


fun Modifier.highlightingShimmer() = composed {
    val highlightingShimmerTheme = defaultShimmerTheme.copy(
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
                delayMillis = delayMillis,
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