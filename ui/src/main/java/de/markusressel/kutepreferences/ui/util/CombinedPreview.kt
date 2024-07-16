package de.markusressel.kutepreferences.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
annotation class CombinedPreview