package de.markusressel.kutepreferences.library.preference

import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.KuteSearchProvider
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory

class KutePreferencesTree(vararg items: KutePreferenceListItem) {

    private val topLevelItems = items.asList()
    private val treeAsList = creatListOfAllItems(topLevelItems)

    private fun creatListOfAllItems(treeItems: List<KutePreferenceListItem>): List<KutePreferenceListItem> {
        val result: MutableList<KutePreferenceListItem> = mutableListOf()

        fun traverse(items: List<KutePreferenceListItem>) {
            items.forEach {
                when (it) {
                    is KutePreferenceCategory -> with(it) {
                        result
                                .add(it)
                        traverse(it.children)
                    }
                    else -> {
                        result
                                .add(it)
                    }
                }
            }
        }

        traverse(treeItems)

        return result
    }

    /**
     * Find preferences containing a given text
     */
    fun findPreferences(text: String): List<KutePreferenceListItem> {
        return treeAsList.filter {
            it is KuteSearchProvider
        }.map {
            it as KuteSearchProvider
        }.filter {
            it.getSearchableItems().any {
                it.contains(text, true)
            }
        }.map {
            it as KutePreferenceListItem
        }
    }


    /**
     * Returns a list of all items in a category
     */
    fun getCategoryItems(@StringRes key: Int): List<KutePreferenceListItem> {
        val category = getCategory(key, treeAsList)
        category
                ?.let {
                    return it
                            .children
                }

        return emptyList()
    }

    private fun getCategory(@StringRes key: Int, items: List<KutePreferenceListItem>): KutePreferenceCategory? {
        return items.filter {
            it is KutePreferenceCategory
        }.filter {
            it.key == key
        }.map {
            it as KutePreferenceCategory
        }.firstOrNull()
    }

    fun searchRecursive(filter: (KutePreferenceListItem) -> Boolean): List<KutePreferenceListItem> {
        return treeAsList.filter {
            filter(it)
        }
    }

    fun getTopLevelItems(): List<KutePreferenceListItem> {
        return topLevelItems
    }

}
