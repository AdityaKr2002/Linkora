package com.sakethh.linkora.domain.dto.server

import com.sakethh.linkora.preferences.AppPreferences
import com.sakethh.linkora.utils.getSystemEpochSeconds
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DeleteEverythingDTO(
    val eventTimestamp: Long = getSystemEpochSeconds(), val correlation: Correlation = AppPreferences.getCorrelation()
)