package de.markusressel.kutepreferences.view

import android.os.Bundle
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.dagger.DaggerSupportActivityBase
import de.markusressel.kutepreferences.library.KutePreferenceLayoutGenerator
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceCategory
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceDivider
import de.markusressel.kutepreferences.preferences.KutePreferencesHolder
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerSupportActivityBase() {

    @Inject
    lateinit var kutePreferencesHolder: KutePreferencesHolder

    override val style: Int
        get() = DEFAULT
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pageItems: Array<KutePreferenceListItem> = arrayOf(
                SimpleKutePreferenceCategory(
                        name = "Category 1",
                        description = "Description of this category",
                        childPreferences = listOf(
                                SimpleKutePreferenceCategory(
                                        name = "Category 2",
                                        description = "Description of this category",
                                        childPreferences = listOf(
                                                kutePreferencesHolder.togglePreference
                                        )
                                ),
                                kutePreferencesHolder.textPreference,
                                kutePreferencesHolder.textPreference,
                                SimpleKutePreferenceDivider("Test Divider"),
                                kutePreferencesHolder.textPreference,
                                kutePreferencesHolder.textPreference
                        )
                ))


        val generator = KutePreferenceLayoutGenerator(this)
        val kutePreferencePage = generator.generatePage(*pageItems)

        contentFrame.addView(kutePreferencePage)
    }


    private fun accessStoredValues() {
        val textValue = kutePreferencesHolder.textPreference.persistedValue
    }


    private fun createDataProvider(): KutePreferenceDataProvider {
        return DefaultKutePreferenceDataProvider(this)
    }

}