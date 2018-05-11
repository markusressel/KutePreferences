package de.markusressel.kutepreferences.view

import android.os.Bundle
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.dagger.DaggerSupportActivityBase

class MainActivity : DaggerSupportActivityBase() {

    override val style: Int
        get() = DEFAULT
    override val layoutRes: Int
        get() = R.layout.activity_main

    private val preferencesFragment: PreferencesFragment = PreferencesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, preferencesFragment)
                .commit()
    }

    override fun onBackPressed() {
        if (preferencesFragment.onBackPressed()) {
            return
        }

        super
                .onBackPressed()
    }

}