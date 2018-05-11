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
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceCategory
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceDivider
import de.markusressel.kutepreferences.library.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.toggle.KuteTogglePreference
import de.markusressel.kutepreferences.library.view.KutePreferencesMainFragment

class PreferencesFragment : KutePreferencesMainFragment() {

    val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(activity as Context)
    }

    private val textPreference by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref,
                name = "Sample Text Pref",
                defaultValue = "Sample value",
                dataProvider = dataProvider)
    }

    private val textPreference2 by lazy {
        KuteTextPreference(key = R.string.key_demo_text_pref_2,
                name = "Sample Text Pref 2",
                defaultValue = "Sample value",
                dataProvider = dataProvider)
    }

    private val togglePreference by lazy {
        KuteTogglePreference(key = R.string.key_demo_toggle_pref,
                name = "Sample Toggle Pref",
                defaultValue = false,
                onPreferenceChangedListener = { old, new ->
                    Toast
                            .makeText(context, "Old: $old New: $new", Toast.LENGTH_SHORT)
                            .show()
                },
                dataProvider = dataProvider)
    }

    override fun initPreferenceTree(): KutePreferencesTree {
        return KutePreferencesTree(
                SimpleKutePreferenceCategory(
                        key = R.string.key_category_battery,
                        icon = getIcon(MaterialDesignIconic.Icon.gmi_battery),
                        name = "Battery",
                        description = "Everything about your battery",
                        children = listOf(
                                SimpleKutePreferenceDivider(
                                        key = R.string.key_divider_test,
                                        name = "Test Divider"),
                                textPreference)),
                SimpleKutePreferenceCategory(key = R.string.key_category_wifi,
                        icon = getIcon(MaterialDesignIconic.Icon.gmi_wifi),
                        name = "Network",
                        description = "Wi-Fi, mobile, hotspot",
                        children = listOf(togglePreference
                        )
                ),
                textPreference2)
    }

    private fun getIcon(icon: IIcon, @ColorInt color: Int = Color.GRAY, sizeDp: Int = 36,
                        paddingDp: Int = 0): Drawable {
        return IconicsDrawable(activity as Context, icon)
                .sizeDp(sizeDp)
                .paddingDp(paddingDp)
                .color(color)
    }

}
