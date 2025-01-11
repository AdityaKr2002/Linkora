package com.sakethh.linkora.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.primaryContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.linkora.common.DependencyContainer
import com.sakethh.linkora.common.Localization
import com.sakethh.linkora.common.utils.rememberLocalizedString
import com.sakethh.linkora.ui.LocalNavController
import com.sakethh.linkora.ui.components.SortingIconButton
import com.sakethh.linkora.ui.navigation.Navigation
import com.sakethh.linkora.ui.utils.genericViewModelFactory
import com.sakethh.linkora.ui.utils.pulsateEffect
import com.sakethh.linkora.ui.utils.rememberDeserializableMutableObject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    val homeScreenVM: HomeScreenVM = viewModel(factory = genericViewModelFactory {
        HomeScreenVM(DependencyContainer.panelsRepo.value)
    })
    val shouldPanelsBtmSheetBeVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val panelsBtmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val panels = homeScreenVM.panels.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val selectedPanel = rememberDeserializableMutableObject {
        mutableStateOf(homeScreenVM.defaultPanel())
    }
    val selectedPanelFolders = homeScreenVM.panelFolders.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = {
        selectedPanelFolders.value.size
    })
    Scaffold(topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = homeScreenVM.currentPhaseOfTheDay.value,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        navController.navigate(Navigation.Home.PanelsManagerScreen)
                    }) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = null)
                    }
                    SortingIconButton()
                }
            }
            Text(
                text = Localization.Key.SelectedPanel.rememberLocalizedString(),
                color = MaterialTheme.colorScheme.primary.copy(0.9f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = {
                    shouldPanelsBtmSheetBeVisible.value = true
                    coroutineScope.launch {
                        panelsBtmSheetState.show()
                    }
                }, indication = null, interactionSource = remember {
                    MutableInteractionSource()
                }).pulsateEffect().fillMaxWidth().padding(start = 5.dp, end = 5.dp),
            ) {
                Spacer(Modifier.width(5.dp))
                FilledTonalIconButton(onClick = {
                    shouldPanelsBtmSheetBeVisible.value = true
                    coroutineScope.launch {
                        panelsBtmSheetState.show()
                    }
                }, modifier = Modifier.size(22.dp)) {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null)
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = selectedPanel.value.panelName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(), selectedTabIndex = pagerState.currentPage,
                divider = {}
            ) {
                selectedPanelFolders.value.forEachIndexed { index, panelFolder ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }.start()
                    }) {
                        Text(
                            text = panelFolder.folderName,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(15.dp),
                            color = if (pagerState.currentPage == index) primaryContentColor else MaterialTheme.colorScheme.onSurface.copy(
                                0.70f
                            )
                        )
                    }
                }
            }
            HorizontalDivider()
            HorizontalPager(state = pagerState) { pageIndex ->

            }
        }
    }
    if (shouldPanelsBtmSheetBeVisible.value) {
        ModalBottomSheet(onDismissRequest = {
            shouldPanelsBtmSheetBeVisible.value = false
            coroutineScope.launch {
                panelsBtmSheetState.hide()
            }
        }, sheetState = panelsBtmSheetState) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Text(
                        text = Localization.Key.SelectAPanel.rememberLocalizedString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(15.dp)
                    )
                }
                items(panels.value) { panel ->
                    Row(modifier = Modifier.fillMaxWidth().clickable {
                        selectedPanel.value = panel
                        homeScreenVM.updatePanelFolders(selectedPanel.value.panelId)
                        coroutineScope.launch {
                            panelsBtmSheetState.hide()
                        }.invokeOnCompletion {
                            shouldPanelsBtmSheetBeVisible.value = false
                        }
                    }.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPanel.value.panelId == panel.panelId, onClick = {
                                selectedPanel.value = panel
                                homeScreenVM.updatePanelFolders(selectedPanel.value.panelId)
                                coroutineScope.launch {
                                    panelsBtmSheetState.hide()
                                }.invokeOnCompletion {
                                    shouldPanelsBtmSheetBeVisible.value = false
                                }
                            })
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = panel.panelName,
                            style = if (selectedPanel.value.panelId == panel.panelId) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleSmall,
                            color = if (selectedPanel.value.panelId == panel.panelId) LocalContentColor.current else LocalContentColor.current.copy(
                                0.85f
                            )
                        )
                    }
                }
            }
        }
    }
}