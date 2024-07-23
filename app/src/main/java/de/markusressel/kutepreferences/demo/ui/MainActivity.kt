package de.markusressel.kutepreferences.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.demo.ui.theme.KutePreferencesDemoAppTheme
import de.markusressel.kutepreferences.ui.theme.*
import de.markusressel.kutepreferences.ui.util.CombinedPreview
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { contentPadding ->
                    KutePreferencesTheme(
                        colors = KutePreferencesThemeDefaults.defaultColors.copy(
                            search = KutePreferencesSearchDefaults.defaultTheme.copy(
                                searchBar = KutePreferencesSearchBarDefaults.defaultTheme.copy(
//                                    textColor = Color.Red
                                ),
                            ),
                            dialog = KutePreferencesDialogDefaults.defaultTheme.copy(
//                                backgroundColor = Color.Red,
//                                buttonTextColor = Color.Red,
                            ),
                        )
                    ) {
                        KutePreferencesScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding),
                            kuteViewModel = vm,
                        )
                    }
                }
            }
        }
    }
}

@CombinedPreview
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