package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import java.util.*

class FindItemStackUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, item: KutePreferenceListItem): Stack<KutePreferenceListItem> {
        return allItems.findItemStackByItem(item).first
    }

    private fun List<KutePreferenceListItem>.findItemStackByItem(target: KutePreferenceListItem, stack: Stack<KutePreferenceListItem> = Stack()): Pair<Stack<KutePreferenceListItem>, Boolean> {
        forEach {
            if (it.key == target.key) {
                return stack to true
            } else if (it is KuteParent) {
                stack.push(it)
                val subResult = (it as KuteParent).children.findItemStackByItem(target, stack)
                if (subResult.second) {
                    return subResult
                }
            }
        }
        throw NoSuchElementException("No element matching ${target.key} found")
    }
}
