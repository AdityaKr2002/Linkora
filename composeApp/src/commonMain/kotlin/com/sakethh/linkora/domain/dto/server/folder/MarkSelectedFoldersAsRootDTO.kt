package com.sakethh.linkora.domain.dto.server.folder

import com.sakethh.linkora.common.preferences.AppPreferences
import com.sakethh.linkora.domain.dto.server.Correlation
import kotlinx.serialization.Serializable

@Serializable
data class MarkSelectedFoldersAsRootDTO(
    val folderIds: List<Long>,
    val eventTimestamp: Long,
    val correlation: Correlation = AppPreferences.getCorrelation()
)
