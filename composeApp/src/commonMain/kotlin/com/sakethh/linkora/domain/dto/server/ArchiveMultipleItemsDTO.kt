package com.sakethh.linkora.domain.dto.server

import com.sakethh.linkora.preferences.AppPreferences
import kotlinx.serialization.Serializable

@Serializable
data class ArchiveMultipleItemsDTO(
    val linkIds: List<Long>,
    val folderIds: List<Long>,
    val eventTimestamp: Long,
    val correlation: Correlation = AppPreferences.getCorrelation()
)
