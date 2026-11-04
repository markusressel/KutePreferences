package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.DecimalSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberSliderPreference
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SliderRangeLabelTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `a float slider shows the decimals of its range`() {
        val behavior = FloatSliderPreferenceBehavior(
            KuteFloatSliderPreference(
                key = "float",
                title = "Slider",
                minimum = 0.5f,
                maximum = 1.5f,
                defaultValue = 1f,
                dataProvider = DummyDataProvider(),
            )
        )
        composeRule.setContent { KutePreferencesTheme { DecimalSliderPreference(behavior) } }
        composeRule.onNodeWithText("Slider").performClick()

        composeRule.onNodeWithText("0.50").assertIsDisplayed()
        composeRule.onNodeWithText("1.50").assertIsDisplayed()
    }

    @Test
    fun `an int slider shows whole numbers`() {
        val behavior = IntSliderPreferenceBehavior(
            KuteIntSliderPreference(
                key = "int",
                title = "Slider",
                minimum = 2,
                maximum = 7,
                defaultValue = 4,
                dataProvider = DummyDataProvider(),
            )
        )
        composeRule.setContent { KutePreferencesTheme { NumberSliderPreference(behavior) } }
        composeRule.onNodeWithText("Slider").performClick()

        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("7").assertIsDisplayed()
    }

}
