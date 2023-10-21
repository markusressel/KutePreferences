package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import java.util.*

class FindItemStackUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, item: KutePreferenceListItem): List<KutePreferenceListItem> {
        val (stack, found) = allItems.findItemStackByItem(item)
        if (!found) {
            throw NoSuchElementException("No element matching ${item.key} found")
        } else {
            return stack.reversed()
        }
    }

    private fun List<KutePreferenceListItem>.findItemStackByItem(target: KutePreferenceListItem, stack: Stack<KutePreferenceListItem> = Stack()): Pair<Stack<KutePreferenceListItem>, Boolean> {
        forEach {
            val currentStack = Stack<KutePreferenceListItem>()

            if (it.key == target.key) {
                currentStack.push(it)
                stack.addAll(currentStack)
                return stack to true
            } else if (it is KuteParent) {
                currentStack.push(it)
                val (subStack, found) = (it as KuteParent).children.findItemStackByItem(target)
                if (found) {
                    stack.addAll(subStack)
                    stack.addAll(currentStack)
                    return stack to found
                }
            }
        }
        return stack to false
    }
}
