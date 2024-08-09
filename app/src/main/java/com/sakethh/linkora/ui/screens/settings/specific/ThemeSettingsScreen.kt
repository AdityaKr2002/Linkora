package com.sakethh.linkora.ui.screens.settings.specific

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.navigation.NavController
import com.sakethh.linkora.R
import com.sakethh.linkora.ui.CommonUiEvent
import com.sakethh.linkora.ui.screens.settings.SettingsPreference
import com.sakethh.linkora.ui.screens.settings.SettingsPreference.dataStore
import com.sakethh.linkora.ui.screens.settings.SettingsPreferences
import com.sakethh.linkora.ui.screens.settings.SettingsScreenVM
import com.sakethh.linkora.ui.screens.settings.SettingsUIElement
import com.sakethh.linkora.ui.screens.settings.composables.RegularSettingComponent
import com.sakethh.linkora.ui.screens.settings.composables.SpecificSettingsScreenTopAppBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(navController: NavController, settingsScreenVM: SettingsScreenVM) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        settingsScreenVM.eventChannel.collectLatest {
            when (it) {
                is CommonUiEvent.ShowToast -> {
                    Toast.makeText(context, context.getString(it.msg), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    SpecificSettingsScreenTopAppBar(
        topAppBarText = stringResource(id = R.string.theme),
        navController = navController
    ) { paddingValues, topAppBarScrollBehaviour ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !SettingsPreference.shouldDarkThemeBeEnabled.value) {
                item(key = "Follow System Theme") {
                    RegularSettingComponent(
                        settingsUIElement = SettingsUIElement(
                            title = stringResource(id = R.string.follow_system_theme),
                            doesDescriptionExists = false,
                            isSwitchNeeded = true,
                            description = null,
                            isSwitchEnabled = SettingsPreference.shouldFollowSystemTheme,
                            onSwitchStateChange = {
                                SettingsPreference.changeSettingPreferenceValue(
                                    preferenceKey = booleanPreferencesKey(
                                        SettingsPreferences.FOLLOW_SYSTEM_THEME.name
                                    ),
                                    dataStore = context.dataStore,
                                    newValue = !SettingsPreference.shouldFollowSystemTheme.value
                                )
                                SettingsPreference.shouldFollowSystemTheme.value =
                                    !SettingsPreference.shouldFollowSystemTheme.value
                            }, isIconNeeded = remember {
                                mutableStateOf(false)
                            })
                    )
                }
            }
            if (!SettingsPreference.shouldFollowSystemTheme.value) {
                item(key = "Use Dark Mode") {
                    RegularSettingComponent(
                        settingsUIElement = SettingsUIElement(
                            title = stringResource(id = R.string.use_dark_mode),
                            doesDescriptionExists = false,
                            description = null,
                            isSwitchNeeded = true,
                            isSwitchEnabled = SettingsPreference.shouldDarkThemeBeEnabled,
                            onSwitchStateChange = {
                                SettingsPreference.changeSettingPreferenceValue(
                                    preferenceKey = booleanPreferencesKey(
                                        SettingsPreferences.DARK_THEME.name
                                    ),
                                    dataStore = context.dataStore,
                                    newValue = !SettingsPreference.shouldDarkThemeBeEnabled.value
                                )
                                SettingsPreference.shouldDarkThemeBeEnabled.value =
                                    !SettingsPreference.shouldDarkThemeBeEnabled.value
                            }, isIconNeeded = remember {
                                mutableStateOf(false)
                            })
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item(key = "Use dynamic theming") {
                    RegularSettingComponent(
                        settingsUIElement = SettingsUIElement(
                            title = stringResource(id = R.string.use_dynamic_theming),
                            doesDescriptionExists = true,
                            description = stringResource(id = R.string.use_dynamic_theming_desc),
                            isSwitchNeeded = true,
                            isSwitchEnabled = SettingsPreference.shouldFollowDynamicTheming,
                            onSwitchStateChange = {
                                SettingsPreference.changeSettingPreferenceValue(
                                    preferenceKey = booleanPreferencesKey(
                                        SettingsPreferences.DYNAMIC_THEMING.name
                                    ),
                                    dataStore = context.dataStore,
                                    newValue = !SettingsPreference.shouldFollowDynamicTheming.value
                                )
                                SettingsPreference.shouldFollowDynamicTheming.value =
                                    !SettingsPreference.shouldFollowDynamicTheming.value
                            }, isIconNeeded = remember {
                                mutableStateOf(false)
                            })
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}