package de.markusressel.kutepreferences.library.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.preference.category.KutePreferenceCategory
import de.markusressel.kutepreferences.library.view.event.CategoryEvent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.kute_preference__main_fragment.*
import java.util.concurrent.TimeUnit

/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers are managed in here.
 */
abstract class KutePreferencesMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)

        PREFERENCE_TREE = initPreferenceTree()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater
                .inflate(R.layout.kute_preference__main_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super
                .onViewCreated(view, savedInstanceState)

        RxSearchView
                .queryTextChanges(kute_preferences__search)
                .bindToLifecycle(kute_preferences__search)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeBy(onNext = {
                    if (it.isBlank()) {
                        replaceContent(PREFERENCE_TREE)
                    } else {
                        val preferences = findPreferences(it.toString())
                        replaceContent(preferences)
                    }
                }, onError = {})

        replaceContent(PREFERENCE_TREE)
    }

    override fun onStart() {
        super
                .onStart()
        Bus
                .observe<CategoryEvent>()
                .subscribe {
                    replaceContent(it.category.getChildren())
                }
                .registerInBus(this)
    }

    private fun replaceContent(children: Array<KutePreferenceListItem>) {
        val fragment = KutePreferencesContentFragment
                .newInstance(children)

        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.kute_preferences__content_layout, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onStop() {
        super
                .onStop()
        Bus
                .unregister(this)
    }

    /**
     * Initialize your preferences tree here
     */
    abstract fun initPreferenceTree(): Array<KutePreferenceListItem>

    /**
     * Find preferences containing a given text
     */
    private fun findPreferences(text: String): Array<KutePreferenceListItem> {
        val result: MutableList<KutePreferenceListItem> = mutableListOf()

        fun filter(items: Array<KutePreferenceListItem>) {
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

        filter(PREFERENCE_TREE)

        return result
                .toTypedArray()
    }

    companion object {
        var PREFERENCE_TREE: Array<KutePreferenceListItem> = emptyArray()
    }

}