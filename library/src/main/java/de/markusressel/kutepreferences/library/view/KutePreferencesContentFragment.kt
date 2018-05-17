package de.markusressel.kutepreferences.library.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eightbitlab.rxbus.Bus
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.library.view.event.CategoryEvent

/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers are managed in here.
 */
class KutePreferencesContentFragment : Fragment() {

    private val mainFragment by lazy { parentFragment as KutePreferencesMainFragment }


    private lateinit var rootLayout: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootLayout = layoutInflater.inflate(R.layout.kute_preference__content_fragment, container, false) as ViewGroup

        val preferenceItemIds = arguments
                ?.getIntArray(KEY_PREFERENCE_IDS)

        val preferenceItems: List<KutePreferenceListItem> = if (preferenceItemIds != null) {
            mainFragment
                    .kutePreferencesTree
                    .searchRecursive {
                        preferenceItemIds
                                .contains(it.key)
                    }
        } else {
            mainFragment
                    .kutePreferencesTree
                    .getTopLevelItems()
        }

        return generatePage(*preferenceItems.toTypedArray())
    }

    /**
     * Generates a ViewGroup for the given preferences
     */
    private fun generatePage(vararg kutePreference: KutePreferenceListItem): ViewGroup {
        // find the layout where list items should be inserted
        val listItemLayout: ViewGroup = rootLayout
                .findViewById(R.id.kute_preferences__list_item_root)
        listItemLayout
                .removeAllViews()

        val keySet: MutableSet<Int> = mutableSetOf()
        kutePreference
                .forEach {
                    // does NOT recurse through child items as we only want to inflate the current tree layer
                    inflate(it, listItemLayout, keySet)
                }

        return rootLayout
    }

    private fun inflate(kutePreferenceListItem: KutePreferenceListItem, layoutToAppendTo: ViewGroup,
                        keySet: MutableSet<Int>) {
        when (kutePreferenceListItem) {
            is KutePreferenceItem<*> -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
            is KutePreferenceCategory -> {
                checkKeyDuplication(kutePreferenceListItem.key, keySet)
            }
        }

        val layout: ViewGroup = inflate(kutePreferenceListItem)
        layoutToAppendTo.addView(layout)

        if (kutePreferenceListItem is KutePreferenceClickListener) {
            layout
                    .setOnClickListener {
                        kutePreferenceListItem
                                .onClick(layoutInflater.context)

                        if (kutePreferenceListItem is KutePreferenceCategory) {
                            Bus
                                    .send(CategoryEvent(kutePreferenceListItem))
                        }
                    }
        }
    }

    private fun checkKeyDuplication(key: Int, keySet: MutableSet<Int>) {
        if (keySet.contains(key)) {
            Log
                    .w("KutePreferences",
                            "Duplicate key '$key' found! Did you accidentally add a KutePreference twice or reused an existing key?")
        } else {
            keySet
                    .add(key)
        }
    }

    private fun inflate(it: KutePreferenceListItem): ViewGroup {
        return it
                .inflateListLayout(layoutInflater)
    }

    companion object {
        private const val KEY_PREFERENCE_IDS = "KEY_PREFERENCE_IDS"

        fun newInstance(preferenceIds: List<Int> = emptyList()): KutePreferencesContentFragment {
            val fragment = KutePreferencesContentFragment()
//            fragment
//                    .allowReturnTransitionOverlap = true
//            fragment
//                    .allowEnterTransitionOverlap = true

            val bundle = Bundle()
            bundle
                    .putIntArray(KEY_PREFERENCE_IDS, preferenceIds.toIntArray())
            fragment
                    .arguments = bundle

            return fragment
        }
    }

}