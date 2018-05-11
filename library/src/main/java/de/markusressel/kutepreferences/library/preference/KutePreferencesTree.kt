package de.markusressel.kutepreferences.library.preference

import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory

class KutePreferencesTree(vararg items: KutePreferenceListItem) {

    val treeItems = items.asList()

    /**
     * Find preferences containing a given text
     */
    fun findPreferences(text: String): List<KutePreferenceListItem> {
        val result: MutableList<KutePreferenceListItem> = mutableListOf()

        fun filter(items: List<KutePreferenceListItem>) {
            items
                    .forEach {
                        when (it) {
                            is KutePreferenceItem<*> -> with(it) {
                                if (name.contains(text, true) or description.contains(text, true)) {
                                    result
                                            .add(it)
                                }
                            }
                            is KutePreferenceCategory -> with(it) {
                                if (it.name.contains(text, true) or it.description.contains(text, true)) {
                                    result
                                            .add(it)
                                }
                                filter(it.children)
                            }
                        }
                    }
        }

        filter(treeItems)

        return result
    }


    /**
     * Returns a list of all items in a category
     */
    fun getCategoryItems(@StringRes key: Int): List<KutePreferenceListItem> {
        val category = getCategory(key, treeItems)
        category
                ?.let {
                    return it
                            .children
                }

        return emptyList()
    }

    fun getCategory(@StringRes key: Int, items: List<KutePreferenceListItem>): KutePreferenceCategory? {
        items
                .forEach {
                    when (it) {
                        is KutePreferenceCategory -> {
                            if (it.key == key) {
                                return it
                            }
                            getCategory(key, it.children)
                        }
                    }
                }

        return null
    }

    fun searchRecursive(filter: (KutePreferenceListItem) -> Boolean): List<KutePreferenceListItem> {
        val result: MutableList<KutePreferenceListItem> = mutableListOf()

        fun innerFilter(items: List<KutePreferenceListItem>) {
            items
                    .forEach {
                        when (it) {

                            is KutePreferenceCategory -> with(it) {
                                if (filter(it)) {
                                    result
                                            .add(it)
                                }
                                innerFilter(it.children)
                            }
                            else -> {
                                if (filter(it)) {
                                    result
                                            .add(it)
                                }
                            }
                        }
                    }
        }

        innerFilter(treeItems)
        return result
    }

}
