package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import io.reactivex.rxkotlin.subscribeBy

open class KuteTextPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        regex: String?) :
        KutePreferenceEditDialogBase<String>() {

    protected val pattern = regex?.let {
        Regex(it)
    }

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__text__edit_dialog

    protected var editTextView: EditText? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        editTextView = contentView.findViewById(R.id.kute_preferences__preference__text__edit_name)
        userInput = persistedValue

        editTextView?.let {
            it.setText(currentValue)
            it.setSelection(it.text.length)
            RxTextView.textChanges(it)
                    .bindToLifecycle(it)
                    .subscribeBy(onNext = {
                        val newValue = it.toString()

                        userInput = newValue
                        currentValue = newValue
                    })
        }
    }

    override fun onCurrentValueChanged(oldValue: String?, newValue: String?, byUser: Boolean) {
        if (!byUser) {
            editTextView?.let {
                it.setText(newValue)
                it.setSelection(it.text.length)
            }
        }

        validate(newValue)
    }

    protected open fun isValid(value: String?, pattern: Regex?): Boolean {
        var isValid = true
        if (value == null) {
            isValid = false
        } else {
            pattern?.let {
                isValid = value.matches(it)
            }
        }

        return isValid
    }

    protected open fun validate(value: String?) {
        val positiveDialogButton = dialog?.getActionButton(WhichButton.POSITIVE)
        positiveDialogButton?.isEnabled = isValid(value, pattern)
    }

}