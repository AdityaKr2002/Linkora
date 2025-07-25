package com.sakethh.linkora.ui.screens.settings.section.data.snapshots

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoDelete
import androidx.compose.material.icons.filled.BackupTable
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sakethh.linkora.common.Localization
import com.sakethh.linkora.common.preferences.AppPreferenceType
import com.sakethh.linkora.common.preferences.AppPreferences
import com.sakethh.linkora.common.utils.addEdgeToEdgeScaffoldPadding
import com.sakethh.linkora.di.linkoraViewModel
import com.sakethh.linkora.domain.Platform
import com.sakethh.linkora.domain.SnapshotFormat
import com.sakethh.linkora.domain.model.settings.SettingComponentParam
import com.sakethh.linkora.ui.LocalNavController
import com.sakethh.linkora.ui.navigation.Navigation
import com.sakethh.linkora.ui.screens.settings.common.composables.SettingComponent
import com.sakethh.linkora.ui.screens.settings.common.composables.SettingsSectionScaffold
import com.sakethh.linkora.ui.screens.settings.section.data.DataSettingsScreenVM
import com.sakethh.linkora.ui.screens.settings.section.data.ExportLocationType
import com.sakethh.linkora.ui.screens.settings.section.data.components.ToggleButton
import com.sakethh.linkora.ui.utils.pulsateEffect
import com.sakethh.platform
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotsScreen() {

    val backupLocation = rememberSaveable(AppPreferences.currentBackupLocation.value) {
        mutableStateOf(AppPreferences.currentBackupLocation.value)
    }

    val backupAutoDeleteThreshold =
        rememberSaveable(AppPreferences.backupAutoDeleteThreshold.intValue) {
            mutableIntStateOf(AppPreferences.backupAutoDeleteThreshold.intValue)
        }
    val localFocusManager = LocalFocusManager.current

    val isBackupAutoDeletionEnabled =
        rememberSaveable(AppPreferences.isBackupAutoDeletionEnabled.value) {
            mutableStateOf(AppPreferences.isBackupAutoDeletionEnabled.value)
        }
    val platform = platform()
    val navController = LocalNavController.current
    val coroutineScope = rememberCoroutineScope()
    val dataSettingsScreenVM: DataSettingsScreenVM = linkoraViewModel()
    SettingsSectionScaffold(
        topAppBarText = Navigation.Settings.Data.SnapshotsScreen.toString(),
        navController = navController
    ) { paddingValues, topAppBarScrollBehaviour ->
        LazyColumn(
            modifier = Modifier.animateContentSize().fillMaxSize()
                .addEdgeToEdgeScaffoldPadding(paddingValues)
                .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item {
                Spacer(modifier = Modifier)
            }
            item {
                SettingComponent(
                    SettingComponentParam(
                        isIconNeeded = rememberSaveable { mutableStateOf(true) },
                        title = Localization.rememberLocalizedString(Localization.Key.UseSnapshots),
                        doesDescriptionExists = true,
                        description = Localization.rememberLocalizedString(Localization.Key.UseSnapshotsDescription),
                        isSwitchNeeded = true,
                        isSwitchEnabled = AppPreferences.areSnapshotsEnabled,
                        onSwitchStateChange = {
                            var isStorageAccessPermitted = false
                            coroutineScope.launch {
                                isStorageAccessPermitted =
                                    com.sakethh.isStorageAccessPermittedOnAndroid()
                            }.invokeOnCompletion { _ ->
                                if (isStorageAccessPermitted.not() && platform is Platform.Android) return@invokeOnCompletion

                                AppPreferences.areSnapshotsEnabled.value = it
                                dataSettingsScreenVM.changeSettingPreferenceValue(
                                    preferenceKey = booleanPreferencesKey(
                                        AppPreferenceType.USE_SNAPSHOTS.name
                                    ), newValue = it
                                )
                            }
                        },
                        icon = Icons.Default.BackupTable,
                        shouldFilledIconBeUsed = rememberSaveable { mutableStateOf(true) })
                )
            }
            if (AppPreferences.areSnapshotsEnabled.value) {
                item {
                    TextField(
                        supportingText = {
                        if (platform is Platform.Android) {
                            Text(
                                text = Localization.rememberLocalizedString(Localization.Key.SnapshotsBackupLocationWarning),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }, textStyle = MaterialTheme.typography.titleSmall, trailingIcon = {
                        FilledTonalIconButton(
                            modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                                .pulsateEffect().padding(end = 5.dp), onClick = {
                                dataSettingsScreenVM.changeExportLocation(
                                    exportLocation = backupLocation.value,
                                    platform = platform,
                                    exportLocationType = ExportLocationType.SNAPSHOT
                                )
                            }) {
                            Icon(
                                imageVector = if (platform is Platform.Android) Icons.Default.FolderOpen else Icons.Default.Save,
                                contentDescription = null
                            )
                        }
                    }, readOnly = platform is Platform.Android, label = {
                        Text(
                            text = Localization.rememberLocalizedString(Localization.Key.SnapshotsBackupLocation),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Start,
                        )
                    }, value = backupLocation.value, onValueChange = {
                        backupLocation.value = it
                    }, modifier = Modifier.padding(
                        start = 15.dp,
                        end = 15.dp,
                    ).fillMaxWidth()
                    )
                }
                item {
                    SettingComponent(
                        SettingComponentParam(
                            isIconNeeded = rememberSaveable { mutableStateOf(true) },
                            title = Localization.rememberLocalizedString(Localization.Key.EnableAutoDeleteSnapshots),
                            doesDescriptionExists = true,
                            description = Localization.rememberLocalizedString(Localization.Key.EnableAutoDeleteSnapshotsDescription),
                            isSwitchNeeded = true,
                            isSwitchEnabled = isBackupAutoDeletionEnabled,
                            onSwitchStateChange = {
                                dataSettingsScreenVM.updateAutoDeletionBackupsState(it)
                            },
                            icon = Icons.Default.AutoDelete,
                            shouldFilledIconBeUsed = rememberSaveable { mutableStateOf(true) })
                    )
                }

                if (isBackupAutoDeletionEnabled.value) {
                    item {
                        TextField(
                            supportingText = {
                                Text(
                                    text = Localization.rememberLocalizedString(Localization.Key.SnapshotsFileLimitWarning),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = MaterialTheme.typography.titleSmall,
                            trailingIcon = {
                                FilledTonalIconButton(
                                    modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand)
                                        .pulsateEffect().padding(end = 5.dp), onClick = {
                                        dataSettingsScreenVM.updateAutoDeletionBackupsThreshold(
                                            backupAutoDeleteThreshold.intValue
                                        )
                                        localFocusManager.clearFocus(force = true)
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Save, contentDescription = null
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = Localization.rememberLocalizedString(Localization.Key.SnapshotsFileLimit),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Start,
                                )
                            },
                            value = backupAutoDeleteThreshold.intValue.toString(),
                            onValueChange = {
                                backupAutoDeleteThreshold.intValue = try {
                                    it.toInt()
                                } catch (_: Exception) {
                                    0
                                } catch (_: Error) {
                                    0
                                }
                            },
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp)) {
                        Text(
                            text = Localization.rememberLocalizedString(Localization.Key.ExportAs),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(15.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            remember {
                                SnapshotFormat.entries
                            }.let {
                                it.forEachIndexed { index, snapshotFormat ->
                                    val checked =
                                        snapshotFormat.id.toString() == AppPreferences.snapshotExportFormatID.value
                                    ToggleButton(
                                        shape = when (index) {
                                            0 -> RoundedCornerShape(
                                                topStart = 15.dp,
                                                bottomStart = 15.dp,
                                                topEnd = 5.dp,
                                                bottomEnd = 5.dp
                                            )

                                            it.lastIndex -> RoundedCornerShape(
                                                topStart = 5.dp,
                                                bottomStart = 5.dp,
                                                topEnd = 15.dp,
                                                bottomEnd = 15.dp
                                            )

                                            else -> RoundedCornerShape(5.dp)
                                        }, checked = checked, onCheckedChange = {
                                            AppPreferences.snapshotExportFormatID.value =
                                                snapshotFormat.id.toString()
                                            dataSettingsScreenVM.changeSettingPreferenceValue(
                                                preferenceKey = stringPreferencesKey(
                                                    AppPreferenceType.SNAPSHOTS_EXPORT_TYPE.name
                                                ), newValue = snapshotFormat.id.toString()
                                            )
                                        }) {
                                        Text(
                                            text = snapshotFormat.localizedValue,
                                            style = if (checked) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                                            color = if (checked) MaterialTheme.colorScheme.onPrimary else LocalContentColor.current
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = if (platform !is Platform.Android) Localization.rememberLocalizedString(
                        Localization.Key.SnapshotsExportDescriptionDesktop
                    ) else Localization.rememberLocalizedString(Localization.Key.SnapshotsExportDescriptionAndroid),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                )
            }
            item {
                Spacer(Modifier.height(150.dp))
            }
        }
    }
}