package de.markusressel.kutepreferences.demo.domain

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord
import de.markusressel.kutepreferences.ui.views.dummy

class CustomPreference(
    override val key: Int,
    override val onClick: (() -> Unit)?,
    override val onLongClick: (() -> Unit)?,
    override val icon: Drawable? = null,
    override val title: String,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null
) : KutePreferenceItem<Int>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Int {
        return 0
    }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

    override fun createDescription(currentValue: Int): String {
        return "The current color is: ${Color(currentValue).toHexString()}"
    }

    private fun Color.toHexString(): String {
        val a = (alpha * 255).toInt().toString(16).padStart(2, '0')
        val r = (red * 255).toInt().toString(16).padStart(2, '0')
        val g = (green * 255).toInt().toString(16).padStart(2, '0')
        val b = (blue * 255).toInt().toString(16).padStart(2, '0')

        return "#$a$r$g$b"
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CustomPreferencePreview() {
    CustomPreferenceView(
        preferenceItem = CustomPreference(
            key = 0,
            onClick = {},
            onLongClick = {},
            dataProvider = dummy,
            title = "Custom Preference"
        )
    )
}

@Composable
fun CustomPreferenceView(
    preferenceItem: CustomPreference,
) {
    var currentColorIndex by remember { mutableStateOf(0) }
    val colors = remember {
        listOf(
            Color.Red, Color.Green, Color.Blue,
            Color.Black, Color.White,
        )
    }

    val color by remember(currentColorIndex) {
        derivedStateOf { colors[currentColorIndex] }
    }

    val textColor by remember(color) {
        derivedStateOf {
            val whiteContrast = ColorUtils.calculateContrast(color.toArgb(), Color.White.toArgb())
            val blackContrast = ColorUtils.calculateContrast(color.toArgb(), Color.Black.toArgb())

            when {
                whiteContrast > blackContrast -> Color.White
                else -> Color.Black
            }
        }
    }

    Box(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .clickable {
                currentColorIndex = colors.indices
                    .filter { it != currentColorIndex }
                    .random()
            }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Custom Preference",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = "This preference will change color every time you touch it! ${preferenceItem.createDescription(color.toArgb())}",
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}