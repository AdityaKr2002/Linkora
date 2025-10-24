package com.sakethh.linkora.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakethh.linkora.utils.getSystemEpochSeconds
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity(tableName = "folders")
@Serializable
data class Folder(
    val name: String,
    val note: String,
    val parentFolderId: Long?,
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val remoteId: Long? = null,
    val isArchived: Boolean = false,
    val lastModified: Long = getSystemEpochSeconds()
) {
    class FolderAlreadyExists(message: String) : Throwable(message)
    class InvalidName(message: String) : Throwable(message)
}
