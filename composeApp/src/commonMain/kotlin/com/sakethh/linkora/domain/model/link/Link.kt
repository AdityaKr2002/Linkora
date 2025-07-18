package com.sakethh.linkora.domain.model.link

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakethh.linkora.common.Localization
import com.sakethh.linkora.common.preferences.AppPreferences
import com.sakethh.linkora.common.utils.baseUrl
import com.sakethh.linkora.common.utils.isATwitterUrl
import com.sakethh.linkora.domain.LinkType
import com.sakethh.linkora.domain.MediaType
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity(tableName = "links")
@Serializable
data class Link(
    val linkType: LinkType,
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val remoteId: Long? = null,
    val title: String,
    val url: String,
    val baseURL: String = if (url.isATwitterUrl()) "twitter.com" else url.baseUrl(throwOnException = false), // called baseUrl, but in some non-ui cases it kinda acts like host. back then I showed baseUrl instead of just host, so stuck with it
    val imgURL: String,
    val note: String,
    val idOfLinkedFolder: Long?,
    val userAgent: String? = AppPreferences.primaryJsoupUserAgent.value,
    val mediaType: MediaType = MediaType.IMAGE,
    val lastModified: Long = Instant.now().epochSecond
) {
    class Invalid(message: String = Localization.getLocalizedString(Localization.Key.InvalidLink)) :
        Throwable(message)
}