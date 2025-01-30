package com.sakethh.linkora.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sakethh.linkora.common.Localization
import com.sakethh.linkora.common.utils.rememberLocalizedString
import com.sakethh.linkora.ui.utils.pulsateEffect


data class AddANewPanelParam(
    val isDialogBoxVisible: MutableState<Boolean>,
    val onCreateClick: (shelfName: String, onCompletion: () -> Unit) -> Unit,
)

@Composable
fun AddANewPanelDialogBox(addANewPanelParam: AddANewPanelParam) {
    if (addANewPanelParam.isDialogBoxVisible.value) {
        val customShelfName = rememberSaveable {
            mutableStateOf("")
        }
        val isInProgress = rememberSaveable {
            mutableStateOf(false)
        }
        AlertDialog(title = {
            Text(
                text = Localization.Key.AddANewPanel.rememberLocalizedString(),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 22.sp,
                lineHeight = 28.sp
            )
        }, onDismissRequest = {
            if (isInProgress.value.not()) {
                addANewPanelParam.isDialogBoxVisible.value = false
            }
        }, text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    label = {
                        Text(
                            text = Localization.Key.PanelName.rememberLocalizedString(),
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 12.sp
                        )
                    },
                    textStyle = MaterialTheme.typography.titleSmall,
                    singleLine = true,
                    value = customShelfName.value,
                    onValueChange = {
                        customShelfName.value = it
                    }, readOnly = isInProgress.value
                )
            }
        }, confirmButton = {
            if (isInProgress.value) return@AlertDialog
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .pulsateEffect(), onClick = {
                    isInProgress.value = true
                    addANewPanelParam.onCreateClick(
                        customShelfName.value, {
                            addANewPanelParam.isDialogBoxVisible.value = false
                            isInProgress.value = false
                        }
                    )
                }) {
                Text(
                    text = Localization.Key.AddANewPanel.rememberLocalizedString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 16.sp
                )
            }
        }, dismissButton = {
            if (isInProgress.value.not()) {
                androidx.compose.material3.OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pulsateEffect(),
                    onClick = {
                        addANewPanelParam.isDialogBoxVisible.value = false
                    }) {
                    Text(
                        text = Localization.Key.Cancel.rememberLocalizedString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp
                    )
                }
            } else {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        })
    }
}