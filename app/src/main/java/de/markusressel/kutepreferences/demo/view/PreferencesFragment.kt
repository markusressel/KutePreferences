package de.markusressel.kutepreferences.demo.view

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.demo.R
import de.markusressel.kutepreferences.demo.helper.IconHelper
import de.markusressel.kutepreferences.demo.preferences.DaggerKutePreferenceFragmentBase
import de.markusressel.kutepreferences.demo.preferences.KutePreferencesHolder
import javax.inject.Inject

/**
 * Main Preference Page
 */
class PreferencesFragment : DaggerKutePreferenceFragmentBase() {

    @Inject
    lateinit var kutePreferencesHolder: KutePreferencesHolder

    @Inject
    lateinit var iconHelper: IconHelper

    override fun initPreferenceTree(): Array<Any> {
        kutePreferencesHolder.floatRangePreference.reset()

        return arrayOf(
                kutePreferencesHolder.sliderPreference,
                kutePreferencesHolder.intRangePreference,
                kutePreferencesHolder.floatRangePreference
        )

//        return arrayOf(
//                KuteSection(
//                        key = R.string.key_section_system_hardware,
//                        title = getString(R.string.title_section_system_hardware),
//                        children = listOf(
//                                KuteCategory(
//                                        key = R.string.key_category_battery,
//                                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_battery),
//                                        title = getString(R.string.title_category_battery),
//                                        description = getString(R.string.description_category_battery),
//                                        children = listOf(
//                                                KuteSection(
//                                                        key = R.string.key_section_test,
//                                                        title = "Test Divider",
//                                                        children = listOf(
//                                                                kutePreferencesHolder.textPreference
//                                                        ))
//                                        )
//                                ),
//                                KuteCategory(key = R.string.key_category_network,
//                                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_wifi),
//                                        title = getString(R.string.title_category_network),
//                                        description = getString(R.string.description_category_network),
//                                        children = listOf(
//                                                kutePreferencesHolder.togglePreference,
//                                                kutePreferencesHolder.numberPreference
//                                        )
//                                ),
//                                kutePreferencesHolder.sliderPreference,
//                                kutePreferencesHolder.intRangePreference,
//                                kutePreferencesHolder.floatRangePreference
//                        )
//                ),
//                KuteSection(
//                        key = R.string.key_section_device_owner,
//                        title = getString(R.string.title_section_device_owner),
//                        children = listOf(
//                                KuteCategory(key = R.string.key_category_user,
//                                        icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_nature_people),
//                                        title = getString(R.string.title_category_user),
//                                        description = getString(R.string.description_category_user),
//                                        children = listOf(
//                                                KuteSection(
//                                                        key = R.string.key_section_person,
//                                                        title = getString(R.string.title_section_person),
//                                                        children = listOf(
//                                                                kutePreferencesHolder.textPreference2,
//                                                                kutePreferencesHolder.singleSelectPreference,
//                                                                kutePreferencesHolder.datePreference,
//                                                                kutePreferencesHolder.timePreference)
//                                                ),
//                                                KuteSection(
//                                                        key = R.string.key_section_taste,
//                                                        title = getString(R.string.title_section_taste),
//                                                        children = listOf(kutePreferencesHolder.colorPreference
//                                                        )),
//                                                KuteSection(
//                                                        key = R.string.key_section_security,
//                                                        title = getString(R.string.title_section_security),
//                                                        children = listOf(kutePreferencesHolder.passwordPreference)
//                                                )
//                                        )
//                                ),
//                                kutePreferencesHolder.multiSelectPreference
//                        )
//                ),
//                kutePreferencesHolder.urlPreference,
//                kutePreferencesHolder.kuteAction
//        )
    }

}
