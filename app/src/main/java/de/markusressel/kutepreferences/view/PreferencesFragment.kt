package de.markusressel.kutepreferences.view

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.helper.IconHelper
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import de.markusressel.kutepreferences.library.preference.category.KuteCategory
import de.markusressel.kutepreferences.library.preference.category.KuteDivider
import de.markusressel.kutepreferences.preferences.DaggerKutePreferenceFragmentBase
import de.markusressel.kutepreferences.preferences.KutePreferencesHolder
import javax.inject.Inject

/**
 * Main Preference Page
 */
class PreferencesFragment : DaggerKutePreferenceFragmentBase() {

    @Inject
    lateinit var kutePreferencesHolder: KutePreferencesHolder

    @Inject
    lateinit var iconHelper: IconHelper

    override fun initPreferenceTree(): KutePreferencesTree {
        return KutePreferencesTree(
                KuteCategory(
                        key = R.string.key_category_battery,
                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_battery),
                        title = getString(R.string.title_category_battery),
                        description = getString(R.string.description_category_battery),
                        children = listOf(
                                KuteDivider(
                                        key = R.string.key_divider_test,
                                        title = "Test Divider"),
                                kutePreferencesHolder.textPreference
                        )
                ),
                KuteCategory(key = R.string.key_category_network,
                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_wifi),
                        title = getString(R.string.title_category_network),
                        description = getString(R.string.description_category_network),
                        children = listOf(
                                kutePreferencesHolder.togglePreference,
                                kutePreferencesHolder.numberPreference
                        )
                ),
                KuteCategory(key = R.string.key_category_user,
                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_nature_people),
                        title = getString(R.string.title_category_user),
                        description = getString(R.string.description_category_user),
                        children = listOf(
                                kutePreferencesHolder.textPreference2,
                                kutePreferencesHolder.singleSelectPreference,
                                kutePreferencesHolder.colorPreference,
                                kutePreferencesHolder.datePreference,
                                kutePreferencesHolder.passwordPreference
                        )
                ),
                kutePreferencesHolder.sliderPreference,
                kutePreferencesHolder.multiSelectPreference,
                kutePreferencesHolder.kuteAction
        )
    }

}
