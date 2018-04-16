package de.markusressel.kutepreferences

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.markusressel.kutepreferences.library.KutePreferenceLayoutGenerator
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.KuteTogglePreference
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceCategory
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceDivider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataProvider = createDataProvider()

        val textPreference = KuteTextPreference(
                key = R.string.key_demo_text_pref,
                dataProvider = dataProvider,
                name = "Sample Text Pref",
                defaultValue = "Sample value")

        val togglePreference = KuteTogglePreference(
                key = R.string.key_demo_toggle_pref,
                dataProvider = dataProvider,
                name = "Sample Toggle Pref",
                defaultValue = false)

        val pageItems: Array<KutePreferenceListItem> = arrayOf(
                SimpleKutePreferenceCategory(
                        name = "Category 1",
                        description = "Description of this category",
                        childPreferences = listOf(
                                SimpleKutePreferenceCategory(
                                        name = "Category 2",
                                        description = "Description of this category",
                                        childPreferences = listOf(
                                                togglePreference
                                        )
                                ),
                                textPreference,
                                textPreference,
                                SimpleKutePreferenceDivider("Test Divider"),
                                textPreference,
                                textPreference
                        )
                ))


        val generator = KutePreferenceLayoutGenerator(this)
        val kutePreferencePage = generator.generatePage(*pageItems)

        contentFrame.addView(kutePreferencePage)
    }

    private fun createDataProvider(): KutePreferenceDataProvider {
        return DefaultKutePreferenceDataProvider(this)
    }

}