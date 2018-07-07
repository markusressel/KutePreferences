package de.markusressel.kutepreferences.library.view.edit

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import de.markusressel.kutepreferences.library.R

/**
 * Base class for edit dialogs
 */
abstract class KutePreferenceEditDialogBase<DataType : Any> : KutePreferenceEditDialog<DataType> {

    /**
     * The dialog itself
     */
    protected var dialog: MaterialDialog? = null

    /**
     * Content layout resource to inflate
     */
    @get:LayoutRes
    abstract val contentLayoutRes: Int

    /**
     * The current input value of this dialog.
     * This might be altered from the persisted value of the preference item.
     */
    var currentValue: DataType? = null
        get() {
            if (field == null) {
                field = persistedValue
            }
            return field
        }
        set(newValue) {
            val oldValue = currentValue
            field = newValue

            if (oldValue != newValue) {
                onCurrentValueChanged(oldValue, currentValue, newValue == userInput)
            }
        }

    /**
     * helper variable to figure out if a "current value" change was initiated by user or system
     */
    lateinit var userInput: DataType

    override fun dismiss() {
        dialog
                ?.dismiss()
    }

    override fun restore() {
        currentValue = persistedValue
    }

    override fun save() {
        currentValue
                ?.let {
                    persistedValue = it
                }
    }

    /**
     * Resets the current input to the default value of the preference item
     */
    override fun resetToDefault() {
        currentValue = preferenceItem.getDefaultValue()
    }

    /**
     * Show this dialog
     */
    override fun show(context: Context) {
        userInput = persistedValue

        val layoutInflater = LayoutInflater
                .from(context)

        val dialogContentView = layoutInflater
                .inflate(contentLayoutRes, null)

        onContentViewCreated(context, layoutInflater, dialogContentView)

        dialog = MaterialDialog
                .Builder(context)
                .customView(dialogContentView, false)
                .title(preferenceItem.title)
                .neutralText(android.R.string.cancel)
                .onNeutral { _, _ ->
                    dismiss()
                }
                .negativeText(R.string.default_value)
                .onNegative { _, _ ->
                    resetToDefault()
                }
                .autoDismiss(false)
                .positiveText(R.string.save)
                .onPositive { _, _ ->
                    save()
                    dismiss()
                }
                .show()
    }

    /**
     * Called when the content view has been created.
     * Find your views here and set listeners, properties, etc.
     */
    abstract fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View)

}