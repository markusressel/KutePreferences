package de.markusressel.kutepreferences

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.markusressel.kutepreferences.library.SimpleKutePreferenceCategory
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.persistence.KutePreferenceLayoutGenerator
import de.markusressel.kutepreferences.library.preference.KuteTextPreference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataProvider = createDataProvider()

        val textPreference = KuteTextPreference(
                key = R.string.key_demo_text_pref,
                dataProvider = dataProvider,
                name = "Text KutePreference",
                defaultValue = "None")

        val pageItems = arrayOf(
                SimpleKutePreferenceCategory(
                        name = "Category 1",
                        childViews = listOf(
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