package com.sakethh.linkora.ui.screens.link_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import com.sakethh.linkora.ui.commonComposables.link_views.LinkUIComponentParam
import com.sakethh.linkora.ui.commonComposables.link_views.components.GridViewComponent
import com.sakethh.linkora.ui.commonComposables.link_views.components.ListViewComponent
import com.sakethh.linkora.ui.screens.link_view.model.LinkPref
import com.sakethh.linkora.ui.screens.settings.SettingsPreference
import com.sakethh.linkora.ui.screens.settings.SettingsPreference.dataStore
import com.sakethh.linkora.ui.screens.settings.SettingsPreferences
import com.sakethh.linkora.ui.screens.settings.composables.SpecificSettingsScreenScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkViewSettings(navController: NavController) {
    val sampleList = remember {
        listOf(
            LinkUIComponentParam(
                title = "Red Dead Redemption 2 - Rockstar Games",
                webBaseURL = "rockstargames.com",
                imgURL = "https://media-rockstargames-com.akamaized.net/rockstargames-newsite/img/global/games/fob/640/reddeadredemption2.jpg",
                onMoreIconClick = { -> },
                onLinkClick = { -> },
                webURL = "https://www.rockstargames.com/reddeadredemption2",
                onForceOpenInExternalBrowserClicked = { -> },
                isSelectionModeEnabled = mutableStateOf(false),
                isItemSelected = mutableStateOf(false),
                onLongClick = { -> }),
            LinkUIComponentParam(
                title = "A Plague Tale: Requiem | Download and Buy Today - Epic Games Store",
                webBaseURL = "store.epicgames.com",
                imgURL = "https://cdn1.epicgames.com/salesEvent/salesEvent/PlagueTale2_2560x1440-f5840bd8286204cb20ae573e160c29f3",
                onMoreIconClick = { -> },
                onLinkClick = { -> },
                webURL = "https://store.epicgames.com/en-US/p/a-plague-tale-requiem",
                onForceOpenInExternalBrowserClicked = { -> },
                isSelectionModeEnabled = mutableStateOf(false),
                isItemSelected = mutableStateOf(false),
                onLongClick = { -> }),
            LinkUIComponentParam(
                title = "Nas | Spotify",
                webBaseURL = "open.spotify.com",
                imgURL = "https://i.scdn.co/image/ab6761610000e5eb153198caeef9e3bda92f9285",
                onMoreIconClick = { -> },
                onLinkClick = { -> },
                webURL = "https://open.spotify.com/artist/20qISvAhX20dpIbOOzGK3q?si=aXLvdWf0TJGbJCEuQc8kzg",
                onForceOpenInExternalBrowserClicked = { -> },
                isSelectionModeEnabled = mutableStateOf(false),
                isItemSelected = mutableStateOf(false),
                onLongClick = { -> }),
            LinkUIComponentParam(
                title = "Hacker (small type)",
                webBaseURL = "twitter.com",
                imgURL = "https://pbs.twimg.com/media/GT7RIrWWwAAjZzg.jpg",
                onMoreIconClick = { -> },
                onLinkClick = { -> },
                webURL = "https://twitter.com/CatWorkers/status/1819121250226127061",
                onForceOpenInExternalBrowserClicked = { -> },
                isSelectionModeEnabled = mutableStateOf(false),
                isItemSelected = mutableStateOf(false),
                onLongClick = { -> }),
        )
    }

    val nonListViewPref = remember {
        listOf(
            LinkPref(
                onClick = {
                    SettingsPreference.enableBorderForNonListViews.value =
                        !SettingsPreference.enableBorderForNonListViews.value
                    SettingsPreference.changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(SettingsPreferences.BORDER_VISIBILITY_FOR_NON_LIST_VIEWS.name),
                        dataStore = navController.context.dataStore,
                        newValue = SettingsPreference.enableBorderForNonListViews.value
                    )
                },
                title = "Show Border Around Links",
                isSwitchChecked = SettingsPreference.enableBorderForNonListViews
            ),
            LinkPref(
                onClick = {
                    SettingsPreference.enableTitleForNonListViews.value =
                        !SettingsPreference.enableTitleForNonListViews.value
                    SettingsPreference.changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(SettingsPreferences.TITLE_VISIBILITY_FOR_NON_LIST_VIEWS.name),
                        dataStore = navController.context.dataStore,
                        newValue = SettingsPreference.enableTitleForNonListViews.value
                    )
                },
                title = "Show Title",
                isSwitchChecked = SettingsPreference.enableTitleForNonListViews
            ),
            LinkPref(
                onClick = {
                    SettingsPreference.enableBaseURLForNonListViews.value =
                        !SettingsPreference.enableBaseURLForNonListViews.value
                    SettingsPreference.changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(SettingsPreferences.BASE_URL_VISIBILITY_FOR_NON_LIST_VIEWS.name),
                        dataStore = navController.context.dataStore,
                        newValue = SettingsPreference.enableBaseURLForNonListViews.value
                    )
                },
                title = "Show Base URL",
                isSwitchChecked = SettingsPreference.enableBaseURLForNonListViews
            ),
            LinkPref(
                onClick = {
                    SettingsPreference.enableFadedEdgeForNonListViews.value =
                        !SettingsPreference.enableFadedEdgeForNonListViews.value
                    SettingsPreference.changeSettingPreferenceValue(
                        preferenceKey = booleanPreferencesKey(SettingsPreferences.FADED_EDGE_VISIBILITY_FOR_NON_LIST_VIEWS.name),
                        dataStore = navController.context.dataStore,
                        newValue = SettingsPreference.enableFadedEdgeForNonListViews.value
                    )
                },
                title = "Show Bottom Faded Edge",
                isSwitchChecked = SettingsPreference.enableFadedEdgeForNonListViews
            ),
        )
    }

    SpecificSettingsScreenScaffold(
        topAppBarText = "Link View Settings",
        navController = navController
    ) { paddingValues, topAppBarScrollBehaviour ->
        if (SettingsPreference.currentlySelectedLinkView.value == LinkView.REGULAR_LIST_VIEW.name
            || SettingsPreference.currentlySelectedLinkView.value == LinkView.TITLE_ONLY_LIST_VIEW.name
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection)
                    .navigationBarsPadding()
            ) {
                item {
                    Text(
                        text = "Choose the layout you like best",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(15.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(LinkView.entries) {
                    LinkViewRadioButtonComponent(it, navController, PaddingValues(start = 10.dp))
                }

                item {
                    HorizontalDivider(
                        Modifier.padding(
                            start = 15.dp, end = 15.dp, top = 15.dp, bottom = 5.dp
                        )
                    )
                }

                item {
                    Text(
                        text = "Feed Preview",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(15.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(sampleList) {
                    ListViewComponent(
                        it,
                        forTitleOnlyView = SettingsPreference.currentlySelectedLinkView.value == LinkView.TITLE_ONLY_LIST_VIEW.name
                    )
                }
                item {
                    Spacer(Modifier.height(100.dp))
                }
            }
        } else if (SettingsPreference.currentlySelectedLinkView.value == LinkView.GRID_VIEW.name) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .padding(paddingValues)
                    .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection)
                    .navigationBarsPadding()
            ) {
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    Text(
                        text = "Choose the layout you like best",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(LinkView.entries, span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    LinkViewRadioButtonComponent(it, navController)
                }

                items(nonListViewPref, span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    LinkViewPreferenceSwitch(
                        onClick = it.onClick,
                        title = it.title,
                        isSwitchChecked = it.isSwitchChecked.value
                    )
                }

                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 5.dp,
                            start = 5.dp,
                            end = 5.dp
                        ),
                    )
                }

                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    Text(
                        text = "Feed Preview",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp, start = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(sampleList + sampleList.shuffled()) {
                    GridViewComponent(it, forStaggeredView = false)
                }
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    Spacer(Modifier.height(100.dp))
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .padding(paddingValues)
                    .nestedScroll(topAppBarScrollBehaviour.nestedScrollConnection)
                    .navigationBarsPadding()
            ) {
                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Text(
                        text = "Choose the layout you like best",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(items = LinkView.entries,
                    span = {
                        StaggeredGridItemSpan.FullLine
                    }) {
                    LinkViewRadioButtonComponent(it, navController)
                }

                items(items = nonListViewPref, span = { StaggeredGridItemSpan.FullLine }) {
                    LinkViewPreferenceSwitch(
                        onClick = it.onClick,
                        title = it.title,
                        isSwitchChecked = it.isSwitchChecked.value
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 5.dp,
                            start = 5.dp,
                            end = 5.dp
                        ),
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = "Feed Preview",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp, start = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(sampleList + sampleList.shuffled()) {
                    GridViewComponent(it, forStaggeredView = true)
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
private fun LinkViewPreferenceSwitch(
    onClick: () -> Unit,
    title: String,
    isSwitchChecked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick()
            }, interactionSource = remember {
                MutableInteractionSource()
            }, indication = null)
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth(0.75f)
        )
        Switch(
            checked = isSwitchChecked,
            onCheckedChange = {
                onClick()
            })
    }
}

@Composable
private fun LinkViewRadioButtonComponent(
    linkView: LinkView,
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                SettingsPreference.currentlySelectedLinkView.value = linkView.name
                SettingsPreference.changeSettingPreferenceValue(
                    preferenceKey = stringPreferencesKey(SettingsPreferences.CURRENTLY_SELECTED_LINK_VIEW.name),
                    dataStore = navController.context.dataStore,
                    newValue = linkView.name
                )
            }
            .padding(paddingValues)
    ) {
        RadioButton(
            selected = SettingsPreference.currentlySelectedLinkView.value == linkView.name,
            onClick = {
                SettingsPreference.currentlySelectedLinkView.value = linkView.name
                SettingsPreference.changeSettingPreferenceValue(
                    preferenceKey = stringPreferencesKey(SettingsPreferences.CURRENTLY_SELECTED_LINK_VIEW.name),
                    dataStore = navController.context.dataStore,
                    newValue = linkView.name
                )
            })
        Text(text = linkView.name.replace("_", " ").split(" ").map {
            it.lowercase()
        }.map {
            it.replaceFirstChar { char -> char.uppercaseChar() }
        }.joinToString().replace(",", ""),
            style = MaterialTheme.typography.titleSmall
        )
    }
}