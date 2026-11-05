package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.asCondition
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.IntRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.select.MultiSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.select.SingleSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.DecimalSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.IntRangeSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.MultiSelectionPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberRangeSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.PasswordPreference
import de.markusressel.kutepreferences.ui.views.listitems.SingleSelectionPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import de.markusressel.kutepreferences.ui.views.listitems.UrlPreference
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Compare with `./gradlew :ui:verifyRoborazziDebug`, update with `./gradlew :ui:recordRoborazziDebug`.
 */
@OptIn(ExperimentalRoborazziApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// NOTE: native graphics require API 26, and without a target SDK Robolectric uses the minimum SDK. On an
// older API the content is not drawn at all, and the screenshots are blank without any test failing.
@Config(sdk = [34], qualifiers = "w400dp-h700dp-xhdpi")
class PreferenceViewsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()

    private fun screenshot(
        name: String,
        expand: String? = null,
        action: () -> Unit = {},
        content: @Composable () -> Unit,
    ) {
        composeRule.setContent {
            KutePreferencesTheme {
                Surface { Column { content() } }
            }
        }
        expand?.let { composeRule.onNodeWithText(it).performClick() }
        action()
        composeRule.waitForIdle()
        // includes dialogs, which are separate windows
        captureScreenRoboImage("src/test/snapshots/$name.png")
    }

    @Test
    fun intSlider() = screenshot("int_slider", expand = "Int slider") {
        NumberSliderPreference(
            IntSliderPreferenceBehavior(
                KuteIntSliderPreference(
                    key = "int",
                    title = "Int slider",
                    minimum = -10,
                    maximum = 10,
                    defaultValue = 5,
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun intSliderAtMinimum() = screenshot("int_slider_minimum", expand = "Int slider") {
        NumberSliderPreference(
            IntSliderPreferenceBehavior(
                KuteIntSliderPreference(
                    key = "int",
                    title = "Int slider",
                    minimum = 0,
                    maximum = 7,
                    defaultValue = 0,
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun intSliderAtMaximum() = screenshot("int_slider_maximum", expand = "Int slider") {
        NumberSliderPreference(
            IntSliderPreferenceBehavior(
                KuteIntSliderPreference(
                    key = "int",
                    title = "Int slider",
                    minimum = 0,
                    maximum = 7,
                    defaultValue = 7,
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun floatSlider() = screenshot("float_slider", expand = "Float slider") {
        DecimalSliderPreference(
            FloatSliderPreferenceBehavior(
                KuteFloatSliderPreference(
                    key = "float",
                    title = "Float slider",
                    minimum = -10f,
                    maximum = 10f,
                    defaultValue = 1.25f,
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun rangeSlider() = screenshot("range_slider", expand = "Range slider") {
        NumberRangeSliderPreference(
            FloatRangeSliderPreferenceBehavior(
                KuteFloatRangePreference(
                    key = "range",
                    title = "Range slider",
                    minimum = 0f,
                    maximum = 10f,
                    defaultValue = RangePersistenceModel(2f, 5f),
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun intRangeSlider() = screenshot("int_range_slider", expand = "Int range") {
        IntRangeSliderPreference(
            IntRangeSliderPreferenceBehavior(
                KuteIntRangePreference(
                    key = "int_range",
                    title = "Int range",
                    minimum = 0,
                    maximum = 10,
                    defaultValue = RangePersistenceModel(2, 5),
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun floatRangeSliderWithSteps() = screenshot("float_range_slider_steps", expand = "Float range") {
        NumberRangeSliderPreference(
            FloatRangeSliderPreferenceBehavior(
                KuteFloatRangePreference(
                    key = "float_range",
                    title = "Float range",
                    minimum = 0f,
                    maximum = 1f,
                    steps = 3,
                    decimalPlaces = 2,
                    defaultValue = RangePersistenceModel(0.25f, 0.75f),
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun rangeSliderDragged() = screenshot(
        name = "range_slider_dragged",
        expand = "Range slider",
        action = {
            composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))[1]
                .performTouchInput {
                    down(center)
                    moveBy(Offset(x = 100f, y = 0f))
                    moveBy(Offset(x = 100f, y = 0f))
                }
        },
    ) {
        NumberRangeSliderPreference(
            FloatRangeSliderPreferenceBehavior(
                KuteFloatRangePreference(
                    key = "range",
                    title = "Range slider",
                    minimum = 0f,
                    maximum = 10f,
                    defaultValue = RangePersistenceModel(2f, 5f),
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun text() = screenshot("text") {
        TextPreference(
            TextPreferenceBehavior(
                KuteTextPreference(key = "t", title = "Text", defaultValue = "value", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun textDialog() = screenshot("text_dialog", expand = "Text") {
        TextPreference(
            TextPreferenceBehavior(
                KuteTextPreference(key = "t", title = "Text", defaultValue = "value", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun passwordDialog() = screenshot("password_dialog", expand = "Password") {
        PasswordPreference(
            PasswordPreferenceBehavior(
                KutePasswordPreference(key = "p", title = "Password", defaultValue = "secret123", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun passwordDialogRevealed() = screenshot(
        name = "password_dialog_revealed",
        expand = "Password",
        action = { composeRule.onNodeWithContentDescription("Show password").performClick() },
    ) {
        PasswordPreference(
            PasswordPreferenceBehavior(
                KutePasswordPreference(key = "p", title = "Password", defaultValue = "secret123", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun passwordDialogWithError() = screenshot("password_dialog_error", expand = "Password") {
        PasswordPreference(
            PasswordPreferenceBehavior(
                KutePasswordPreference(key = "p", title = "Password", defaultValue = "short", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun urlDialog() = screenshot("url_dialog", expand = "Url") {
        UrlPreference(
            UrlPreferenceBehavior(
                KuteUrlPreference(key = "u", title = "Url", defaultValue = "https://example.com", dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun numberDialog() = screenshot("number_dialog", expand = "Number") {
        NumberPreference(
            NumberPreferenceBehavior(
                KuteNumberPreference(key = "n", title = "Number", defaultValue = 42, dataProvider = dataProvider)
            )
        )
    }

    @Test
    fun `category screen`() {
        val leaf = KuteCategory(
            key = "leaf",
            title = "Proxy",
            children = listOf(
                KuteBooleanPreference(key = "1", title = "Use a proxy", defaultValue = true, dataProvider = dataProvider),
            ),
        )
        val parent = KuteCategory(key = "parent", title = "Network", children = listOf(leaf))
        val viewModel = KutePreferencesViewModel().apply {
            initPreferencesTree(listOf(parent))
            navigator.setCategories(listOf("parent", "leaf"))
        }

        screenshot("category_screen") {
            KutePreferencesScreen(kuteViewModel = viewModel)
        }
    }

    @Test
    fun `disabled item`() = screenshot("disabled_item") {
        KuteRegistryForPreview {
        val disabled = MutableStateFlow(false).asCondition()
        TextPreference(
            TextPreferenceBehavior(
                KuteTextPreference(key = "e", title = "Enabled", defaultValue = "value", dataProvider = dataProvider)
            )
        )
        LocalKuteRegistry.current.Content(
            KuteTextPreference(
                key = "d",
                title = "Disabled",
                defaultValue = "value",
                dataProvider = dataProvider,
                enabledWhen = disabled,
            )
        )
        }
    }

    @Test
    fun `choice dialog`() = screenshot("choice_dialog", expand = "Choice") {
        SingleSelectionPreference(
            SingleSelectionPreferenceBehavior(
                KuteChoicePreference(
                    key = "c",
                    title = "Choice",
                    defaultValue = "b",
                    options = mapOf("a" to "First", "b" to "Second"),
                    dataProvider = dataProvider,
                )
            )
        )
    }

    @Test
    fun `multi choice dialog`() = screenshot("multi_choice_dialog", expand = "Multi choice") {
        MultiSelectionPreference(
            MultiSelectionPreferenceBehavior(
                KuteMultiChoicePreference(
                    key = "m",
                    title = "Multi choice",
                    defaultValue = setOf("a", "c"),
                    options = mapOf("a" to "First", "b" to "Second", "c" to "Third"),
                    dataProvider = dataProvider,
                )
            )
        )
    }

}
