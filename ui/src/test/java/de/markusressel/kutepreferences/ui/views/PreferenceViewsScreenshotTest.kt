package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.DecimalSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberRangeSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.PasswordPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import de.markusressel.kutepreferences.ui.views.listitems.UrlPreference
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
@Config(qualifiers = "w400dp-h700dp-xhdpi")
class PreferenceViewsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()

    private fun screenshot(name: String, expand: String? = null, content: @Composable () -> Unit) {
        composeRule.setContent {
            KutePreferencesTheme {
                Surface { Column { content() } }
            }
        }
        expand?.let { composeRule.onNodeWithText(it).performClick() }
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
}
