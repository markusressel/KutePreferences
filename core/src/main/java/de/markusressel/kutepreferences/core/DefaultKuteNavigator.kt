package de.markusressel.kutepreferences.core

import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class DefaultKuteNavigator : KuteNavigator {

    override val currentCategory = MutableStateFlow<Int?>(null)
    private val categoryStack = Stack<Int>()

    override fun enterCategory(key: Int) {
        categoryStack.push(key)
        currentCategory.value = key
    }

    override fun navigateTo(key: Int) {
        TODO("Not yet implemented")
    }

    override fun goBack(): Boolean {
        try {
            categoryStack.pop()
        } catch (_: EmptyStackException) {
        }

        val category = try {
            categoryStack.peek()
        } catch (ex: EmptyStackException) {
            null
        }

        return when {
            category == null && currentCategory.value == null -> false
            else -> {
                currentCategory.value = category
                true
            }
        }
    }

}