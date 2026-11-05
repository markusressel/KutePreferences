package de.markusressel.kutepreferences.ui.views

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.ActionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.BooleanPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.bool.KuteMainSwitchPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteCategoryBehavior
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.core.preference.color.ColorPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import de.markusressel.kutepreferences.core.preference.date.DatePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.IntRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.core.preference.section.KutePreferenceSection
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.section.KuteSectionBehavior
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.KuteSingleSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.MultiSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.select.SingleSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePreferenceBehavior
import de.markusressel.kutepreferences.ui.views.listitems.ActionPreference
import de.markusressel.kutepreferences.ui.views.listitems.BooleanPreference
import de.markusressel.kutepreferences.ui.views.listitems.ColorPreferenceView
import de.markusressel.kutepreferences.ui.views.listitems.DatePreference
import de.markusressel.kutepreferences.ui.views.listitems.DecimalSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.IntRangeSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.KuteCategoryView
import de.markusressel.kutepreferences.ui.views.listitems.KuteSectionView
import de.markusressel.kutepreferences.ui.views.listitems.MainSwitchPreference
import de.markusressel.kutepreferences.ui.views.listitems.MultiSelectionPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberRangeSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.NumberSliderPreference
import de.markusressel.kutepreferences.ui.views.listitems.PasswordPreference
import de.markusressel.kutepreferences.ui.views.listitems.SingleSelectionPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import de.markusressel.kutepreferences.ui.views.listitems.TimePreference
import de.markusressel.kutepreferences.ui.views.listitems.UrlPreference
import java.util.IdentityHashMap

/**
 * The associations between [KutePreferenceListItem] types and their behavior and UI,
 * as well as the behaviors of the items rendered with it.
 *
 * The built-in types are registered by default. Registering a type again replaces its previous registration.
 * Items of a type without a registration use the registration of their closest registered supertype.
 *
 * @param navigator used f.ex. for navigation between categories
 */
class KuteRegistry(navigator: KuteNavigator) {

    private class Registration<I : KutePreferenceListItem, B : KuteItemBehavior>(
        val createBehavior: ((I) -> B)?,
        val content: @Composable (I, B?) -> Unit,
    )

    private val registrations = mutableMapOf<Class<*>, Registration<*, *>>()
    private val behaviors = IdentityHashMap<KutePreferenceListItem, KuteItemBehavior>()
    private val mainSwitchConditions = MainSwitchConditions()

    init {
        registerDefaults(navigator)
    }

    /**
     * Registers the UI of an item type that keeps its state in a [KuteItemBehavior].
     *
     * @param behavior creates the behavior of an item. It is called once per item.
     * @param content renders the behavior
     */
    inline fun <reified I : KutePreferenceListItem, B : KuteItemBehavior> register(
        noinline behavior: (I) -> B,
        noinline content: @Composable (B) -> Unit,
    ) = add(I::class.java, behavior, content)

    /**
     * Registers the UI of an item type that does not need a [KuteItemBehavior].
     *
     * @param content renders the item
     */
    inline fun <reified I : KutePreferenceListItem> register(
        noinline content: @Composable (I) -> Unit,
    ) = add(I::class.java, content)

    @PublishedApi
    internal fun <I : KutePreferenceListItem, B : KuteItemBehavior> add(
        type: Class<I>,
        behavior: (I) -> B,
        content: @Composable (B) -> Unit,
    ) {
        registrations[type] = Registration<I, B>(behavior) { _, itemBehavior -> content(itemBehavior!!) }
    }

    @PublishedApi
    internal fun <I : KutePreferenceListItem> add(
        type: Class<I>,
        content: @Composable (I) -> Unit,
    ) {
        registrations[type] = Registration<I, KuteItemBehavior>(null) { item, _ -> content(item) }
    }

    /**
     * @return the behavior of the given [item], or null if its type does not use one
     */
    fun behaviorFor(item: KutePreferenceListItem): KuteItemBehavior? {
        return behaviors[item] ?: createBehavior(item)?.also { behaviors[item] = it }
    }

