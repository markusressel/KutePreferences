package de.markusressel.kutepreferences.demo.ui

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.demo.ui.theme.KutePreferencesDemoAppTheme
import de.markusressel.kutepreferences.ui.theme.KutePreferencesDialogDefaults
import de.markusressel.kutepreferences.ui.theme.KutePreferencesSearchBarDefaults
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.KutePreferencesThemeDefaults
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel

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
                    KutePreferencesTheme(
                        colors = KutePreferencesThemeDefaults.defaultColors.copy(
                            searchBar = KutePreferencesSearchBarDefaults.defaultTheme.copy(
                                textColor = Color.Red
                            ),
                            dialog = KutePreferencesDialogDefaults.defaultTheme.copy(
                                backgroundColor = Color.Red,
                                buttonTextColor = Color.Red,
                            ),
                        )
                    ) {
                        KutePreferencesScreen(
                            modifier = Modifier.fillMaxSize(),
                            kuteViewModel = vm,
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
    val vm by remember {
        derivedStateOf {
            val navigator = DefaultKuteNavigator()
            KutePreferencesViewModel(navigator).apply {
                initPreferencesTree(
                    listOf(
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

    KutePreferencesDemoAppTheme {
        KutePreferencesTheme {
            KutePreferencesScreen(
                kuteViewModel = vm,
            )
        }
    }
}