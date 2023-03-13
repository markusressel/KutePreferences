package de.markusressel.kutepreferences.ui.views.common

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MarkerPreview() {
    Marker(
        modifier = Modifier.padding(8.dp),
        text = "100"
    )
}

@Composable
fun Marker(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White,
    elevation: Dp = 4.dp,
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .shadow(
                elevation = elevation,
                shape = DefaultMarkerShape
            )
            .clip(DefaultMarkerShape)
            .background(backgroundColor)
            .animateContentSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 4.dp,
                start = 2.dp,
                end = 2.dp
            ),
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

val DefaultMarkerShape = MarkerShape()

class MarkerShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val triangleHeight = 10
        val triangleWidth = 14

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - triangleHeight)
            lineTo(size.width / 2f + triangleWidth / 2f, size.height - triangleHeight)
            lineTo(size.width / 2f, size.height)
            lineTo(size.width / 2f - triangleWidth / 2f, size.height - triangleHeight)
            lineTo(0f, size.height - triangleHeight)
            lineTo(0f, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}
