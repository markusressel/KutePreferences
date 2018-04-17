/*
 * DataMunch by Markus Ressel
 * Copyright (c) 2018.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.markusressel.kutepreferences.dagger

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject

/**
 * Created by Markus on 20.12.2017.
 */
abstract class DaggerSupportActivityBase : AppCompatActivity(), HasFragmentInjector,
        HasSupportFragmentInjector {

    @Inject
    internal lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    /**
     * @return true if this activity should use a dialog theme instead of a normal activity theme
     */
    @get:Style
    protected abstract val style: Int

    /**
     * The layout ressource for this Activity
     */
    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? {
        return frameworkFragmentInjector
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection
                .inject(this)

        // apply forced locale (if set in developer options)
//        initLocale()

        // set Theme before anything else in onCreate();
//        initTheme()

        if (style == FULLSCREEN) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            hideStatusBar()
        } else if (style == DIALOG) {
            // Hide title on dialogs to use view_toolbar instead
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        }

        super
                .onCreate(savedInstanceState)

        // inflate view manually so it can be altered in plugins
        val contentView = layoutInflater
                .inflate(layoutRes, null)
        setContentView(contentView)

//        setSupportActionBar(toolbar)

        supportActionBar
                ?.setDisplayHomeAsUpEnabled(true)

    }

    /**
     * Show the status bar
     */
    protected fun showStatusBar() {
        window
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * Hide the status bar
     */
    protected fun hideStatusBar() {
        window
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setLocale(locale: Locale) {
        val res = resources
        // Change locale settings in the app.
        val dm = res
                .displayMetrics
        val conf = res
                .configuration

        conf
                .locale = locale
        conf
                .setLocale(locale)
        res
                .updateConfiguration(conf, dm)

        onConfigurationChanged(conf)
    }

    @IntDef(DEFAULT, DIALOG)
    @kotlin.annotation.Retention
    annotation class Style

    companion object {

        /**
         * Normal activity style
         */
        const val DEFAULT = 0

        /**
         * Dialog style
         */
        const val DIALOG = 1

        /**
         * Fullscreen activity style
         */
        const val FULLSCREEN = 2
    }

}
