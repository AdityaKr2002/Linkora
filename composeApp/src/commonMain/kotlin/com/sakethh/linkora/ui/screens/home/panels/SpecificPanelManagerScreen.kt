package com.sakethh.linkora.ui.screens.home.panels

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sakethh.linkora.common.Localization
import com.sakethh.linkora.common.utils.addEdgeToEdgeScaffoldPadding
import com.sakethh.linkora.common.utils.rememberLocalizedString
import com.sakethh.linkora.di.SpecificPanelManagerScreenVMAssistedFactory
import com.sakethh.linkora.di.linkoraViewModel
import com.sakethh.linkora.domain.Platform
import com.sakethh.linkora.domain.model.panel.PanelFolder
import com.sakethh.linkora.ui.LocalNavController
import com.sakethh.linkora.ui.utils.pulsateEffect
import com.sakethh.platform

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpecificPanelManagerScreen(
    paddingValues: PaddingValues = PaddingValues(),
    specificPanelManagerScreenVM: SpecificPanelManagerScreenVM = linkoraViewModel(factory = SpecificPanelManagerScreenVMAssistedFactory.createForSpecificPanelManagerScreen())
) {
    val navController = LocalNavController.current
    val foldersOfTheSelectedPanel =
        specificPanelManagerScreenVM.foldersOfTheSelectedPanel.collectAsStateWithLifecycle()

    val foldersToIncludeInPanel =
        specificPanelManagerScreenVM.foldersToIncludeInPanel.collectAsStateWithLifecycle()
    val topAppBarState = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()).fillMaxSize(),
        topBar = {
            MediumTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                }
            }, scrollBehavior = topAppBarState, title = {
                Text(
                    text = SpecificPanelManagerScreenVM.selectedPanel.value.panelName,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                )
            })
        },
        bottomBar = {
            Column {
                OutlinedTextField(
                    trailingIcon = {
                        if (specificPanelManagerScreenVM.foldersSearchQuery.value.isNotBlank()) {
                            IconButton(onClick = {
                                specificPanelManagerScreenVM.updateFoldersSearchQuery("")
                            }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    textStyle = MaterialTheme.typography.titleSmall,
                    shape = RoundedCornerShape(15.dp),
                    value = specificPanelManagerScreenVM.foldersSearchQuery.value,
                    onValueChange = {
                        specificPanelManagerScreenVM.updateFoldersSearchQuery(it)
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp)
                        .navigationBarsPadding(),
                    placeholder = {
                        Text(
                            text = "Search folders to add",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.basicMarquee()
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    })
                if (platform() is Platform.Desktop) {
                    Spacer(Modifier.height(15.dp))
                }
            }
        }) {
        LazyColumn(
            modifier = Modifier.addEdgeToEdgeScaffoldPadding(it).fillMaxWidth().animateContentSize()
                .nestedScroll(topAppBarState.nestedScrollConnection)
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(15.dp)
                ) {
                    Text(
                        text = Localization.Key.Panels.rememberLocalizedString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            navController.navigateUp()
                        })
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = ""
                    )
                    Text(
                        text = SpecificPanelManagerScreenVM.selectedPanel.value.panelName,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp
                    )
                }
                HorizontalDivider(color = LocalContentColor.current.copy(0.25f))
            }
            if (foldersOfTheSelectedPanel.value.isNotEmpty()) {

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = Localization.Key.FoldersInThisPanel.rememberLocalizedString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 10.dp, end = 15.dp)
                    )
                }
                items(foldersOfTheSelectedPanel.value) { folderItem ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().pulsateEffect().clickable(onClick = {
                            specificPanelManagerScreenVM.removeAFolderFromAPanel(
                                panelId = SpecificPanelManagerScreenVM.selectedPanel.value.localId,
                                folderId = folderItem.folderId
                            )
                        }, indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }).padding(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = folderItem.folderName,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            if (foldersToIncludeInPanel.value.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = Localization.Key.FoldersThatCanBeAddedToThisPanel.rememberLocalizedString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 10.dp, end = 15.dp)
                    )
                }
                items(foldersToIncludeInPanel.value) { folderToIncludeInPanel ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().pulsateEffect().clickable(onClick = {
                            specificPanelManagerScreenVM.addANewFolderInAPanel(
                                PanelFolder(
                                    folderId = folderToIncludeInPanel.localId,
                                    folderName = folderToIncludeInPanel.name,
                                    connectedPanelId = SpecificPanelManagerScreenVM.selectedPanel.value.localId,
                                    panelPosition = 0
                                )
                            )
                        }, indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }).padding(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = folderToIncludeInPanel.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}