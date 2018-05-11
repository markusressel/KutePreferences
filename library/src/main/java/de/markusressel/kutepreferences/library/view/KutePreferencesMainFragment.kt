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
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import de.markusressel.kutepreferences.library.view.event.CategoryEvent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.kute_preference__main_fragment.*
import java.util.concurrent.TimeUnit

/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers is managed in here.
 */
abstract class KutePreferencesMainFragment : Fragment() {

    lateinit var kutePreferencesTree: KutePreferencesTree

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)

        kutePreferencesTree = initPreferenceTree()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater
                .inflate(R.layout.kute_preference__main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super
                .onViewCreated(view, savedInstanceState)

        RxSearchView
                .queryTextChanges(kute_preferences__search)
                .skipInitialValue()
                .bindToLifecycle(kute_preferences__search)
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribeBy(onNext = {
                    if (it.isBlank()) {
                        showTopLevel()
                    } else {
                        val preferenceIds = kutePreferencesTree
                                .findPreferences(it.toString())
                                .map {
                                    it
                                            .id
                                }
                        replaceContent(preferenceIds)
                    }
                }, onError = {})

        showTopLevel()
    }

    override fun onStart() {
        super
                .onStart()
        Bus
                .observe<CategoryEvent>()
                .subscribe {
                    val preferenceIds = kutePreferencesTree
                            .getCategoryItems(it.category.id)
                            .map {
                                it
                                        .id
                            }

                    replaceContent(preferenceIds)
                }
                .registerInBus(this)
    }

    private fun showTopLevel() {
        replaceContent(kutePreferencesTree.items.map {
            it
                    .id
        })
    }

    private fun replaceContent(preferenceIds: List<Long>) {
        val fragment = KutePreferencesContentFragment
                .newInstance(preferenceIds)

        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
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
    abstract fun initPreferenceTree(): KutePreferencesTree

    /**
     * Call this from your activity's {@link AppCompatActivity.onBackPressed()} to ensure
     * back navigation behaviour is working es expected
     * @return true if a navigation happened, false otherwise
     */
    fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager
                    .popBackStack()
            true
        } else {
            false
        }
    }

}