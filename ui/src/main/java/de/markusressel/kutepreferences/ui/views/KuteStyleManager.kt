package de.markusressel.kutepreferences.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.preference.KuteItemBehavior
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.ActionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.BooleanPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteCategoryBehavior
import de.markusressel.kutepreferences.core.preference.color.ColorPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.color.KuteColorPreference
import de.markusressel.kutepreferences.core.preference.date.DatePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.section.KuteSectionBehavior
import de.markusressel.kutepreferences.core.preference.select.KuteMultiSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.KuteSingleSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.MultiSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.select.SingleSelectionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.*
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePreferenceBehavior
import de.markusressel.kutepreferences.ui.views.listitems.*

object BehaviorStore {

    private val behaviorMap = mutableMapOf<KutePreferenceListItem, KuteItemBehavior>()

    fun get(item: KutePreferenceListItem): KuteItemBehavior? {
        return behaviorMap[item]
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : KutePreferenceListItem, K : KuteItemBehavior> get(item: T, producer: (T) -> K): K {
        return behaviorMap.getOrPut(item) { producer(item) } as K
    }

}

/**
 * Global place for the associations between [KutePreferenceListItem] implementations
 * and their UI.
 */
object KuteStyleManager {

    private val hooks = mutableListOf<@Composable (item: KutePreferenceListItem) -> Boolean>()

    /**
     * Registers the built-in components
     *
     * @param navigator a [KuteNavigator] used f.ex. for navigation between categories
     */
    fun registerDefaultStyles(navigator: KuteNavigator) = registerTypeHook {
        when (it) {
            is KuteAction -> {
                val behavior = BehaviorStore.get(it) { ActionPreferenceBehavior(it) }
                ActionPreference(
                    behavior = behavior
                )
                true
            }

            is KuteSection -> {
                val behavior = BehaviorStore.get(it) { KuteSectionBehavior(preferenceItem = it) }
                KuteSectionView(
                    behavior = behavior
                )
                true
            }

            is KuteCategory -> {
                val behavior = BehaviorStore.get(it) {
                    KuteCategoryBehavior(
                        preferenceItem = it,
                        navigator = navigator,
                    )
                }
                KuteCategoryView(
                    behavior = behavior
                )
                true
            }

            is KuteBooleanPreference -> {
                val behavior = BehaviorStore.get(it) { BooleanPreferenceBehavior(it) }
                BooleanPreference(
                    behavior = behavior
                )
                true
            }

            is KuteColorPreference -> {
                val behavior = BehaviorStore.get(it) { ColorPreferenceBehavior(it) }
                ColorPreferenceView(
                    behavior = behavior
                )
                true
            }
            // NOTE: must be checked before KuteTextPreference
            is KuteUrlPreference -> {
                val behavior = BehaviorStore.get(it) { UrlPreferenceBehavior(it) }
                UrlPreference(
                    behavior = behavior
                )
                true
            }
            // NOTE: must be checked before KuteTextPreference
            is KutePasswordPreference -> {
                val behavior = BehaviorStore.get(it) { PasswordPreferenceBehavior(it) }
                PasswordPreference(
                    behavior = behavior
                )
                true
            }

            is KuteTextPreference -> {
                val behavior = BehaviorStore.get(it) { TextPreferenceBehavior(it) }
                TextPreference(
                    behavior = behavior
                )
                true
            }

            is KuteNumberPreference -> {
                val behavior = BehaviorStore.get(it) { NumberPreferenceBehavior(it) }
                NumberPreference(
                    behavior = behavior
                )
                true
            }

            is KuteFloatRangePreference -> {
                val behavior = BehaviorStore.get(it) { FloatRangeSliderPreferenceBehavior(it) }
                NumberRangeSliderPreference(
                    behavior = behavior
                )
                true
            }

            is KuteIntSliderPreference -> {
                val behavior = BehaviorStore.get(it) { IntSliderPreferenceBehavior(it) }
                NumberSliderPreference(
                    behavior = behavior,
                )
                true
            }

            is KuteFloatSliderPreference -> {
                val behavior = BehaviorStore.get(it) { FloatSliderPreferenceBehavior(it) }
                DecimalSliderPreference(
                    behavior = behavior,
                )
                true
            }

            is KuteDatePreference -> {
                val behavior = BehaviorStore.get(it) { DatePreferenceBehavior(it) }
                DatePreference(
                    behavior = behavior,
                )
                true
            }

            is KuteTimePreference -> {
                val behavior = BehaviorStore.get(it) { TimePreferenceBehavior(it) }
                TimePreference(
                    behavior = behavior,
                )
                true
            }

            is KuteSingleSelectStringPreference -> {
                val behavior = BehaviorStore.get(it) { SingleSelectionPreferenceBehavior(it) }
                SingleSelectionPreference(
                    behavior = behavior,
                )
                true
            }

            is KuteMultiSelectStringPreference -> {
                val behavior = BehaviorStore.get(it) { MultiSelectionPreferenceBehavior(it) }
                MultiSelectionPreference(
                    behavior = behavior,
                )
                true
            }


            else -> {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Unsupported list item type: ${it.javaClass.simpleName}")
                    Text(text = "Did you forget to register it on KuteStyleManager?")
                }

                true
            }
        }
    }

    /**
     * Register a type hook function, which will be used to instantiate the UI for
     * a given [KutePreferenceListItem] type.
     *
     * Type hooks are called in reverse order of registration, which lets you override existing
     * hooks (like the built-in one).
     *
     * To make custom preference types show your custom UI, you have to register them with the
     * [KuteStyleManager] using this method. You can also use this to manipulate the UI of existing
     * types by using the above mentioned override mechanism.
     *
     * @param hook function which (optionally) renders the UI for a given preference item.
     *             returns true if it handled the given item, false otherwise.
     */
    fun registerTypeHook(
        hook: @Composable (item: KutePreferenceListItem) -> Boolean,
    ) {
        hooks.add(hook)
    }

    @Composable
    fun Content(
        item: KutePreferenceListItem,
    ) {
        for (hook in hooks.reversed()) {
            val result = hook(item)
            if (result) {
                return
            }
        }

        Log.w(
            "KuteStyleManager", "No hook with positive return value while trying " +
            "to render: ${item::class.java}"
        )
    }

}