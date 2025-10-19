package com.sakethh.linkora.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sakethh.linkora.domain.model.tag.LinkTag
import com.sakethh.linkora.domain.model.tag.Tag
import com.sakethh.linkora.domain.model.tag.TagWithLinkId
import com.sakethh.linkora.utils.Sorting
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Insert
    suspend fun createATag(tag: Tag): Long

    @Update
    suspend fun updateATag(tag: Tag)
    @Query("UPDATE tags SET remoteId = :newRemoteId WHERE localId = :localId")
    suspend fun updateRemoteId(localId: Long, newRemoteId: Long)

    @Query("SELECT * FROM tags WHERE localId = :tagId LIMIT 1")
    suspend fun getATag(tagId: Long): Tag

    @Insert
    suspend fun createLinkTags(linksTags: List<LinkTag>)

    @Query("DELETE FROM link_tags WHERE linkId = :linkId")
    suspend fun deleteLinkTagsBasedOnLink(linkId: Long)

    @Query("DELETE FROM link_tags WHERE tagId = :tagId")
    suspend fun deleteLinkTagsBasedOnTag(tagId: Long)

    @Query("DELETE FROM link_tags WHERE tagId IN (:tagIds)")
    suspend fun deleteLinkTagsBasedOnTags(tagIds: List<Long>)

    @Query("DELETE FROM tags WHERE localId = :id ")
    suspend fun deleteATag(id: Long)

    @Query("UPDATE tags SET name = :newName WHERE localId = :tagId")
    suspend fun renameATag(tagId: Long, newName: String)

    @Query("SELECT MAX(localId) FROM tags")
    suspend fun getLastInsertedIdFromTags(): Long

    @Query(
        """SELECT * FROM tags 
    ORDER BY 
        CASE WHEN :sortOption = '${Sorting.OLD_TO_NEW}' THEN localId END ASC,
        CASE WHEN :sortOption = '${Sorting.NEW_TO_OLD}' THEN localId END DESC,
        CASE WHEN :sortOption = '${Sorting.A_TO_Z}' THEN name COLLATE NOCASE END ASC,
        CASE WHEN :sortOption = '${Sorting.Z_TO_A}' THEN name COLLATE NOCASE END DESC"""
    )
    fun getAllTags(sortOption: String): Flow<List<Tag>>

    @Query("SELECT * FROM tags")
    suspend fun getAllTagsAsList(): List<Tag>

    @Query("SELECT * FROM link_tags")
    suspend fun getAllTagLinksAsList(): List<LinkTag>

    @Query("SELECT * FROM link_tags")
    fun getAllLinkTags(): Flow<List<LinkTag>>

    @Query("SELECT tags.* FROM tags INNER JOIN link_tags ON tags.localId = link_tags.tagId WHERE link_tags.linkId = :linkId")
    fun getTagsBasedOnTheLinkId(linkId: Long): Flow<List<Tag>>

    @Query("SELECT tags.* FROM tags INNER JOIN link_tags ON tags.localId = link_tags.tagId WHERE link_tags.linkId = :linkId")
   suspend fun getTags(linkId: Long): List<Tag>

    @Query(
        """
    SELECT * FROM tags
    WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'
    ORDER BY
        CASE WHEN :sortOption = '${Sorting.A_TO_Z}' THEN name COLLATE NOCASE END ASC,
        CASE WHEN :sortOption = '${Sorting.Z_TO_A}' THEN name COLLATE NOCASE END DESC,
        CASE WHEN :sortOption = '${Sorting.NEW_TO_OLD}' THEN localId END DESC,
        CASE WHEN :sortOption = '${Sorting.OLD_TO_NEW}' THEN localId END ASC
    """
    )
    fun search(query: String, sortOption: String): Flow<List<Tag>>

    @Transaction
    @Query(
        """
    SELECT linkTag.linkId, tag.* FROM tags tag
    INNER JOIN link_tags linkTag ON tag.localId = linkTag.tagId
    WHERE linkTag.linkId IN (:linkIds)
    """
    )
    fun getTagsWithLinkIds(linkIds: List<Long>): Flow<List<TagWithLinkId>>

    @Query("SELECT localId FROM tags WHERE remoteId IN (:remoteIds)")
    suspend fun getLocalTagIds(remoteIds: List<Long>): List<Long>

    @Query("SELECT * FROM tags WHERE remoteId IN (:remoteIds)")
    suspend fun getLocalTags(remoteIds: List<Long>): List<Tag>

    @Query("SELECT localId FROM tags WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getLocalTagId(remoteId: Long): Long

    @Query("SELECT remoteId FROM tags WHERE localId IN (:localIds)")
    suspend fun getRemoteTagIds(localIds: List<Long>): List<Long>

    @Query("SELECT * FROM tags WHERE remoteId IS NULL")
    suspend fun getUnsyncedTags(): List<Tag>
}