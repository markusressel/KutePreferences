package de.markusressel.kutepreferences.preferences

import android.content.Context
import android.widget.Toast
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.KuteTogglePreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KutePreferencesHolder @Inject constructor() {

    @Inject
    lateinit var context: Context

    val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(context)
    }

    val textPreference by lazy {
        KuteTextPreference(
                key = R.string.key_demo_text_pref,
                dataProvider = dataProvider,
                name = "Sample Text Pref",
                defaultValue = "Sample value")
    }

    val togglePreference by lazy {
        KuteTogglePreference(
                key = R.string.key_demo_toggle_pref,
                dataProvider = dataProvider,
                name = "Sample Toggle Pref",
                defaultValue = false,
                onPreferenceChangedListener = { old, new ->
                    Toast.makeText(
                            context,
                            "Old: $old New: $new",
                            Toast.LENGTH_SHORT)
                            .show()
                })
    }

}