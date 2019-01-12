package de.markusressel.kutepreferences.core.tree

import androidx.annotation.StringRes
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchable
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.core.preference.section.KutePreferenceSection

class TreeManager(vararg items: KutePreferenceListItem) {

    private val topLevelItems = items.asList()
    private val tree = createTreeFromItems(items = topLevelItems)
    private val treeAsList = creatListOfAllItems(tree)

    private val itemsWithSearchProviders by lazy {
        treeAsList.asSequence().mapNotNull { it.item }.filter {
            it is KuteSearchable
        }.map {
            it as KuteSearchable
        }
    }

    /**
     * Traverses the whole tree and creates a list of all items in the tree
     */
    private fun creatListOfAllItems(treeItems: List<Node>): List<Node> {
        val result: MutableList<Node> = mutableListOf()

        fun traverse(items: List<Node>) {
            items.forEach {
                when (it.item) {
                    is KuteParent -> {
                        result.add(it)
                        traverse(it.children)
                    }
                    null -> {
                        // ignore
                    }
                    else -> result.add(it)
                }
            }
        }

        traverse(treeItems)

        return result
    }

    private fun createTreeFromItems(parent: Node? = null, items: List<KutePreferenceListItem>): List<Node> {
        return items.map {
            val node = Node(
                    parent = parent,
                    item = it
            )

            if (it is KuteParent) {
                node.children.addAll(createTreeFromItems(node, it.children))
            }

            node
        }
    }

    /**
     * Finds KuteSearchProviders and analyzes them for the given text
     *
     * @param text text to search for
     * @return list of items
     */
    fun findInSearchProviders(text: String): List<KutePreferenceListItem> {
        return itemsWithSearchProviders.filter {
            it.getSearchableItems().any { searchableText ->
                searchableText.contains(text, true)
            }
        }.map {
            it as KutePreferenceListItem
        }.toList()
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
    private fun getCategory(@StringRes key: Int, items: List<Node>): KutePreferenceCategory? {
        return items.asSequence().mapNotNull { it.item }.filter {
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
    fun findInTree(filter: (KutePreferenceListItem) -> Boolean): List<Node> {
        return treeAsList.filter {
            when {
                it.item != null -> filter(it.item)
                else -> false
            }
        }
    }

    /**
     * Get all top-level items of the tree
     */
    fun getTopLevelItems(): List<KutePreferenceListItem> {
        return topLevelItems
    }

    /**
     * Find the parent category of a section, if it has one
     * @param section the section to find the parent for (this must exist)
     */
    fun findParentCategory(section: KutePreferenceSection): KutePreferenceCategory? {
        val sectionInTree = findInTree {
            it.key == section.key
        }.first()

        return if (sectionInTree.parent?.item != null) {
            // section parent should always be a category
            sectionInTree.parent.item as KutePreferenceCategory
        } else {
            null
        }
    }

}
