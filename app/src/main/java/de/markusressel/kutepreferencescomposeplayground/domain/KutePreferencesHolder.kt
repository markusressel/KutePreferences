package de.markusressel.kutepreferencescomposeplayground.domain

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import de.markusressel.kutepreferences.core.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.number.slider.KuteSliderPreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.KuteSingleSelectStringPreference
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import de.markusressel.kutepreferencescomposeplayground.R
import de.markusressel.kutepreferencescomposeplayground.ui.IconHelper
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Holder for KutePreference items for easy access to preference values across the application
 */
@Singleton
class KutePreferencesHolder @Inject constructor(
    private val context: Context,
    private val translationManager: TranslationManager,
    private val iconHelper: IconHelper
) {

    private val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(context)
    }

    val textPreference by lazy {
        KuteTextPreference(
            key = R.string.key_demo_text_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_text_format),
            title = translationManager.getTranslation(R.string.title_demo_text_pref),
            defaultValue = translationManager.getTranslation(R.string.default_value_demo_text_pref),
            dataProvider = dataProvider
        )
    }

    val textPreference2 by lazy {
        KuteTextPreference(
            key = R.string.key_demo_text_pref_2,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_text_format),
            title = translationManager.getTranslation(R.string.title_demo_text_pref_2),
            defaultValue = TEXT_PREFERENCE_2_DEFAULT_VALUE,
            dataProvider = dataProvider
        )
    }

    val urlPreference by lazy {
        KuteUrlPreference(
            key = R.string.key_demo_url_pref,
            title = translationManager.getTranslation(R.string.title_demo_url_pref),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_open_in_browser),
            defaultValue = "https://www.markusressel.de",
            dataProvider = dataProvider
        )
    }

    val passwordPreference by lazy {
        KutePasswordPreference(
            key = R.string.key_demo_text_pref_password,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_lock),
            title = translationManager.getTranslation(R.string.title_demo_text_pref_password),
            regex = "^.{8,}\$|^\$",
            defaultValue = "",
            dataProvider = dataProvider
        )
    }

    val togglePreference by lazy {
        KuteBooleanPreference(
            key = R.string.key_demo_toggle_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_airplane),
            title = translationManager.getTranslation(R.string.title_demo_toggle_pref),
            descriptionFunction = {
                if (it) {
                    "All radios in this device are disabled"
                } else {
                    "Normal operation"
                }
            },
            defaultValue = false,
            onPreferenceChangedListener = { old, new ->
                Toast.makeText(context, "Old: $old New: $new", Toast.LENGTH_SHORT).show()
            },
            dataProvider = dataProvider
        )
    }

    val numberPreference by lazy {
        KuteNumberPreference(
            key = R.string.key_demo_number_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_time_countdown),
            title = translationManager.getTranslation(R.string.title_demo_number_pref),
            unit = "ms",
            defaultValue = 2000,
            dataProvider = dataProvider
        )
    }

    val sliderPreference by lazy {
        KuteSliderPreference(
            key = R.string.key_demo_slider_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_volume_up),
            title = translationManager.getTranslation(R.string.title_demo_slider_pref),
            minimum = 0,
            maximum = 7,
            defaultValue = 5,
            dataProvider = dataProvider
        )
    }

//    val intRangePreference by lazy {
//        KuteIntRangePreference(
//            key = R.string.key_demo_int_range_pref,
//            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_arrows),
//            title = translationManager.getTranslation(R.string.title_demo_int_range_pref),
//            minimum = 0,
//            maximum = 100,
//            defaultValue = RangePersistenceModel(0, 100),
//            dataProvider = dataProvider
//        )
//    }

    val floatRangePreference by lazy {
        KuteFloatRangePreference(
            key = R.string.key_demo_float_range_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_arrows),
            title = translationManager.getTranslation(R.string.title_demo_float_range_pref),
            decimalPlaces = 2,
            minimum = -0.1f,
            maximum = 0.1f,
            defaultValue = RangePersistenceModel(-0.003f, 0.02f),
            dataProvider = dataProvider
        )
    }

    val colorPreference by lazy {
        KuteColorPreference(
            key = R.string.key_demo_color_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_colorize),
            title = translationManager.getTranslation(R.string.title_demo_color_pref),
            defaultValue = ContextCompat.getColor(context, R.color.design_default_color_primary),
            dataProvider = dataProvider
        )
    }

    val datePreference by lazy {
        KuteDatePreference(
            key = R.string.key_demo_date_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_calendar),
            title = translationManager.getTranslation(R.string.title_demo_date_pref),
            defaultValue = LocalDate.now().toEpochDay(),
            dataProvider = dataProvider
        )
    }

    val timePreference by lazy {
        KuteTimePreference(
            key = R.string.key_demo_time_pref,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_time),
            title = translationManager.getTranslation(R.string.title_demo_time_pref),
            defaultValue = TimePersistenceModel(hourOfDay = 12, minute = 30),
            dataProvider = dataProvider
        )
    }

    val singleSelectPreference by lazy {
        KuteSingleSelectStringPreference(
            context = context,
            key = R.string.key_demo_single_select,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_select_all),
            title = translationManager.getTranslation(R.string.title_demo_single_select),
            defaultValue = R.string.key_single_select_1,
            possibleValues = mapOf(
                R.string.key_single_select_1 to R.string.title_single_select_1,
                R.string.key_single_select_2 to R.string.title_single_select_2,
                R.string.key_single_select_3 to R.string.title_single_select_3
            ),
            dataProvider = dataProvider
        )
    }

    val multiSelectPreference by lazy {
        KuteMultiSelectStringPreference(
            context = context,
            key = R.string.key_demo_multi_select,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_select_all),
            title = translationManager.getTranslation(R.string.title_demo_multi_select),
            defaultValue = setOf(R.string.key_multi_select_1, R.string.key_multi_select_2),
            possibleValues = mapOf(
                R.string.key_multi_select_1 to R.string.title_multi_select_1,
                R.string.key_multi_select_2 to R.string.title_multi_select_2,
                R.string.key_multi_select_3 to R.string.title_multi_select_3
            ),
            dataProvider = dataProvider
        )
    }

    val kuteAction by lazy {
        KuteAction(
            key = R.string.key_demo_action,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_info),
            title = translationManager.getTranslation(R.string.title_demo_action),
            description = "Additional info about this app.",
            onClick = {
                Toast.makeText(context, "About clicked!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    companion object {
        const val TEXT_PREFERENCE_2_DEFAULT_VALUE = "Markus Ressel"
    }

}