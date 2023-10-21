package de.markusressel.kutepreferences.demo.domain

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.ui.theme.itemShape
import de.markusressel.kutepreferences.ui.views.search.dummy

class CustomBooleanPreference(
    override val key: Int,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val icon: Drawable? = null,
    override val title: String,
    override val descriptionFunction: ((Boolean) -> String)? = null,
    private val defaultValue: Boolean,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null
) : KuteBooleanPreference(
    key = key,
    icon = icon,
    title = title,
    descriptionFunction = descriptionFunction,
    defaultValue = defaultValue,
    onClick = onClick,
    onLongClick = onLongClick,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CustomBooleanPreferencePreview() {
    CustomBooleanPreferenceView(
        behavior = CustomBooleanPreferenceBehavior(
            preferenceItem = CustomBooleanPreference(
                key = 0,
                onClick = {},
                onLongClick = {},
                dataProvider = dummy,
                title = "Custom Preference",
                defaultValue = true,
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBooleanPreferenceView(
    behavior: CustomBooleanPreferenceBehavior,
) {
    val checked by behavior.persistedValue.collectAsState()

    Card(
        modifier = Modifier
            .padding(8.dp),
        shape = itemShape,
        onClick = {
            behavior.preferenceItem.persistValue(behavior.persistedValue.value.not())
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {

                Text(
                    text = behavior.preferenceItem.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                val description =
                    remember { behavior.preferenceItem.createDescription(behavior.currentValue.value) }
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        fontSize = 12.sp
                    )
                }
            }

            Checkbox(
                checked = checked,
                onCheckedChange = { behavior.onInputChanged(it) })
        }
    }
}