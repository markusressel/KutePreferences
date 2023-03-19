package de.markusressel.kutepreferences.app.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import de.markusressel.kutepreferences.app.ui.theme.KutePreferencesDemoAppTheme
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.KutePreferencesScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            if (vm.onBackPressed().not()) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KutePreferencesDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KutePreferencesTheme {
                        val currentItems by vm.currentPreferenceItems.collectAsState(initial = emptyList())

                        KutePreferencesScreen(
                            modifier = Modifier.fillMaxSize(),
                            items = currentItems,
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DefaultPreview() {
    KutePreferencesDemoAppTheme {
        KutePreferencesTheme {
            KutePreferencesScreen(
                items = listOf(
                    KuteCategory(
                        key = 1,
                        title = "hello",
                        description = "test",
                    ),
                )
            )
        }
    }
}