package de.markusressel.kutepreferences.library.preference.text

import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase
import io.reactivex.rxkotlin.subscribeBy

class KuteTextPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        private val minLength: Int?,
        private val maxLength: Int?,
        private val regex: String?) :
        KutePreferenceEditDialogBase<String>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__text__edit_dialog

    private var editTextView: EditText? = null

    override fun onContentViewCreated(contentView: View) {
        editTextView = contentView
                .findViewById(R.id.kute_preferences__preference__text__edit_name)
        editTextView
                ?.setText(currentValue)

        userInput = persistedValue

        editTextView?.let {
            RxTextView.textChanges(it)
                    .bindToLifecycle(it)
                    .subscribeBy(
                            onNext = {
                                val newValue = it.toString()

                                userInput = newValue
                                currentValue = newValue
                            }

                    )
        }
    }

    override fun onCurrentValueChanged(oldValue: String?, newValue: String?, byUser: Boolean) {
        if (!byUser) {
            editTextView
                    ?.setText(newValue)
        }
    }

}