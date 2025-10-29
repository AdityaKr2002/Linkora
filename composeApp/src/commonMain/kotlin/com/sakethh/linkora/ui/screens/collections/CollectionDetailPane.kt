package com.sakethh.linkora.ui.screens.collections

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.primaryContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.sakethh.linkora.Localization
import com.sakethh.linkora.di.CollectionScreenVMAssistedFactory
import com.sakethh.linkora.domain.LinkSaveConfig
import com.sakethh.linkora.domain.LinkType
import com.sakethh.linkora.domain.Platform
import com.sakethh.linkora.domain.asLocalizedString
import com.sakethh.linkora.domain.asMenuBtmSheetType
import com.sakethh.linkora.platform.PlatformSpecificBackHandler
import com.sakethh.linkora.platform.platform
import com.sakethh.linkora.ui.LocalNavController
import com.sakethh.linkora.ui.components.CollectionLayoutManager
import com.sakethh.linkora.ui.components.SortingIconButton
import com.sakethh.linkora.ui.components.folder.FolderComponent
import com.sakethh.linkora.ui.components.menu.MenuBtmSheetType
import com.sakethh.linkora.ui.domain.CurrentFABContext
import com.sakethh.linkora.ui.domain.FABContext
import com.sakethh.linkora.ui.domain.model.CollectionDetailPaneInfo
import com.sakethh.linkora.ui.domain.model.CollectionType
import com.sakethh.linkora.ui.domain.model.FolderComponentParam
import com.sakethh.linkora.ui.navigation.Navigation
import com.sakethh.linkora.ui.screens.DataEmptyScreen
import com.sakethh.linkora.ui.screens.search.FilterChip
import com.sakethh.linkora.ui.utils.UIEvent
import com.sakethh.linkora.ui.utils.UIEvent.pushUIEvent
import com.sakethh.linkora.utils.Constants
import com.sakethh.linkora.utils.addEdgeToEdgeScaffoldPadding
import com.sakethh.linkora.utils.getLocalizedString
import com.sakethh.linkora.utils.rememberLocalizedString
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailPane(
    currentFABContext: (CurrentFABContext) -> Unit, platform: Platform = platform(),
    navController: NavController = LocalNavController.current,
    collectionsScreenVM: CollectionsScreenVM = viewModel(
        factory = CollectionScreenVMAssistedFactory.createForCollectionDetailPane(
            platform, navController
        )
    ),
) {
    val links = collectionsScreenVM.linkTagsPairs.collectAsStateWithLifecycle()
    val childFolders = collectionsScreenVM.childFolders.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val rootArchiveFolders = collectionsScreenVM.rootArchiveFolders.collectAsStateWithLifecycle()
    val peekCollectionPaneHistory by collectionsScreenVM.peekPaneHistory.collectAsStateWithLifecycle()
    val currentFolder =
        if (collectionsScreenVM.collectionDetailPaneInfo != null) collectionsScreenVM.collectionDetailPaneInfo.currentFolder else peekCollectionPaneHistory?.currentFolder
    val currentTag =
        if (collectionsScreenVM.collectionDetailPaneInfo != null) collectionsScreenVM.collectionDetailPaneInfo.currentTag else peekCollectionPaneHistory?.currentTag
    val navController = LocalNavController.current
    val localUriHandler = LocalUriHandler.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val showArchiveCollection =
        if (collectionsScreenVM.collectionDetailPaneInfo != null) collectionsScreenVM.collectionDetailPaneInfo.currentFolder?.localId == Constants.ARCHIVE_ID else peekCollectionPaneHistory?.currentFolder?.localId == Constants.ARCHIVE_ID
    val showAllLinksCollection =
        if (collectionsScreenVM.collectionDetailPaneInfo != null) collectionsScreenVM.collectionDetailPaneInfo.currentFolder?.localId == Constants.ALL_LINKS_ID else peekCollectionPaneHistory?.currentFolder?.localId == Constants.ALL_LINKS_ID

    DisposableEffect(Unit) {
        onDispose {
            if (platform is Platform.Android.Mobile && navController.currentBackStackEntry?.destination?.hasRoute<Navigation.Root.CollectionsScreen>() == true) {
                currentFABContext(CurrentFABContext.ROOT)
            }
        }
    }

    LaunchedEffect(currentFolder) {
        if (currentTag != null || (currentFolder != null && (currentFolder.localId == Constants.ALL_LINKS_ID || currentFolder.localId >= 0))) {
            currentFABContext(
                CurrentFABContext(
                    fabContext = FABContext.REGULAR, currentFolder = currentFolder
                )
            )
            return@LaunchedEffect
        }
        if (currentFolder != null && (currentFolder.localId == Constants.SAVED_LINKS_ID || currentFolder.localId == Constants.IMPORTANT_LINKS_ID)) {
            currentFABContext(
                CurrentFABContext(
                    fabContext = FABContext.ADD_LINK_IN_FOLDER, currentFolder = currentFolder
                )
            )
            return@LaunchedEffect
        }

        // for archive:
        currentFABContext(CurrentFABContext(FABContext.HIDE))
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Column {
            MediumTopAppBar(scrollBehavior = topAppBarScrollBehavior, actions = {
                SortingIconButton()
            }, navigationIcon = {
                IconButton(modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand), onClick = {
                    if (collectionsScreenVM.collectionDetailPaneInfo != null) {
                        navController.navigateUp()
                    } else {
                        collectionsScreenVM.popFromDetailPane()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                    )
                }
            }, title = {
                val isTag =
                    if (collectionsScreenVM.collectionDetailPaneInfo != null) collectionsScreenVM.collectionDetailPaneInfo.collectionType == CollectionType.TAG
                    else collectionsScreenVM.peekPaneHistory.collectAsStateWithLifecycle().value?.collectionType == CollectionType.TAG
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isTag) {
                        Icon(imageVector = Icons.Default.Tag, contentDescription = null)
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                    Text(
                        text = if (isTag) currentTag?.name ?: "" else currentFolder?.name ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp
                    )
                }
            })
        }
    }) { paddingValues ->
        if (showArchiveCollection) {
            Column(modifier = Modifier.addEdgeToEdgeScaffoldPadding(paddingValues).fillMaxSize()) {
                TabRow(selectedTabIndex = pagerState.currentPage) {
                    listOf(
                        Localization.Key.Links.rememberLocalizedString(),
                        Localization.Key.Folders.rememberLocalizedString()
                    ).forEachIndexed { index, screenName ->
                        Tab(modifier = Modifier.pointerHoverIcon(icon = PointerIcon.Hand), selected = pagerState.currentPage == index, onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }.start()
                        }) {
                            Text(
                                text = screenName,
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
                HorizontalPager(state = pagerState) { pageIndex ->
                    when (pageIndex) {
                        0 -> {
                            CollectionLayoutManager(
                                folders = emptyList(),
                                linksTagsPairs = links.value,
                                paddingValues = PaddingValues(0.dp),
                                linkMoreIconClick = {
                                    coroutineScope.pushUIEvent(
                                        UIEvent.Type.ShowMenuBtmSheet(
                                            menuBtmSheetFor = it.link.linkType.asMenuBtmSheetType(),
                                            selectedLinkForMenuBtmSheet = it,
                                            selectedFolderForMenuBtmSheet = null
                                        )
                                    )
                                },
                                folderMoreIconClick = {},
                                onFolderClick = {},
                                onLinkClick = {
                                    collectionsScreenVM.addANewLink(
                                        link = it.link.copy(
                                            linkType = LinkType.HISTORY_LINK, localId = 0
                                        ),
                                        linkSaveConfig = LinkSaveConfig(
                                            forceAutoDetectTitle = false,
                                            forceSaveWithoutRetrievingData = true
                                        ),
                                        onCompletion = {},
                                        pushSnackbarOnSuccess = false,
                                        selectedTags = it.tags
                                    )
                                    localUriHandler.openUri(it.link.url)
                                },
                                isCurrentlyInDetailsView = {
                                    peekCollectionPaneHistory?.currentFolder?.localId == it.localId
                                },
                                emptyDataText = Localization.Key.NoArchiveLinksFound.getLocalizedString(),
                                nestedScrollConnection = topAppBarScrollBehavior.nestedScrollConnection,
                                onAttachedTagClick = {
                                    if (currentTag?.localId == it.localId) {
                                        return@CollectionLayoutManager
                                    }
                                    val collectionDetailPaneInfo = CollectionDetailPaneInfo(
                                        currentFolder = null,
                                        currentTag = it,
                                        collectionType = CollectionType.TAG,
                                    )
                                    if (platform is Platform.Android.Mobile) {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            key = Constants.COLLECTION_INFO_SAVED_STATE_HANDLE_KEY,
                                            value = Json.encodeToString(
                                                collectionDetailPaneInfo
                                            )
                                        )
                                        navController.navigate(
                                            Navigation.Collection.CollectionDetailPane
                                        )
                                    } else {
                                        collectionsScreenVM.pushToDetailPane(
                                            collectionDetailPaneInfo
                                        )
                                    }
                                },
                                tags = emptyList(),
                                tagMoreIconClick = {},
                                onTagClick = {})
                        }

                        1 -> {
                            LazyColumn(Modifier.fillMaxSize()) {
                                if (rootArchiveFolders.value.isEmpty()) {
                                    item {
                                        DataEmptyScreen(text = Localization.Key.NoFoldersFoundInArchive.getLocalizedString())
                                    }
                                    return@LazyColumn
                                }
                                items(rootArchiveFolders.value) { rootArchiveFolder ->
                                    FolderComponent(
                                        FolderComponentParam(
                                            name = rootArchiveFolder.name,
                                            note = rootArchiveFolder.note,
                                            onClick = { ->
                                                if (CollectionsScreenVM.selectedFoldersViaLongClick.contains(
                                                        rootArchiveFolder
                                                    )
                                                ) {
                                                    return@FolderComponentParam
                                                }
                                                val collectionDetailPaneInfo =
                                                    CollectionDetailPaneInfo(
                                                        currentFolder = rootArchiveFolder,
                                                        collectionType = CollectionType.FOLDER,
                                                        currentTag = null
                                                    )
                                                if (platform is Platform.Android.Mobile) {
                                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                                        key = Constants.COLLECTION_INFO_SAVED_STATE_HANDLE_KEY,
                                                        value = Json.encodeToString(
                                                            collectionDetailPaneInfo
                                                        )
                                                    )
                                                    navController.navigate(Navigation.Collection.CollectionDetailPane)
                                                } else {
                                                    collectionsScreenVM.pushToDetailPane(
                                                        collectionDetailPaneInfo
                                                    )
                                                }
                                            },
                                            onLongClick = { ->
                                                if (CollectionsScreenVM.isSelectionEnabled.value.not()) {
                                                    CollectionsScreenVM.isSelectionEnabled.value =
                                                        true
                                                    CollectionsScreenVM.selectedFoldersViaLongClick.add(
                                                        rootArchiveFolder
                                                    )
                                                }
                                            },
                                            onMoreIconClick = { ->
                                                coroutineScope.pushUIEvent(
                                                    UIEvent.Type.ShowMenuBtmSheet(
                                                        menuBtmSheetFor = MenuBtmSheetType.Folder.RegularFolder,
                                                        selectedLinkForMenuBtmSheet = null,
                                                        selectedFolderForMenuBtmSheet = rootArchiveFolder
                                                    )
                                                )
                                            },
                                            isCurrentlyInDetailsView = remember(
                                                peekCollectionPaneHistory?.currentFolder?.localId
                                            ) {
                                                mutableStateOf(peekCollectionPaneHistory?.currentFolder?.localId == rootArchiveFolder.localId)
                                            },
                                            showMoreIcon = rememberSaveable {
                                                mutableStateOf(true)
                                            },
                                            isSelectedForSelection = rememberSaveable(
                                                CollectionsScreenVM.isSelectionEnabled.value,
                                                CollectionsScreenVM.selectedFoldersViaLongClick.contains(
                                                    rootArchiveFolder
                                                )
                                            ) {
                                                mutableStateOf(
                                                    CollectionsScreenVM.isSelectionEnabled.value && CollectionsScreenVM.selectedFoldersViaLongClick.contains(
                                                        rootArchiveFolder
                                                    )
                                                )
                                            },
                                            showCheckBox = CollectionsScreenVM.isSelectionEnabled,
                                            onCheckBoxChanged = { bool ->
                                                if (bool) {
                                                    CollectionsScreenVM.selectedFoldersViaLongClick.add(
                                                        rootArchiveFolder
                                                    )
                                                } else {
                                                    CollectionsScreenVM.selectedFoldersViaLongClick.remove(
                                                        rootArchiveFolder
                                                    )
                                                }
                                            })
                                    )
                                }
                            }
                        }
                    }
                }
            }
            return@Scaffold
        }
        val availableFiltersForAllLinks by collectionsScreenVM.availableFiltersForAllLinks.collectAsStateWithLifecycle()
        Column(modifier = Modifier.addEdgeToEdgeScaffoldPadding(paddingValues).fillMaxSize()) {
            if (showAllLinksCollection) {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    availableFiltersForAllLinks.forEach {
                        FilterChip(
                            text = it.asLocalizedString(),
                            isSelected = collectionsScreenVM.appliedFiltersForAllLinks.contains(
                                it
                            ),
                            onClick = {
                                collectionsScreenVM.toggleAllLinksFilter(it)
                            })
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            CollectionLayoutManager(
                folders = childFolders.value,
                linksTagsPairs = links.value,
                paddingValues = PaddingValues(0.dp),
                linkMoreIconClick = {
                    coroutineScope.pushUIEvent(
                        UIEvent.Type.ShowMenuBtmSheet(
                            menuBtmSheetFor = it.link.linkType.asMenuBtmSheetType(),
                            selectedLinkForMenuBtmSheet = it,
                            selectedFolderForMenuBtmSheet = null
                        )
                    )
                },
                folderMoreIconClick = {
                    coroutineScope.pushUIEvent(
                        UIEvent.Type.ShowMenuBtmSheet(
                            menuBtmSheetFor = MenuBtmSheetType.Folder.RegularFolder,
                            selectedLinkForMenuBtmSheet = null,
                            selectedFolderForMenuBtmSheet = it
                        )
                    )
                },
                onFolderClick = {
                    val collectionDetailPaneInfo = CollectionDetailPaneInfo(
                        currentFolder = it,
                        collectionType = CollectionType.FOLDER,
                        currentTag = null
                    )

                    if (collectionsScreenVM.collectionDetailPaneInfo != null) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = Constants.COLLECTION_INFO_SAVED_STATE_HANDLE_KEY,
                            value = Json.encodeToString(
                                collectionDetailPaneInfo
                            )
                        )
                        navController.navigate(Navigation.Collection.CollectionDetailPane)
                    } else {
                        collectionsScreenVM.pushToDetailPane(
                            collectionDetailPaneInfo
                        )
                    }
                },
                onLinkClick = {
                    collectionsScreenVM.addANewLink(
                        link = it.link.copy(linkType = LinkType.HISTORY_LINK, localId = 0),
                        linkSaveConfig = LinkSaveConfig(
                            forceAutoDetectTitle = false, forceSaveWithoutRetrievingData = true
                        ),
                        onCompletion = {},
                        pushSnackbarOnSuccess = false,
                        selectedTags = it.tags
                    )
                    localUriHandler.openUri(it.link.url)
                },
                isCurrentlyInDetailsView = {
                    peekCollectionPaneHistory?.currentFolder?.localId == it.localId
                },
                nestedScrollConnection = topAppBarScrollBehavior.nestedScrollConnection,
                emptyDataText = if (currentTag != null) Localization.Key.NoAttachmentsToTags.rememberLocalizedString() else if (currentFolder?.localId in listOf(
                        Constants.SAVED_LINKS_ID, Constants.IMPORTANT_LINKS_ID
                    )
                ) Localization.Key.NoLinksFound.rememberLocalizedString() else "",
                onAttachedTagClick = {
                    if (currentTag?.localId == it.localId) {
                        return@CollectionLayoutManager
                    }
                    val collectionDetailPaneInfo = CollectionDetailPaneInfo(
                        currentFolder = null,
                        currentTag = it,
                        collectionType = CollectionType.TAG,
                    )
                    if (collectionsScreenVM.collectionDetailPaneInfo != null) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = Constants.COLLECTION_INFO_SAVED_STATE_HANDLE_KEY,
                            value = Json.encodeToString(
                                collectionDetailPaneInfo
                            )
                        )
                        navController.navigate(
                            Navigation.Collection.CollectionDetailPane
                        )
                    } else {
                        collectionsScreenVM.pushToDetailPane(collectionDetailPaneInfo)
                    }
                },
                tags = emptyList(),
                tagMoreIconClick = {},
                onTagClick = {})
        }
    }
    PlatformSpecificBackHandler {
        if (platform is Platform.Android.Mobile) {
            navController.navigateUp()
        } else {
            collectionsScreenVM.popFromDetailPane()
        }
    }
}