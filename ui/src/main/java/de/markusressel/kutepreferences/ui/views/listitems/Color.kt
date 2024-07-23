package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.toColorInt
import de.markusressel.kutepreferences.core.preference.color.ColorPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun ColorPreferenceView(
    behavior: ColorPreferenceBehavior,
) {
    val uiState by behavior.uiState.collectAsState()

    var collapsed by remember { mutableStateOf(true) }

    val persistedValue by behavior.persistedValue.collectAsState()

    InlineExpandablePreferenceView(
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        header = {
            Row(
                modifier = Modifier
                    .modifyIf(uiState.shimmering) {
                        highlightingShimmer()
                    }
                    .padding(end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DefaultPreferenceListItemCardContent(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    icon = behavior.preferenceItem.icon,
                    title = behavior.preferenceItem.title,
                    subtitle = behavior.preferenceItem.createDescription(persistedValue),
                )

                ColorCircle(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp),
                    fillColor = Color(persistedValue),
                    outlineColor = MaterialTheme.colorScheme.secondary,
                    onClick = { collapsed = collapsed.not() }
                )
            }
        },
        content = { ColorPreferenceEditView(behavior) },
        collapsed = collapsed,
        onToggleCollapsedState = { collapsed = collapsed.not() }
    )
}

@CombinedPreview
@Composable
private fun ColorPreferenceEditViewPreview() {
    KutePreferencesTheme {
        ColorPreferenceEditView(
            behavior = ColorPreferenceBehavior(
                preferenceItem = KuteColorPreference(
                    key = 0,
                    title = "Color Preference",
                    defaultValue = Color.Red.copy(alpha = 0.5f).toArgb(),
                    dataProvider = dummy,
                )
            )
        )
    }
}

@Composable
private fun ColorPreferenceEditView(behavior: ColorPreferenceBehavior) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp)
    ) {
        val currentValue by remember { mutableIntStateOf(behavior.persistedValue.value) }
        ClassicColorPicker(modifier = Modifier.height(200.dp),
            color = HsvColor.from(color = Color(currentValue)),
            onColorChanged = { color: HsvColor ->
                behavior.onInputChanged(color.toColorInt())
                behavior.persistCurrentValue()
            }
        )
    }
}

@CombinedPreview
@Composable
private fun ColorPreferencePreview() {
    KutePreferencesTheme {
        ColorPreferenceView(
            behavior = ColorPreferenceBehavior(
                preferenceItem = KuteColorPreference(
                    key = 0,
                    title = "Color Preference",
                    defaultValue = Color.Red.copy(alpha = 0.5f).toArgb(),
                    dataProvider = dummy,
                )
            )
        )
    }
}

@Composable
private fun ColorCircle(
    modifier: Modifier = Modifier,
    fillColor: Color,
    outlineColor: Color,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = fillColor,
        ),
        shape = CircleShape,
        onClick = { onClick() }
    ) {
        CheckerBoard(
            modifier = Modifier.border(
                width = 1.dp,
                color = outlineColor,
                shape = CircleShape
            ),
            alpha = 1f - fillColor.alpha,
        )
    }
}

@Composable
private fun CheckerBoard(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    Column(modifier) {
        for (i in 6 downTo 0) {
            Row {
                for (j in 6 downTo 0) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                when {
                                    (i + j) % 2 == 0 -> Color.White
                                    else -> Color.LightGray
                                }.copy(alpha = alpha)
                            )
                    )
                }
            }
        }
    }
}

@CombinedPreview
@Composable
private fun ColorCirclePreview() {
    KutePreferencesTheme {
        ColorCircle(
            modifier = Modifier
                .size(48.dp),
            fillColor = Color.Red.copy(alpha = 0.5f),
            outlineColor = Color.Yellow,
        )
    }
}
