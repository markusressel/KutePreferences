package de.markusressel.kutepreferences.library

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceDivider

/**
 * Class to generate a preference page
 */
class KutePreferenceLayoutGenerator(context: Context) {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private val rootLayout: ViewGroup by lazy {
        layoutInflater.inflate(R.layout.kute_preference__root, null, false) as ViewGroup
    }

    /**
     * Generates a ViewGroup for the given preferences
     */
    fun generatePage(vararg kutePreference: de.markusressel.kutepreferences.library.KutePreferenceListItem): ViewGroup {
        // find the layout where list items should be inserted
        val listItemLayout: ViewGroup = rootLayout.findViewById(R.id.kute_preferences__list_item_root)

        kutePreference.forEach {
            // recurses through child items
            inflateAndAppend(it, listItemLayout)
        }

        return rootLayout
    }

    private fun inflateAndAppend(it: de.markusressel.kutepreferences.library.KutePreferenceListItem, layoutToAppendTo: ViewGroup) {
        when (it) {
            is KutePreferenceCategory -> {
                inflateAndAppend(it, layoutToAppendTo)
                it.getChildren().forEach {
                    inflateAndAppend(it, layoutToAppendTo)
                }
            }
            is KutePreferenceDivider -> {
                inflateAndAppend(it, layoutToAppendTo)
            }
            is KutePreferenceItem<*> -> {
                inflateAndAppend(it, layoutToAppendTo)
            }
        }
    }

    private fun inflateAndAppend(it: KutePreferenceCategory, layoutToAppendTo: ViewGroup) {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
    }

    private fun inflateAndAppend(it: KutePreferenceDivider, layoutToAppendTo: ViewGroup) {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
    }

    private fun inflateAndAppend(it: KutePreferenceItem<*>, layoutToAppendTo: ViewGroup) {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
    }

    private fun append(viewToAppend: ViewGroup, layoutToAppendTo: ViewGroup) {
        layoutToAppendTo.addView(viewToAppend)
    }

}