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
     * The dialog
     */
    private var dialog: MaterialDialog? = null

    /**
     * Content layout resource to in
     */
    @get:LayoutRes
    abstract val contentLayoutRes: Int

    /**
     * The current value of this preference
     * that might be altered from the persisted value
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
            onCurrentValueChanged(oldValue, currentValue)
        }

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

    override fun reset() {
        currentValue = preferenceItem
                .defaultValue
    }

    /**
     * Show this dialog
     */
    override fun show(context: Context) {
        val layoutInflater = LayoutInflater
                .from(context)

        val dialogContentView = layoutInflater
                .inflate(contentLayoutRes, null)

        onContentViewCreated(dialogContentView)

        dialog = MaterialDialog
                .Builder(context)
                .customView(dialogContentView, false)
                .title(preferenceItem.name)
                .neutralText(android.R.string.cancel)
                .onNeutral { dialog, which ->
                    dismiss()
                }
                .negativeText(R.string.reset)
                .onNegative { dialog, which ->
                    reset()
                }
                .autoDismiss(false)
                .positiveText(R.string.save)
                .onPositive { dialog, which ->
                    save()
                    dismiss()
                }
                .show()
    }

    /**
     * Called when the content view has been created.
     * Find your views here and set listeners, properties, etc.
     */
    abstract fun onContentViewCreated(contentView: View)

}