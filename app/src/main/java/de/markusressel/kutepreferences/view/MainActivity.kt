package de.markusressel.kutepreferences.view

import android.os.Bundle
import de.markusressel.kutepreferences.R
import de.markusressel.kutepreferences.dagger.DaggerSupportActivityBase

class MainActivity : DaggerSupportActivityBase() {

    override val style: Int
        get() = DEFAULT
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, PreferencesFragment())
                .commit()
    }

}