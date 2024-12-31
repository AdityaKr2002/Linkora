package com.sakethh.linkora.domain.dto.localization

import kotlinx.serialization.Serializable

@Serializable
data class AvailableLanguage(
    val languageCode: String,
    val languageName: String,
    val localizedStringsCount: Int,
    val contributionLink: String
)