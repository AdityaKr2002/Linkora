package com.sakethh.linkora.common.utils

object Constants {
    const val APP_VERSION_NAME = "v0.13.2"
    const val LOCALIZATION_SERVER_URL = "https://linkoralocalizationserver.onrender.com/"

    const val ALL_LINKS_ID: Long = -1
    const val SAVED_LINKS_ID: Long = -2
    const val IMPORTANT_LINKS_ID: Long = -3
    const val ARCHIVE_ID: Long = -4
    const val HISTORY_ID: Long = -5
    const val DEFAULT_PANELS_ID: Long = -6

    const val DEFAULT_APP_LANGUAGE_CODE = "en"
    const val DEFAULT_APP_LANGUAGE_NAME = "English"
    const val DEFAULT_USER_AGENT = "Twitterbot/1.0"
    const val EXPORT_SCHEMA_VERSION = 12

    const val DATA_STORE_NAME = "linkoraDataStore.preferences_pb"

    const val COLLECTION_INFO_SAVED_STATE_HANDLE_KEY = "parentFolderDetail"
}

object LinkType {
    const val SAVED_LINK = "SAVED_LINK"
    const val FOLDER_LINK = "FOLDER_LINK"
    const val HISTORY_LINK = "HISTORY_LINK"
    const val IMPORTANT_LINK = "IMPORTANT_LINK"
    const val ARCHIVE_LINK = "ARCHIVE_LINK"
}

object Sorting {
    const val A_TO_Z = "A_TO_Z"
    const val Z_TO_A = "Z_TO_A"
    const val NEW_TO_OLD = "NEW_TO_OLD"
    const val OLD_TO_NEW = "OLD_TO_NEW"
}