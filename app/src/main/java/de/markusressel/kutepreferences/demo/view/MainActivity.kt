package de.markusressel.kutepreferences.demo.view

import android.os.Bundle
import de.markusressel.kutepreferences.demo.R
import de.markusressel.kutepreferences.demo.dagger.DaggerSupportActivityBase
import de.markusressel.kutepreferences.demo.preferences.KutePreferencesHolder
import javax.inject.Inject

class MainActivity : DaggerSupportActivityBase() {

    override val style: Int
        get() = DEFAULT
    override val layoutRes: Int
        get() = R.layout.activity_main

    private val preferencesFragment: PreferencesFragment = PreferencesFragment()

    @Inject
    lateinit var preferencesHolder: KutePreferencesHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, preferencesFragment)
                .commit()

        preferencesHolder.dataProvider.storeValueUnsafe(R.string.key_demo_text_pref_2,
                KutePreferencesHolder.TEXT_PREFERENCE_2_DEFAULT_VALUE)

        // somewhere in your app:
        val storedTextValue = preferencesHolder.textPreference2.persistedValue

        // retrieve a persisted value without using the preferenceItem
        // WARNING: this is not type safe unless you specify the correct default value
        val storedTextValueUnsafe: String = preferencesHolder.dataProvider.getValueUnsafe(R.string.key_demo_text_pref_2,
                KutePreferencesHolder.TEXT_PREFERENCE_2_DEFAULT_VALUE)

        // set a new valu
        preferencesHolder.textPreference2.persistedValue = "Tom Riddle"

        // store a value without using the preferenceItem directly
        // WARNING: this potentially has the power to completely change the type of the persisted value
        // if this is not prohibited somehow by the data provider.
        // USE WITH CAUTION AND ONLY IF NECESSARY
        preferencesHolder.dataProvider.storeValueUnsafe(R.string.key_demo_text_pref_2,
                "Markus Ressel")

        // this would also compile and run, breaking type correctness
        // of for later preference value accesses
//        preferencesHolder.dataProvider.storeValueUnsafe(R.string.key_demo_text_pref_2,
//                1000)

    }

    override fun onBackPressed() {
        if (preferencesFragment.onBackPressed()) {
            return
        }

        super
                .onBackPressed()
    }

}