package de.markusressel.kutepreferences.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultKuteNavigator : KuteNavigator {

    private val categoryStack = ArrayDeque<String>()

    private val _currentCategory = MutableStateFlow<String?>(null)
    override val currentCategory: StateFlow<String?> = _currentCategory.asStateFlow()

    override fun enterCategory(key: String) {
        categoryStack.addLast(key)
        _currentCategory.value = key
    }

    override fun setCategories(keys: List<String>) {
        categoryStack.clear()
        categoryStack.addAll(keys)
        _currentCategory.value = categoryStack.lastOrNull()
    }

    override fun backToTop() {
        setCategories(emptyList())
    }

    override fun goBack(): Boolean {
        if (categoryStack.isEmpty()) {
            return false
        }

        categoryStack.removeLast()
        _currentCategory.value = categoryStack.lastOrNull()
        return true
    }

}
