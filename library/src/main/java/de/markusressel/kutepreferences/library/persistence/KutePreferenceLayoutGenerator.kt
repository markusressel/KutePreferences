package de.markusressel.kutepreferences.library.persistence

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.view.KutePreferenceView

/**
 * Class to generate a preference page
 */
class KutePreferenceLayoutGenerator(context: Context) {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val rootLayout: ViewGroup by lazy {
        layoutInflater.inflate(R.layout.kute_preference_root, null, false) as ViewGroup
    }

    /**
     * Generates a ViewGroup for the given preferences
     */
    fun generatePage(vararg kutePreference: KutePreferenceView): ViewGroup {
        kutePreference.forEach {
            val view = it.inflateListLayout(layoutInflater)

            it.getChildren().forEach {
                inflateAndAppend(it, rootLayout)
            }

            rootLayout.addView(view)
        }

        return rootLayout
    }

    private fun inflateAndAppend(it: KutePreferenceView, rootLayout: ViewGroup) {
        rootLayout.addView(it.inflateListLayout(layoutInflater))
    }

}