package de.markusressel.kutepreferences.view

import android.content.Context
import android.widget.Toast
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.library.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceCategory
import de.markusressel.kutepreferences.library.preference.category.SimpleKutePreferenceDivider
import de.markusressel.kutepreferences.library.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.library.preference.toggle.KuteTogglePreference
import de.markusressel.kutepreferences.library.view.KutePreferencesMainFragment
import java.util.concurrent.atomic.AtomicLong

class PreferencesFragment : KutePreferencesMainFragment() {

    val idCounter = AtomicLong()

    val dataProvider by lazy {
        DefaultKutePreferenceDataProvider(activity as Context)
    }

    private val textPreference by lazy {
        KuteTextPreference(id = idCounter.getAndIncrement(), key = R.string.key_demo_text_pref, name = "Sample Text Pref", defaultValue = "Sample value", dataProvider = dataProvider)
    }

    private val textPreference2 by lazy {
        KuteTextPreference(id = idCounter.getAndIncrement(), key = R.string.key_demo_text_pref, name = "Sample Text Pref 2", defaultValue = "Sample value", dataProvider = dataProvider)
    }

    private val togglePreference by lazy {
        KuteTogglePreference(id = idCounter.getAndIncrement(), key = R.string.key_demo_toggle_pref, name = "Sample Toggle Pref", defaultValue = false, onPreferenceChangedListener = { old, new ->
            Toast
                    .makeText(context, "Old: $old New: $new", Toast.LENGTH_SHORT)
                    .show()
        }, dataProvider = dataProvider)
    }

    override fun initPreferenceTree(): KutePreferencesTree {
        return KutePreferencesTree(listOf(SimpleKutePreferenceCategory(id = idCounter.getAndIncrement(), name = "Category 1", description = "Description of this category", childPreferences = listOf(SimpleKutePreferenceDivider(id = idCounter.getAndIncrement(), name = "Test Divider"), textPreference)), SimpleKutePreferenceCategory(id = idCounter.getAndIncrement(), name = "Category 2", description = "Description of this category", childPreferences = listOf(togglePreference)), textPreference2))
    }

}