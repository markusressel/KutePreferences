package de.markusressel.kutepreferences.library

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener
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
    fun generatePage(vararg kutePreference: KutePreferenceListItem): ViewGroup {
        // find the layout where list items should be inserted
        val listItemLayout: ViewGroup = rootLayout.findViewById(R.id.kute_preferences__list_item_root)

        val keySet: MutableSet<Int> = mutableSetOf()
        kutePreference.forEach {
            // recurses through child items
            inflateAndAppend(it, listItemLayout, keySet)
        }

        return rootLayout
    }

    private fun inflateAndAppend(kutePreferenceListItem: KutePreferenceListItem, layoutToAppendTo: ViewGroup,
                                 keySet: MutableSet<Int>) {
        var layout: ViewGroup? = null
        when (kutePreferenceListItem) {
            is KutePreferenceCategory -> {
                layout = inflateAndAppend(kutePreferenceListItem, layoutToAppendTo)
                kutePreferenceListItem.getChildren().forEach {
                    inflateAndAppend(it, layoutToAppendTo, keySet)
                }
            }
            is KutePreferenceDivider -> {
                layout = inflateAndAppend(kutePreferenceListItem, layoutToAppendTo)
            }
            is KutePreferenceItem<*> -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
                layout = inflateAndAppend(kutePreferenceListItem, layoutToAppendTo)
            }
        }

        if (kutePreferenceListItem is KutePreferenceClickListener) {
            layout?.setOnClickListener {
                kutePreferenceListItem.onClick(layoutInflater.context)
            }
        }
    }

    private fun checkKeyDuplication(key: Int, keySet: MutableSet<Int>) {
        if (keySet.contains(key)) {
            Log.w("KutePreferences", "Duplicate key '$key' found! Did you accidentally add a KutePreference twice or reused an existing key?")
        } else {
            keySet.add(key)
        }
    }

    private fun inflateAndAppend(it: KutePreferenceCategory, layoutToAppendTo: ViewGroup): ViewGroup {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
        return layout
    }

    private fun inflateAndAppend(it: KutePreferenceDivider, layoutToAppendTo: ViewGroup): ViewGroup {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
        return layout
    }

    private fun inflateAndAppend(it: KutePreferenceItem<*>, layoutToAppendTo: ViewGroup): ViewGroup {
        val layout = it.inflateListLayout(layoutInflater)
        append(layout, layoutToAppendTo)
        return layout
    }

    private fun append(viewToAppend: ViewGroup, layoutToAppendTo: ViewGroup) {
        layoutToAppendTo.addView(viewToAppend)
    }

}