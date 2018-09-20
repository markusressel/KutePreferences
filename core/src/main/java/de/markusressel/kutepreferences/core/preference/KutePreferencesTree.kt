package de.markusressel.kutepreferences.core.preference

import android.support.annotation.StringRes
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory

class KutePreferencesTree(vararg items: KutePreferenceListItem) {

    private val topLevelItems = items.asList()
    private val treeAsList = creatListOfAllItems(topLevelItems)

    /**
     * Traverses the whole tree and creates a list of all items in the tree
     */
    private fun creatListOfAllItems(treeItems: List<KutePreferenceListItem>): List<KutePreferenceListItem> {
        val result: MutableList<KutePreferenceListItem> = mutableListOf()

        fun traverse(items: List<KutePreferenceListItem>) {
            items.forEach {
                when (it) {
                    is KuteParent -> with(it) {
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
     * Finds KuteSearchProviders and analyzes them for the given text
     *
     * @param text text to search for
     * @return list of items
     */
    fun findInSearchProviders(text: String): List<KutePreferenceListItem> {
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
     *
     * @param categoryKey key of the category
     * @return list of items
     */
    fun getCategoryItems(@StringRes categoryKey: Int): List<KutePreferenceListItem> {
        val category = getCategory(categoryKey, treeAsList)
        return category?.children ?: emptyList()
    }

    /**
     * Find a category somewhere in the tree
     */
    private fun getCategory(@StringRes key: Int, items: List<KutePreferenceListItem>): KutePreferenceCategory? {
        return items.filter {
            it is KutePreferenceCategory
        }.filter {
            it.key == key
        }.map {
            it as KutePreferenceCategory
        }.firstOrNull()
    }

    /**
     *
     */
    fun findInTree(filter: (KutePreferenceListItem) -> Boolean): List<KutePreferenceListItem> {
        return treeAsList.filter {
            filter(it)
        }
    }

    /**
     * Get all top-level items of the tree
     */
    fun getTopLevelItems(): List<KutePreferenceListItem> {
        return topLevelItems
    }

}
