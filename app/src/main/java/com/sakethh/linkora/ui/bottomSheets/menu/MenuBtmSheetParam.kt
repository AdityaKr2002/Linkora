package com.sakethh.linkora.ui.bottomSheets.menu

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.sakethh.linkora.ui.viewmodels.commonBtmSheets.OptionsBtmSheetType

data class MenuBtmSheetParam @OptIn(ExperimentalMaterial3Api::class) constructor(
    val btmModalSheetState: SheetState,
    val shouldBtmModalSheetBeVisible: MutableState<Boolean>,
    val btmSheetFor: OptionsBtmSheetType,
    val onDeleteCardClick: () -> Unit,
    val onNoteDeleteCardClick: () -> Unit,
    val onRenameClick: () -> Unit,
    val onArchiveClick: () -> Unit,
    val onUnarchiveClick: () -> Unit = {},
    val onImportantLinkClick: (() -> Unit?)? = null,
    val inArchiveScreen: MutableState<Boolean> = mutableStateOf(false),
    val inSpecificArchiveScreen: MutableState<Boolean> = mutableStateOf(false),
    val noteForSaving: String,
    val folderName: String,
    val linkTitle: String,
    val forAChildFolder: MutableState<Boolean> = mutableStateOf(false)
)