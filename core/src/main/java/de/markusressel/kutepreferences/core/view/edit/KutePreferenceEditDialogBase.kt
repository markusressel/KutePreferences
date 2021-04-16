package de.markusressel.kutepreferences.core.view.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import de.markusressel.kutepreferences.core.R

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
        dialog?.dismiss()
    }

    override fun restore() {
        currentValue = persistedValue
    }

    override fun save() {
        currentValue?.let {
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
     * Get the content view of the dialog
     */
    val contentView: View?
        get() = dialog?.getCustomView()

    /**
     * Show this dialog
     */
    override fun show(context: Context) {
        userInput = persistedValue

        val layoutInflater = LayoutInflater.from(context)
        val contentView = layoutInflater.inflate(contentLayoutRes, null)

        dialog = MaterialDialog(context).customView(view = contentView, scrollable = false)
                .noAutoDismiss()
                .title(text = preferenceItem.title)
                .neutralButton(res = android.R.string.cancel, click = {
                    dismiss()
                })
                .negativeButton(res = R.string.default_value, click = {
                    resetToDefault()
                })
                .positiveButton(res = R.string.save, click = {
                    save()
                    dismiss()
                })

        onContentViewCreated(context, layoutInflater, contentView)
        dialog?.show()
    }

    /**
     * Called when the content view has been created.
     * Find your views here and set listeners, properties, etc.
     */
    abstract fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View)

}