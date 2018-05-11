package de.markusressel.kutepreferences.library.preference

import android.support.annotation.StringRes
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory

class KutePreferencesTree(val items: List<KutePreferenceListItem>) {

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
                                filter(it.getChildren())
                            }
                        }
                    }
        }

        filter(items)

        return result
    }


    /**
     * Returns a list of all items in a category
     */
    fun getCategoryItems(@StringRes key: Int): List<KutePreferenceListItem> {
        val category = getCategory(key, items)
        category
                ?.let {
                    return it
                            .getChildren()
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
                            getCategory(key, it.getChildren())
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
                                innerFilter(it.getChildren())
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

        innerFilter(items)
        return result
    }

}
