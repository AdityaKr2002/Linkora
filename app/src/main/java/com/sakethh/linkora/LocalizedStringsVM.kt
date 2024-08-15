package com.sakethh.linkora

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.linkora.data.local.localization.language.translations.TranslationsRepo
import com.sakethh.linkora.ui.screens.settings.SettingsPreference
import com.sakethh.linkora.utils.ifNullOrBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalizedStringsVM @Inject constructor(private val translationsRepo: TranslationsRepo) :
    ViewModel() {

    private val _general = mutableStateOf("")
    val general = _general

    private val _useLanguageStringsFetchedFromTheServer = mutableStateOf("")
    val useLanguageStringsFetchedFromTheServer = _useLanguageStringsFetchedFromTheServer

    private val _useLanguageStringsFetchedFromTheServerDesc = mutableStateOf("")
    val useLanguageStringsFetchedFromTheServerDesc = _useLanguageStringsFetchedFromTheServerDesc

    private val _userAgentDesc = mutableStateOf("")
    val userAgentDesc = _userAgentDesc

    private val _userAgent = mutableStateOf("")
    val userAgent = _userAgent

    private val _refreshingLinks = mutableStateOf("")
    val refreshingLinks = _refreshingLinks

    private val _workManagerDesc = mutableStateOf("")
    val workManagerDesc = _workManagerDesc

    private val _of = mutableStateOf("")
    val of = _of

    private val _linksRefreshed = mutableStateOf("")
    val linksRefreshed = _linksRefreshed

    private val _refreshingLinksInfo = mutableStateOf("")
    val refreshingLinksInfo = _refreshingLinksInfo

    private val _refreshAllLinksTitlesAndImages = mutableStateOf("")
    val refreshAllLinksTitlesAndImages = _refreshAllLinksTitlesAndImages

    private val _refreshAllLinksTitlesAndImagesDesc = mutableStateOf("")
    val refreshAllLinksTitlesAndImagesDesc = _refreshAllLinksTitlesAndImagesDesc

    private val _titleCopiedToClipboard = mutableStateOf("")
    val titleCopiedToClipboard = _titleCopiedToClipboard

    private val _viewNote = mutableStateOf("")
    val viewNote = _viewNote

    private val _rename = mutableStateOf("")
    val rename = _rename

    private val _refreshingTitleAndImage = mutableStateOf("")
    val refreshingTitleAndImage = _refreshingTitleAndImage

    private val _refreshImageAndTitle = mutableStateOf("")
    val refreshImageAndTitle = _refreshImageAndTitle

    private val _unarchive = mutableStateOf("")
    val unarchive = _unarchive

    private val _deleteTheNote = mutableStateOf("")
    val deleteTheNote = _deleteTheNote

    private val _deleteFolder = mutableStateOf("")
    val deleteFolder = _deleteFolder

    private val _deleteLink = mutableStateOf("")
    val deleteLink = _deleteLink

    private val _savedNote = mutableStateOf("")
    val savedNote = _savedNote

    private val _noteCopiedToClipboard = mutableStateOf("")
    val noteCopiedToClipboard = _noteCopiedToClipboard

    private val _youDidNotAddNoteForThis = mutableStateOf("")
    val youDidNotAddNoteForThis = _youDidNotAddNoteForThis


    // TODO()

    private val _sortFoldersBy = mutableStateOf("")
    val sortFoldersBy =
        _sortFoldersBy

    private val _sortHistoryLinksBy = mutableStateOf("")
    val sortHistoryLinksBy =
        _sortHistoryLinksBy

    private val _sortBy = mutableStateOf("")
    val sortBy =
        _sortBy

    private val _sortSavedLinksBy = mutableStateOf("")
    val sortSavedLinksBy =
        _sortSavedLinksBy

    private val _sortImportantLinksBy = mutableStateOf("")
    val sortImportantLinksBy =
        _sortImportantLinksBy

    private val _sortBasedOn = mutableStateOf("")
    val sortBasedOn =
        _sortBasedOn

    private val _folders = mutableStateOf("")
    val folders =
        _folders

    private val _addANewLinkInImportantLinks = mutableStateOf("")
    val addANewLinkInImportantLinks =
        _addANewLinkInImportantLinks

    private val _addANewLinkInSavedLinks = mutableStateOf("")
    val addANewLinkInSavedLinks =
        _addANewLinkInSavedLinks

    private val _addANewLinkIn = mutableStateOf("")
    val addANewLinkIn =
        _addANewLinkIn

    private val _addANewLink = mutableStateOf("")
    val addANewLink =
        _addANewLink

    private val _linkAddress = mutableStateOf("")
    val linkAddress =
        _linkAddress

    private val _titleForTheLink = mutableStateOf("")
    val titleForTheLink =
        _titleForTheLink

    private val _noteForSavingTheLink = mutableStateOf("")
    val noteForSavingTheLink =
        _noteForSavingTheLink

    private val _titleWillBeAutomaticallyDetected = mutableStateOf("")
    val titleWillBeAutomaticallyDetected =
        _titleWillBeAutomaticallyDetected

    private val _addIn = mutableStateOf("")
    val addIn = _addIn

    private val _savedLinks = mutableStateOf("")
    val savedLinks = _savedLinks

    private val _importantLinks = mutableStateOf("")
    val importantLinks = _importantLinks

    private val _forceAutoDetectTitle = mutableStateOf("")
    val forceAutoDetectTitle = _forceAutoDetectTitle

    private val _cancel = mutableStateOf("")
    val cancel = _cancel

    private val _save = mutableStateOf("")
    val save = _save

    private val _thisFolderHasNoSubfolders = mutableStateOf("")
    val thisFolderHasNoSubfolders = _thisFolderHasNoSubfolders

    private val _saveInThisFolder = mutableStateOf("")
    val saveInThisFolder = _saveInThisFolder

    fun loadStrings(context: Context) {
        viewModelScope.launch {
            awaitAll(
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _general.value =
                            translationsRepo.getLocalizedStringValueFor(
                                "general",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                if (it.isNullOrBlank()) {
                                    context.getString(R.string.general)
                                } else {
                                    it
                                }
                            }
                    } else {
                        _general.value = (context.getString(R.string.general))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _useLanguageStringsFetchedFromTheServer.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "use_language_strings_fetched_from_the_server",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.use_language_strings_fetched_from_the_server)
                                }
                            }
                        )
                    } else {
                        _useLanguageStringsFetchedFromTheServer.value =
                            (context.getString(R.string.use_language_strings_fetched_from_the_server))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _useLanguageStringsFetchedFromTheServerDesc.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "use_language_strings_fetched_from_the_server_desc",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.use_language_strings_fetched_from_the_server_desc)
                                }
                            }
                        )
                    } else {
                        _useLanguageStringsFetchedFromTheServerDesc.value =
                            (context.getString(R.string.use_language_strings_fetched_from_the_server_desc))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _userAgentDesc.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "user_agent_desc",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.user_agent_desc)
                                }
                            }
                        )
                    } else {
                        _userAgentDesc.value = (context.getString(R.string.user_agent_desc))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _userAgent.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "user_agent",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.user_agent)
                                }
                            }
                        )
                    } else {
                        _userAgent.value = (context.getString(R.string.user_agent))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshingLinks.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refreshing_links",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refreshing_links)
                                }
                            }
                        )
                    } else {
                        _refreshingLinks.value = (context.getString(R.string.refreshing_links))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _workManagerDesc.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "work_manager_desc",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.work_manager_desc)
                                }
                            }
                        )
                    } else {
                        _workManagerDesc.value = (context.getString(R.string.work_manager_desc))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _linksRefreshed.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "links_refreshed",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.links_refreshed)
                                }
                            }
                        )
                    } else {
                        _linksRefreshed.value = (context.getString(R.string.links_refreshed))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshingLinksInfo.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refreshing_links_info",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refreshing_links_info)
                                }
                            }
                        )
                    } else {
                        _refreshingLinksInfo.value =
                            (context.getString(R.string.refreshing_links_info))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshAllLinksTitlesAndImages.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refresh_all_links_titles_and_images",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refresh_all_links_titles_and_images)
                                }
                            }
                        )
                    } else {
                        _refreshAllLinksTitlesAndImages.value =
                            (context.getString(R.string.refresh_all_links_titles_and_images))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshAllLinksTitlesAndImagesDesc.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refresh_all_links_titles_and_images_desc",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refresh_all_links_titles_and_images_desc)
                                }
                            }
                        )
                    } else {
                        _refreshAllLinksTitlesAndImagesDesc.value =
                            (context.getString(R.string.refresh_all_links_titles_and_images_desc))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _of.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "of",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.of)
                                }
                            }
                        )
                    } else {
                        _of.value = (context.getString(R.string.of))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _titleCopiedToClipboard.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "title_copied_to_clipboard",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.title_copied_to_clipboard)
                                }
                            }
                        )
                    } else {
                        _titleCopiedToClipboard.value =
                            (context.getString(R.string.title_copied_to_clipboard))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _viewNote.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "view_note",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.view_note)
                                }
                            }
                        )
                    } else {
                        _viewNote.value = (context.getString(R.string.view_note))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _rename.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "rename",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.rename)
                                }
                            }
                        )
                    } else {
                        _rename.value = (context.getString(R.string.rename))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshingTitleAndImage.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refreshing_title_and_image",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refreshing_title_and_image)
                                }
                            }
                        )
                    } else {
                        _refreshingTitleAndImage.value =
                            (context.getString(R.string.refreshing_title_and_image))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _refreshImageAndTitle.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "refresh_image_and_title",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.refresh_image_and_title)
                                }
                            }
                        )
                    } else {
                        _refreshImageAndTitle.value =
                            (context.getString(R.string.refresh_image_and_title))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _unarchive.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "unarchive",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.unarchive)
                                }
                            }
                        )
                    } else {
                        _unarchive.value = (context.getString(R.string.unarchive))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _deleteTheNote.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "delete_the_note",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.delete_the_note)
                                }
                            }
                        )
                    } else {
                        _deleteTheNote.value = (context.getString(R.string.delete_the_note))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _deleteFolder.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "delete_folder",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.delete_folder)
                                }
                            }
                        )
                    } else {
                        _deleteFolder.value = (context.getString(R.string.delete_folder))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _deleteLink.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "delete_link",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.delete_link)
                                }
                            }
                        )
                    } else {
                        _deleteLink.value = (context.getString(R.string.delete_link))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _savedNote.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "saved_note",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.saved_note)
                                }
                            }
                        )
                    } else {
                        _savedNote.value = (context.getString(R.string.saved_note))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _noteCopiedToClipboard.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "note_copied_to_clipboard",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.note_copied_to_clipboard)
                                }
                            }
                        )
                    } else {
                        _noteCopiedToClipboard.value =
                            (context.getString(R.string.note_copied_to_clipboard))
                    }
                },
                async {
                    if (SettingsPreference.useLanguageStringsBasedOnFetchedValuesFromServer.value) {
                        _youDidNotAddNoteForThis.value = (
                            translationsRepo.getLocalizedStringValueFor(
                                "you_did_not_add_note_for_this",
                                SettingsPreference.preferredAppLanguageCode.value
                            ).let {
                                it.ifNullOrBlank {
                                    context.getString(R.string.you_did_not_add_note_for_this)
                                }
                            }
                        )
                    } else {
                        _youDidNotAddNoteForThis.value =
                            (context.getString(R.string.you_did_not_add_note_for_this))
                    }
                },
            )
        }
    }
}