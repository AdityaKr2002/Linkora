package com.sakethh.linkora.customComposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakethh.linkora.ui.theme.LinkoraTheme

enum class DataDialogBoxType {
    LINK, FOLDER, REMOVE_ENTIRE_DATA
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialogBox(
    shouldDialogBoxAppear: MutableState<Boolean>,
    deleteDialogBoxType: DataDialogBoxType,
    onDeleteClick: () -> Unit,
    onDeleted: () -> Unit = {},
) {
    Column {
        if (shouldDialogBoxAppear.value) {
            LinkoraTheme {
                AlertDialog(modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(AlertDialogDefaults.containerColor),
                    onDismissRequest = { shouldDialogBoxAppear.value = false }) {
                    Column {
                        Text(
                            text = if (deleteDialogBoxType == DataDialogBoxType.LINK) "Are you sure want to delete the link?" else if (deleteDialogBoxType == DataDialogBoxType.FOLDER) "Are you sure want to delete the folder?" else "Are you sure want to delete entire Data?",
                            color = AlertDialogDefaults.titleContentColor,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(start = 20.dp, top = 30.dp, end = 20.dp),
                            lineHeight = 27.sp,
                            textAlign = TextAlign.Start
                        )
                        Button(colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .padding(
                                    end = 20.dp,
                                    top = 20.dp,
                                )
                                .align(Alignment.End),
                            onClick = {
                                onDeleteClick()
                                shouldDialogBoxAppear.value = false
                                onDeleted()
                            }) {
                            Text(
                                text = "Delete it",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp
                            )
                        }
                        OutlinedButton(colors = ButtonDefaults.outlinedButtonColors(),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .padding(
                                    end = 20.dp,
                                    top = 10.dp,
                                    bottom = 30.dp
                                )
                                .align(Alignment.End),
                            onClick = {
                                shouldDialogBoxAppear.value = false
                            }) {
                            Text(
                                text = "Never-mind",
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}