package de.markusressel.kutepreferences.preferences

import android.content.Context
import android.widget.Toast
import androidx.core.widget.toast
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.helper.IconHelper
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.action.KuteAction
import de.markusressel.kutepreferences.library.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.library.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.library.preference.number.KuteSliderPreference
import de.markusressel.kutepreferences.library.preference.select.KuteMultiSelectPreference
import de.markusressel.kutepreferences.library.preference.select.KuteSingleSelectPreference
import de.markusressel.kutepreferences.library.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.library.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.toggle.KuteTogglePreference
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Holder for KutePreference items for easy access to preference values across the application
 */
@Singleton
class KutePreferencesHolder @Inject constructor(
        private val context: Context,
        private val iconHelper: IconHelper) {

    val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(context)
    }

    val textPreference by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_battery),
                title = context.getString(R.string.title_demo_text_pref),
                defaultValue = context.getString(R.string.default_value_demo_text_pref),
                dataProvider = dataProvider)
    }

    val textPreference2 by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref_2,
                title = context.getString(R.string.title_demo_text_pref_2),
                defaultValue = "Markus Ressel",
                dataProvider = dataProvider)
    }

    val passwordPreference by lazy {
        KutePasswordPreference(key = R.string.key_demo_text_pref_password,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_lock),
                title = context.getString(R.string.title_demo_text_pref_password),
                defaultValue = "",
                dataProvider = dataProvider)
    }

    val togglePreference by lazy {
        KuteTogglePreference(key = R.string.key_demo_toggle_pref,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_airplane),
                title = context.getString(R.string.title_demo_toggle_pref),
                descriptionFunction = {
                    if (it) {
                        "All radios in this device are disabled"
                    } else {
                        "Normal operation"
                    }
                },
                defaultValue = false,
                onPreferenceChangedListener = { old, new ->
                    context.toast("Old: $old New: $new", Toast.LENGTH_SHORT)
                },
                dataProvider = dataProvider)
    }

    val numberPreference by lazy {
        KuteNumberPreference(key = R.string.key_demo_number_pref,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_time_countdown),
                title = context.getString(R.string.title_demo_number_pref),
                unit = "ms",
                defaultValue = 2000,
                dataProvider = dataProvider)
    }

    val sliderPreference by lazy {
        KuteSliderPreference(key = R.string.key_demo_slider_pref,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_volume_up),
                title = context.getString(R.string.title_demo_slider_pref),
                maximum = 7,
                defaultValue = 5,
                dataProvider = dataProvider)
    }

    val datePreference by lazy {
        KuteDatePreference(key = R.string.key_demo_date_pref,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_calendar),
                title = context.getString(R.string.title_demo_date_pref),
                defaultValue = Date().time,
                dataProvider = dataProvider)
    }

    val singleSelectPreference by lazy {
        KuteSingleSelectPreference(
                context = context,
                key = R.string.key_demo_single_select,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_select_all),
                title = context.getString(R.string.title_demo_single_select),
                defaultValue = R.string.key_single_select_1,
                possibleValues = mapOf(
                        R.string.key_single_select_1 to R.string.title_single_select_1,
                        R.string.key_single_select_2 to R.string.title_single_select_2,
                        R.string.key_single_select_3 to R.string.title_single_select_3
                ),
                dataProvider = dataProvider)
    }

    val multiSelectPreference by lazy {
        KuteMultiSelectPreference(
                context = context,
                key = R.string.key_demo_multi_select,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_select_all),
                title = context.getString(R.string.title_demo_multi_select),
                defaultValue = setOf(R.string.key_multi_select_1, R.string.key_multi_select_2),
                possibleValues = mapOf(
                        R.string.key_multi_select_1 to R.string.title_multi_select_1,
                        R.string.key_multi_select_2 to R.string.title_multi_select_2,
                        R.string.key_multi_select_3 to R.string.title_multi_select_3
                ),
                dataProvider = dataProvider)
    }

    val kuteAction by lazy {
        KuteAction(key = R.string.key_demo_action,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_info),
                title = context.getString(R.string.title_demo_action),
                onClickAction = { context, kuteAction ->
                    context.toast("Action clicked!", Toast.LENGTH_SHORT)
                })
    }

}