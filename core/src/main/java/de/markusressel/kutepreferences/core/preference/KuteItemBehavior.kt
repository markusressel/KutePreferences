package de.markusressel.kutepreferences.core.preference

interface KuteItemBehavior {

    /**
     * Instructs the item to highlight itself
     */
    fun highlight()

    /**
     * Instructs the item to stop highlighting itself. The UI calls this when the highlight animation has finished.
     */
    fun clearHighlight() {}

}

/**
 * Behavior of an item that can hide its children, and has to show them when one of them is navigated to.
 */
interface KuteExpandable {

    /**
     * Instructs the item to show its children
     */
    fun expand()

}