    private fun createBehavior(item: KutePreferenceListItem): KuteItemBehavior? {
        @Suppress("UNCHECKED_CAST")
        val registration = findRegistration(item.javaClass) as Registration<KutePreferenceListItem, KuteItemBehavior>?
        return registration?.createBehavior?.invoke(item)
    }

    /**
     * Finds the items that are disabled by a main switch
     */
    internal fun setPreferenceTree(items: List<KutePreferenceListItem>) {
        mainSwitchConditions.update(items)
    }

    /**
     * Renders the given [item]
     */
    @Composable
    fun Content(item: KutePreferenceListItem) {
        @Suppress("UNCHECKED_CAST")
        val registration = findRegistration(item.javaClass) as Registration<KutePreferenceListItem, KuteItemBehavior>?

        ConditionalItem(item, mainSwitchConditions[item]) {
            when (registration) {
                null -> UnsupportedItem(item)
                else -> registration.content(item, behaviorFor(item))
            }
        }
    }

    private fun findRegistration(type: Class<*>): Registration<*, *>? {
        val classes = generateSequence(type) { it.superclass }.toList()
        val interfaces = classes.flatMap { allInterfaces(it) }
        return (classes + interfaces).firstNotNullOfOrNull { registrations[it] }
    }

    private fun allInterfaces(type: Class<*>): List<Class<*>> {
        return type.interfaces.flatMap { listOf(it) + allInterfaces(it) }
    }

    private fun registerDefaults(navigator: KuteNavigator) {
        register<KuteAction, ActionPreferenceBehavior>(::ActionPreferenceBehavior) { ActionPreference(it) }
        register<KuteSection, KuteSectionBehavior>(::KuteSectionBehavior) { KuteSectionView(it) }
        register<KuteCategory, KuteCategoryBehavior>({ KuteCategoryBehavior(it, navigator) }) { KuteCategoryView(it) }
        register<KuteBooleanPreference, BooleanPreferenceBehavior>(::BooleanPreferenceBehavior) {
            BooleanPreference(it)
        }
        register<KuteMainSwitchPreference, BooleanPreferenceBehavior>({ BooleanPreferenceBehavior(it) }) {
            MainSwitchPreference(it)
        }
        register<KuteColorPreference, ColorPreferenceBehavior>(::ColorPreferenceBehavior) {
            ColorPreferenceView(it)
        }
        register<KuteUrlPreference, UrlPreferenceBehavior>(::UrlPreferenceBehavior) { UrlPreference(it) }
        register<KutePasswordPreference, PasswordPreferenceBehavior>(::PasswordPreferenceBehavior) {
            PasswordPreference(it)
        }
        register<KuteTextPreference, TextPreferenceBehavior>(::TextPreferenceBehavior) { TextPreference(it) }
        register<KuteNumberPreference, NumberPreferenceBehavior>(::NumberPreferenceBehavior) { NumberPreference(it) }
        register<KuteIntRangePreference, IntRangeSliderPreferenceBehavior>(::IntRangeSliderPreferenceBehavior) {
            IntRangeSliderPreference(it)
        }
        register<KuteFloatRangePreference, FloatRangeSliderPreferenceBehavior>(::FloatRangeSliderPreferenceBehavior) {
            NumberRangeSliderPreference(it)
        }
        register<KuteIntSliderPreference, IntSliderPreferenceBehavior>(::IntSliderPreferenceBehavior) {
            NumberSliderPreference(it)
        }
        register<KuteFloatSliderPreference, FloatSliderPreferenceBehavior>(::FloatSliderPreferenceBehavior) {
            DecimalSliderPreference(it)
        }
        register<KuteDatePreference, DatePreferenceBehavior>(::DatePreferenceBehavior) { DatePreference(it) }
        register<KuteTimePreference, TimePreferenceBehavior>(::TimePreferenceBehavior) { TimePreference(it) }
        register<KuteChoicePreference, SingleSelectionPreferenceBehavior>(::SingleSelectionPreferenceBehavior) {
            SingleSelectionPreference(it)
        }
        register<KuteMultiChoicePreference, MultiSelectionPreferenceBehavior>(::MultiSelectionPreferenceBehavior) {
            MultiSelectionPreference(it)
        }
        register<KuteSingleSelectStringPreference, SingleSelectionPreferenceBehavior>(
            ::SingleSelectionPreferenceBehavior
        ) {
            SingleSelectionPreference(it)
        }
        register<KuteMultiSelectStringPreference, MultiSelectionPreferenceBehavior>(
            ::MultiSelectionPreferenceBehavior
        ) {
            MultiSelectionPreference(it)
        }
    }

}

