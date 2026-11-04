package de.markusressel.kutepreferences.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultKuteNavigator : KuteNavigator {

    private val openCategories = ArrayDeque<String>()

    private val _currentCategory = MutableStateFlow<String?>(null)
    override val currentCategory: StateFlow<String?> = _currentCategory.asStateFlow()

    override val categoryStack: List<String>
        get() = openCategories.toList()

    override fun enterCategory(key: String) {
        openCategories.addLast(key)
        _currentCategory.value = key
    }

    override fun setCategories(keys: List<String>) {
        openCategories.clear()
        openCategories.addAll(keys)
        _currentCategory.value = openCategories.lastOrNull()
    }

    override fun backToTop() {
        setCategories(emptyList())
    }

    override fun goBack(): Boolean {
        if (openCategories.isEmpty()) {
            return false
        }

        openCategories.removeLast()
        _currentCategory.value = openCategories.lastOrNull()
        return true
    }

}
