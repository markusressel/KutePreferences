package de.markusressel.kutepreferences.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.widget.Toast
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import de.markusressel.kutepreferences.library.preference.action.KuteAction
import de.markusressel.kutepreferences.library.preference.category.KuteCategory
import de.markusressel.kutepreferences.library.preference.category.KuteDivider
import de.markusressel.kutepreferences.library.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.library.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.library.preference.number.KuteSliderPreference
import de.markusressel.kutepreferences.library.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.toggle.KuteTogglePreference
import de.markusressel.kutepreferences.library.view.KutePreferencesMainFragment
import java.util.*

class PreferencesFragment : KutePreferencesMainFragment() {

    private val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(activity as Context)
    }

    private val textPreference by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref,
                title = "Name",
                defaultValue = "My Battery",
                dataProvider = dataProvider)
    }

    private val textPreference2 by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref_2,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_nature_people),
                title = "Device Owner",
                defaultValue = "Markus Ressel",
                dataProvider = dataProvider)
    }

    private val togglePreference by lazy {
        KuteTogglePreference(key = R.string.key_demo_toggle_pref,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_airplane),
                title = "Airplane Mode",
                defaultValue = false,
                onPreferenceChangedListener = { old, new ->
                    Toast
                            .makeText(context, "Old: $old New: $new", Toast.LENGTH_SHORT)
                            .show()
                },
                dataProvider = dataProvider)
    }

    private val numberPreference by lazy {
        KuteNumberPreference(key = R.string.key_demo_number_pref,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_time_countdown),
                title = "Connection Timeout",
                defaultValue = 2000,
                dataProvider = dataProvider)
    }

    private val sliderPreference by lazy {
        KuteSliderPreference(key = R.string.key_demo_slider_pref,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_volume_up),
                title = "Volume",
                maximum = 7,
                defaultValue = 5,
                dataProvider = dataProvider)
    }

    private val datePreference by lazy {
        KuteDatePreference(key = R.string.key_demo_date_pref,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_calendar),
                title = "Date",
                defaultValue = Date().time,
                dataProvider = dataProvider)
    }

    private val kuteAction by lazy {
        KuteAction(key = R.string.key_demo_action,
                icon = getIcon(MaterialDesignIconic.Icon.gmi_calendar),
                title = "Demo Action",
                onClickAction = {
                    Toast.makeText(it, "Action clicked!", Toast.LENGTH_SHORT).show()
                })
    }

    override fun initPreferenceTree(): KutePreferencesTree {
        return KutePreferencesTree(
                KuteCategory(
                        key = R.string.key_category_battery,
                        icon = getIcon(MaterialDesignIconic.Icon.gmi_battery),
                        title = "Battery",
                        description = "Everything about your battery",
                        children = listOf(
                                KuteDivider(
                                        key = R.string.key_divider_test,
                                        title = "Test Divider"),
                                textPreference
                        )
                ),
                KuteCategory(key = R.string.key_category_wifi,
                        icon = getIcon(MaterialDesignIconic.Icon.gmi_wifi),
                        title = "Network",
                        description = "Wi-Fi, mobile, hotspot",
                        children = listOf(
                                togglePreference,
                                numberPreference
                        )
                ),
                textPreference2,
                sliderPreference,
                datePreference,
                kuteAction
        )
    }

    private fun getIcon(icon: IIcon, @ColorInt color: Int = Color.GRAY, sizeDp: Int = 36,
                        paddingDp: Int = 0): Drawable {
        return IconicsDrawable(activity as Context, icon)
                .sizeDp(sizeDp)
                .paddingDp(paddingDp)
                .color(color)
    }

}