@Composable
private fun UnsupportedItem(item: KutePreferenceListItem) {
    Log.w("KuteRegistry", "No registration found for ${item.javaClass}")
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Unsupported list item type: ${item.javaClass.simpleName}")
        Text(text = "Did you forget to register it on the KuteRegistry?")
    }
}

/**
 * Applies [KutePreferenceListItem.visibleWhen], [KutePreferenceListItem.enabledWhen] and the state
 * of the main switch of the item, if any, to the given [content]
 */
@Composable
private fun ConditionalItem(
    item: KutePreferenceListItem,
    mainSwitchCondition: KuteCondition?,
    content: @Composable () -> Unit,
) {
    val isVisible = item.visibleWhen.collectIsMet()
    val isEnabled = item.enabledWhen.collectIsMet() && mainSwitchCondition.collectIsMet()

    // starts in the current state, so that an item is not animated when it first appears
    val visibility = remember { MutableTransitionState(isVisible) }
    visibility.targetState = isVisible

    AnimatedVisibility(visibleState = visibility) {
        Box(
            modifier = when {
                isEnabled -> Modifier
                else -> Modifier.disabledItem(item)
            },
        ) {
            content()
        }
    }
}

@Composable
private fun KuteCondition?.collectIsMet(): Boolean {
    if (this == null) {
        return true
    }
    val isMet by changes.collectAsStateWithLifecycle(initialValue = isMet)
    return isMet
}

/**
 * Blocks touch input, keyboard focus and accessibility actions of the content, which can be any custom view.
 */
private fun Modifier.disabledItem(item: KutePreferenceListItem): Modifier = this
    .alpha(DISABLED_ITEM_ALPHA)
    .focusProperties { onEnter = { cancelFocusChange() } }
    .focusGroup()
    .clearAndSetSemantics {
        disabled()
        item.label?.let { contentDescription = it }
    }
    .pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
            }
        }
    }

private val KutePreferenceListItem.label: String?
    get() = when (this) {
        is KutePreferenceItem<*> -> title
        is KutePreferenceCategory -> title
        is KutePreferenceSection -> title
        is KuteAction -> title
        else -> null
    }

private const val DISABLED_ITEM_ALPHA = 0.38f

/**
 * The [KuteRegistry] used to render items. `KutePreferencesScreen` provides the one of its ViewModel.
 */
val LocalKuteRegistry = staticCompositionLocalOf<KuteRegistry> {
    error("No KuteRegistry provided. Render items inside of KutePreferencesScreen, or provide LocalKuteRegistry.")
}

/**
 * Renders this item using the [LocalKuteRegistry]
 */
@Composable
fun KutePreferenceListItem.Composable() {
    LocalKuteRegistry.current.Content(this)
}

/**
 * Provides a [LocalKuteRegistry] for compose previews
 */
@Composable
fun KuteRegistryForPreview(content: @Composable () -> Unit) {
    val registry = remember { KuteRegistry(DefaultKuteNavigator()) }
    CompositionLocalProvider(LocalKuteRegistry provides registry, content)
}
