package com.sakethh.linkora.localDB.dao.sorting

import androidx.room.Dao
import androidx.room.Query
import com.sakethh.linkora.localDB.LinksTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchiveFolderLinksSorting {

    @Query("SELECT * FROM links_table WHERE isLinkedWithArchivedFolder=1 AND keyOfArchiveLinkedFolder = :folderName ORDER BY title ASC")
    fun sortByAToZ(folderName: String): Flow<List<LinksTable>>

    @Query("SELECT * FROM links_table WHERE isLinkedWithArchivedFolder=1 AND keyOfArchiveLinkedFolder=:folderName ORDER BY title DESC")
    fun sortByZToA(folderName: String): Flow<List<LinksTable>>

    @Query("SELECT * FROM links_table WHERE isLinkedWithArchivedFolder=1 AND keyOfArchiveLinkedFolder=:folderName ORDER BY id DESC")
    fun sortByLatestToOldest(folderName: String): Flow<List<LinksTable>>

    @Query("SELECT * FROM links_table WHERE isLinkedWithArchivedFolder=1 AND keyOfArchiveLinkedFolder=:folderName ORDER BY id ASC")
    fun sortByOldestToLatest(folderName: String): Flow<List<LinksTable>>

}