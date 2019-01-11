package de.markusressel.kutepreferences.core.viewmodel.base

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.markusressel.kutepreferences.core.HighlighterFunction

/**
 * Base class for the viewmodel of a preference item's list entry
 */
abstract class PreferenceItemViewModel : ViewModel() {

    /**
     * The title of the preference item
     */
    val title = MutableLiveData<String>()

    /**
     * The description of the preference item
     * This should always represent the current value of the preference item
     * if it is not visualized in some other form
     */
    val description = MutableLiveData<String>()

    /**
     * Indicates the visibility of the description TextView
     */
    val descriptionVisibility = MutableLiveData<Int>()

    /**
     * The icon of the preference item
     */
    val icon = MutableLiveData<Drawable>()

    /**
     * Action for clicks
     */
    var onClick: View.OnClickListener? = null

    /**
     * Action for long clicks
     */
    var onLongClick: View.OnLongClickListener? = null

    init {
        description.observeForever {
            // link description view visibility to the description content
            descriptionVisibility.value = if (it.isBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    /**
     * Highlights all searchable content with the given highlighter function.
     *
     * @param highlighterFunction the function to apply to text fields
     */
    abstract fun highlight(highlighterFunction: HighlighterFunction)

}