package de.markusressel.kutepreferences.demo.ui

import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import dagger.hilt.android.lifecycle.HiltViewModel
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.matches
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.demo.R
import de.markusressel.kutepreferences.demo.domain.*
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
         * To make custom preference types show up, you have to register them with the
         * [registry]. You can also use this to replace the UI of existing types.
         */
        registry.register<CustomPreference> { CustomPreferenceView(it) }
        registry.register(::CustomBooleanPreferenceBehavior) { CustomBooleanPreferenceView(it) }

        val preferenceTree = createPreferenceTree()
        initPreferencesTree(preferenceTree)
    }

    private fun createPreferenceTree(): List<KutePreferenceListItem> {
        val categoryBattery = KuteCategory(
            key = translationManager.getTranslation(R.string.key_category_battery),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_battery),
            title = translationManager.getTranslation(R.string.title_category_battery),
            description = translationManager.getTranslation(R.string.description_category_battery),
            children = listOf(
                KuteSection(
                    key = translationManager.getTranslation(R.string.key_section_test),
                    title = "Test Divider",
                    children = listOf(
                        kutePreferencesHolder.textPreference
                    ),
                )
            ),
        )

        val categoryNetwork = KuteCategory(
            key = translationManager.getTranslation(R.string.key_category_network),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_wifi),
            title = translationManager.getTranslation(R.string.title_category_network),
            description = translationManager.getTranslation(R.string.description_category_network),
            children = listOf(
                kutePreferencesHolder.togglePreference,
                kutePreferencesHolder.numberPreference
            ),
        )

        val typeSectionContent = createTypeSectionContent()

        val systemHardwareSectionContent = listOf(
            categoryBattery,
            categoryNetwork,
            kutePreferencesHolder.intSliderPreference,
//            kutePreferencesHolder.intRangePreference,
            kutePreferencesHolder.floatRangePreference
        )

        val categoryUser = KuteCategory(
            key = translationManager.getTranslation(R.string.key_category_user),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_nature_people),
            title = translationManager.getTranslation(R.string.title_category_user),
            description = translationManager.getTranslation(R.string.description_category_user),
            children = listOf(
                KuteSection(
                    key = translationManager.getTranslation(R.string.key_section_person),
                    title = translationManager.getTranslation(R.string.title_section_person),
                    children = listOf(
                        kutePreferencesHolder.textPreference2,
                        kutePreferencesHolder.singleSelectPreference,
                        kutePreferencesHolder.datePreference,
                        kutePreferencesHolder.timePreference
                    )
                ),
                KuteSection(
                    key = translationManager.getTranslation(R.string.key_section_taste),
                    title = translationManager.getTranslation(R.string.title_section_taste),
                    children = listOf(
                        kutePreferencesHolder.colorPreference
                    )
                ),
                KuteSection(
                    key = translationManager.getTranslation(R.string.key_section_security),
                    title = translationManager.getTranslation(R.string.title_section_security),
                    children = listOf(
                        kutePreferencesHolder.passwordPreference
                    )
                )
            )
        )

        return listOf(
            KuteSection(
                key = translationManager.getTranslation(R.string.sec_first),
                title = "Base Types",
                children = typeSectionContent,
            ),
            KuteSection(
                key = translationManager.getTranslation(R.string.custom_items),
                title = "Custom Types",
                children = createCustomTypeSectionItems()
            ),
            KuteSection(
                key = "section_conditional",
                title = "Conditional Preferences",
                children = createConditionalSectionItems(),
            ),
            KuteCategory(
                key = translationManager.getTranslation(R.string.cat_hello),
                icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_developer_board),
                title = "Example Category",
                description = "This category contains example preferences that might be realistic in a real app.",
                children = listOf(
                    KuteSection(
                        key = translationManager.getTranslation(R.string.key_section_system_hardware),
                        title = translationManager.getTranslation(R.string.title_section_system_hardware),
                        children = systemHardwareSectionContent
                    ),
                    KuteSection(
                        key = translationManager.getTranslation(R.string.key_section_device_owner),
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

    private fun createConditionalSectionItems(): List<KutePreferenceListItem> {
        val useProxy = KuteBooleanPreference(
            key = "demo_use_proxy",
            title = "Use a proxy",
            dataProvider = dataProvider,
            defaultValue = false,
        )
        val authenticate = KuteBooleanPreference(
            key = "demo_proxy_authenticate",
            title = "Authenticate at the proxy",
            dataProvider = dataProvider,
            defaultValue = false,
            visibleWhen = useProxy.matches { it },
        )
        val proxyUser = KuteTextPreference(
            key = "demo_proxy_user",
            title = "Proxy user",
            dataProvider = dataProvider,
            defaultValue = "",
            visibleWhen = useProxy.matches { it },
            enabledWhen = authenticate.matches { it },
        )

        val proxyProtocol = KuteChoicePreference(
            key = "demo_proxy_protocol",
            title = "Proxy protocol",
            defaultValue = "http",
            options = mapOf("http" to "HTTP", "socks" to "SOCKS"),
            dataProvider = dataProvider,
            visibleWhen = useProxy.matches { it },
        )
        val proxyBypass = KuteMultiChoicePreference(
            key = "demo_proxy_bypass",
            title = "Bypass the proxy for",
            options = mapOf("local" to "Local addresses", "vpn" to "VPN"),
            dataProvider = dataProvider,
            visibleWhen = useProxy.matches { it },
        )

        return listOf(useProxy, proxyProtocol, proxyBypass, authenticate, proxyUser)
    }

    private fun createCustomTypeSectionItems(): List<KutePreferenceListItem> = listOf(
        CustomBooleanPreference(
            key = translationManager.getTranslation(R.string.pref_boolean),
            title = "Custom Boolean Preference",
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        CustomBooleanPreference(
            key = translationManager.getTranslation(R.string.pref_boolean),
            title = "Second Custom Boolean Preference",
            descriptionFunction = {
                "This uses the same key as the entry above, which results in both entries updating together."
            },
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        CustomPreference(
            key = translationManager.getTranslation(R.string.pref_custom_item),
            onClick = {},
            onLongClick = {},
            dataProvider = dataProvider,
            title = "Custom Preference"
        )
    )

    private fun createTypeSectionContent(): List<KutePreferenceListItem> = listOf(
        KuteBooleanPreference(
            key = translationManager.getTranslation(R.string.pref_boolean),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_check),
            title = "Boolean",
            dataProvider = dataProvider,
            defaultValue = true,
        ),
        KuteTextPreference(
            key = translationManager.getTranslation(R.string.pref_string),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_text_format),
            title = "Text",
            dataProvider = dataProvider,
            defaultValue = "Current Value"
        ),
        kutePreferencesHolder.urlPreference,
        kutePreferencesHolder.passwordPreference,
        KuteNumberPreference(
            key = translationManager.getTranslation(R.string.pref_number_float),
            icon = iconHelper.getIcon(MaterialDesignIconic.Icon.gmi_confirmation_number),
            title = translationManager.getTranslation(R.string.title_demo_number_pref),
            dataProvider = dataProvider,
            defaultValue = 1337,
        ),
        kutePreferencesHolder.intSliderPreference,
        kutePreferencesHolder.floatSliderPreference,
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