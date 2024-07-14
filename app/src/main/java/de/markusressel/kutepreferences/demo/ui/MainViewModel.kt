package de.markusressel.kutepreferences.demo.ui

import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import dagger.hilt.android.lifecycle.HiltViewModel
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.demo.R
import de.markusressel.kutepreferences.demo.domain.*
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataProvider: KutePreferenceDataProvider,
    private val translationManager: TranslationManager,
    private val kutePreferencesHolder: KutePreferencesHolder,
    private val iconHelper: IconHelper,
    navigator: KuteNavigator,
) : KutePreferencesViewModel(navigator) {

    init {
        /**
         * To make custom preference types show up, you have to register the with the
         * [KuteStyleManager]. You can also use this to manipulate the UI of existing
         * types.
         */
        KuteStyleManager.registerTypeHook { listItem ->
            when (listItem) {
                is CustomPreference -> {
                    CustomPreferenceView(listItem)
                    true
                }

                is CustomBooleanPreference -> {
                    val behavior = CustomBooleanPreferenceBehavior(listItem)
                    CustomBooleanPreferenceView(behavior)
                    true
                }

                else -> false
            }
        }

        val preferenceTree = createPreferenceTree()
        initPreferencesTree(preferenceTree)
    }

    private fun createPreferenceTree(): List<KutePreferenceListItem> {
        val categoryBattery = KuteCategory(
            key = R.string.key_category_battery,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_battery),
            title = translationManager.getTranslation(R.string.title_category_battery),
            description = translationManager.getTranslation(R.string.description_category_battery),
            children = listOf(
                KuteSection(
                    key = R.string.key_section_test,
                    title = "Test Divider",
                    children = listOf(
                        kutePreferencesHolder.textPreference
                    ),
                )
            ),
            onClick = { navigator.enterCategory(R.string.key_category_battery) },
        )

        val categoryNetwork = KuteCategory(
            key = R.string.key_category_network,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_wifi),
            title = translationManager.getTranslation(R.string.title_category_network),
            description = translationManager.getTranslation(R.string.description_category_network),
            children = listOf(
                kutePreferencesHolder.togglePreference,
                kutePreferencesHolder.numberPreference
            ),
            onClick = { navigator.enterCategory(R.string.key_category_network) },
        )

        val typeSectionContent = createTypeSectionContent()

        val systemHardwareSectionContent = listOf(
            categoryBattery,
            categoryNetwork,
            kutePreferencesHolder.sliderPreference,
//            kutePreferencesHolder.intRangePreference,
            kutePreferencesHolder.floatRangePreference
        )

        val categoryUser = KuteCategory(
            key = R.string.key_category_user,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_nature_people),
            title = translationManager.getTranslation(R.string.title_category_user),
            description = translationManager.getTranslation(R.string.description_category_user),
            onClick = { navigator.enterCategory(R.string.key_category_user) },
            children = listOf(
                KuteSection(
                    key = R.string.key_section_person,
                    title = translationManager.getTranslation(R.string.title_section_person),
                    children = listOf(
                        kutePreferencesHolder.textPreference2,
                        kutePreferencesHolder.singleSelectPreference,
                        kutePreferencesHolder.datePreference,
                        kutePreferencesHolder.timePreference
                    )
                ),
                KuteSection(
                    key = R.string.key_section_taste,
                    title = translationManager.getTranslation(R.string.title_section_taste),
                    children = listOf(
                        kutePreferencesHolder.colorPreference
                    )
                ),
                KuteSection(
                    key = R.string.key_section_security,
                    title = translationManager.getTranslation(R.string.title_section_security),
                    children = listOf(
                        kutePreferencesHolder.passwordPreference
                    )
                )
            )
        )

        return listOf(
            KuteSection(
                key = R.string.sec_first,
                title = "Base Types",
                children = typeSectionContent,
            ),
            KuteSection(
                key = R.string.custom_items,
                title = "Custom Types",
                children = createCustomTypeSectionItems()
            ),
            KuteCategory(
                key = R.string.cat_hello,
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_developer_board),
                title = "Example Category",
                description = "This category contains example preferences that might be realistic in a real app.",
                onClick = { navigator.enterCategory(R.string.cat_hello) },
                children = listOf(
                    KuteSection(
                        key = R.string.key_section_system_hardware,
                        title = translationManager.getTranslation(R.string.title_section_system_hardware),
                        children = systemHardwareSectionContent
                    ),
                    KuteSection(
                        key = R.string.key_section_device_owner,
                        title = translationManager.getTranslation(R.string.title_section_device_owner),
                        children = listOf(
                            categoryUser,
                            kutePreferencesHolder.multiSelectPreference
                        )
                    ),
                    kutePreferencesHolder.kuteAction,
                )
            ),
        )
    }

    private fun createCustomTypeSectionItems(): List<KutePreferenceListItem> = listOf(
        CustomBooleanPreference(
            key = R.string.pref_boolean,
            title = "Custom Boolean Preference",
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        CustomBooleanPreference(
            key = R.string.pref_boolean,
            title = "Second Custom Boolean Preference",
            descriptionFunction = {
                "This uses the same key as the entry above, which results in both entries updating together."
            },
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        CustomPreference(
            key = R.string.pref_custom_item,
            onClick = {},
            onLongClick = {},
            dataProvider = dataProvider,
            title = "Custom Preference"
        )
    )

    private fun createTypeSectionContent(): List<KutePreferenceListItem> = listOf(
        KuteBooleanPreference(
            key = R.string.pref_boolean,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_check),
            title = "Boolean",
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        KuteTextPreference(
            key = R.string.pref_string,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_text_format),
            title = "Text",
            dataProvider = dataProvider,
            defaultValue = "Current Value"
        ),
        kutePreferencesHolder.urlPreference,
        kutePreferencesHolder.passwordPreference,
        KuteNumberPreference(
            key = R.string.pref_number_float,
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_confirmation_number),
            title = translationManager.getTranslation(R.string.title_demo_number_pref),
            dataProvider = dataProvider,
            defaultValue = 1337,
        ),
        kutePreferencesHolder.sliderPreference,
        kutePreferencesHolder.intRangePreference,
        kutePreferencesHolder.floatRangePreference,
        kutePreferencesHolder.datePreference,
        kutePreferencesHolder.timePreference,
        kutePreferencesHolder.colorPreference,
        kutePreferencesHolder.singleSelectPreference,
        kutePreferencesHolder.multiSelectPreference,
    )

    fun onBackPressed(): Boolean {
        return navigator.goBack()
    }

}