package com.sakethh.linkora.domain.dto.server.panel

import kotlinx.serialization.Serializable

@Serializable
data class PanelFolderDTO(
    val id: Long,
    val folderId: Long,
    val panelPosition: Long,
    val folderName: String,
    val connectedPanelId: Long,
    val correlationId: String
)