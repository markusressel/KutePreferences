package de.markusressel.kutepreferences.library.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.R.id.*
import de.markusressel.kutepreferences.library.preference.KutePreferencesTree
import java.util.concurrent.TimeUnit

/**
 * The main class for all preferences.
 * All navigation between categories, subcategories and dividers is managed in here.
 */
abstract class KutePreferencesMainFragment : Fragment() {

    lateinit var kutePreferencesTree: KutePreferencesTree

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater
                .inflate(R.layout.kute_preference__main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        kutePreferencesTree = initPreferenceTree()
        showTopLevel()
    }

    override fun onResume() {
        super.onResume()

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
                                .findInSearchProviders(it.toString())
                                .map {
                                    it
                                            .key
                                }
                        replaceContent(preferenceIds)
                    }
                }, onError = {})
    }

    private fun showSearchResults() {
        kute_preferences__search_result_layout
                .visibility = View
                .VISIBLE
        kute_preferences__content_layout
                .visibility = View
                .GONE
    }

    private fun hideSearchResults() {
        kute_preferences__search_result_layout
                .visibility = View
                .GONE
        kute_preferences__content_layout
                .visibility = View
                .VISIBLE
    }

    private fun showTopLevel() {
        replaceContent(kutePreferencesTree.getTopLevelItems().map {
            it.key
        })
    }

    /**
     * Show a specific category
     *
     * @param category the category to show
     */
    fun showCategory(category: KutePreferenceListItem) {
        val preferenceIds = kutePreferencesTree
                .getCategoryItems(category.key)
                .map {
                    it.key
                }

        replaceContent(preferenceIds)
    }

    private fun replaceContent(preferenceIds: List<Int>) {
        val fragment = KutePreferencesContentFragment
                .newInstance(preferenceIds)

        childFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.kute_preferences__content_layout, fragment)
                .addToBackStack(null)
                .commit()
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
    open fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager
                    .popBackStack()
            true
        } else {
            false
        }
    }

}