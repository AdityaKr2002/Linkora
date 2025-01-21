package com.sakethh.linkora.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTitleOfTheLinkDTO(
    val linkId: Long,
    val newTitleOfTheLink: String
)
