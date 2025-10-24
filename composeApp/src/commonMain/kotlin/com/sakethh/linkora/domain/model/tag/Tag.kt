package com.sakethh.linkora.domain.model.tag

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sakethh.linkora.domain.model.link.Link
import com.sakethh.linkora.utils.getSystemEpochSeconds
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val remoteId: Long? = null,
    val lastModified: Long = getSystemEpochSeconds(),
    val name: String
)

@Entity(
    tableName = "link_tags", primaryKeys = ["linkId", "tagId"], foreignKeys = [ForeignKey(
        entity = Link::class,
        parentColumns = ["localId"],
        childColumns = ["linkId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Tag::class,
        parentColumns = ["localId"],
        childColumns = ["tagId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Serializable
data class LinkTag(
    val remoteId: Long? = null,
    val linkId: Long,
    val tagId: Long,
    val lastModified: Long = getSystemEpochSeconds()
)