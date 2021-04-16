package de.markusressel.kutepreferences.demo.dagger

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Created by Markus on 20.12.2017.
 */
abstract class DaggerSupportActivityBase : AppCompatActivity() {

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

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
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

        super.onCreate(savedInstanceState)

        // inflate view manually so it can be altered in plugins
        val contentView = layoutInflater.inflate(layoutRes, null)
        setContentView(contentView)

//        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Show the status bar
     */
    protected fun showStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * Hide the status bar
     */
    protected fun hideStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setLocale(locale: Locale) {
        val res = resources
        // Change locale settings in the app.
        val dm = res.displayMetrics
        val conf = res.configuration

        conf.locale = locale
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)

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
