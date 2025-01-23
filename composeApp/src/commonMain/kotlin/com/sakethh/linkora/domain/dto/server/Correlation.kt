package com.sakethh.linkora.domain.dto.server

import kotlinx.serialization.Serializable

@Serializable
data class Correlation(
    val id: String, val clientName: String
)
