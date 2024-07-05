package com.sakethh.linkora.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sakethh.linkora.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL("DROP TABLE IF EXISTS new_folders_table;")
            db.execSQL("CREATE TABLE IF NOT EXISTS new_folders_table (folderName TEXT NOT NULL, infoForSaving TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);")
            db.execSQL("INSERT INTO new_folders_table (folderName, infoForSaving) SELECT folderName, infoForSaving FROM folders_table;")
            db.execSQL("DROP TABLE IF EXISTS folders_table;")
            db.execSQL("ALTER TABLE new_folders_table RENAME TO folders_table;")

            db.execSQL("DROP TABLE IF EXISTS new_archived_links_table;")
            db.execSQL("CREATE TABLE IF NOT EXISTS new_archived_links_table (title TEXT NOT NULL, webURL TEXT NOT NULL, baseURL TEXT NOT NULL, imgURL TEXT NOT NULL, infoForSaving TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);")
            db.execSQL("INSERT INTO new_archived_links_table (title, webURL, baseURL, imgURL, infoForSaving) SELECT title, webURL, baseURL, imgURL, infoForSaving FROM archived_links_table;")
            db.execSQL("DROP TABLE IF EXISTS archived_links_table;")
            db.execSQL("ALTER TABLE new_archived_links_table RENAME TO archived_links_table;")

            db.execSQL("DROP TABLE IF EXISTS new_archived_folders_table;")
            db.execSQL("CREATE TABLE IF NOT EXISTS new_archived_folders_table (archiveFolderName TEXT NOT NULL, infoForSaving TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);")
            db.execSQL("INSERT INTO new_archived_folders_table (archiveFolderName, infoForSaving) SELECT archiveFolderName, infoForSaving FROM archived_folders_table;")
            db.execSQL("DROP TABLE IF EXISTS archived_folders_table;")
            db.execSQL("ALTER TABLE new_archived_folders_table RENAME TO archived_folders_table;")

            db.execSQL("DROP TABLE IF EXISTS new_important_links_table;")
            db.execSQL("CREATE TABLE IF NOT EXISTS new_important_links_table (title TEXT NOT NULL, webURL TEXT NOT NULL, baseURL TEXT NOT NULL, imgURL TEXT NOT NULL, infoForSaving TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);")
            db.execSQL("INSERT INTO new_important_links_table (title, webURL, baseURL, imgURL, infoForSaving) SELECT title, webURL, baseURL, imgURL, infoForSaving FROM important_links_table;")
            db.execSQL("DROP TABLE IF EXISTS important_links_table;")
            db.execSQL("ALTER TABLE new_important_links_table RENAME TO important_links_table;")

            db.execSQL("DROP TABLE IF EXISTS new_important_folders_table;")
            db.execSQL("CREATE TABLE IF NOT EXISTS new_important_folders_table (impFolderName TEXT NOT NULL, infoForSaving TEXT NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);")
            db.execSQL("INSERT INTO new_important_folders_table (impFolderName, infoForSaving) SELECT impFolderName, infoForSaving FROM important_folders_table;")
            db.execSQL("DROP TABLE IF EXISTS important_folders_table;")
            db.execSQL("ALTER TABLE new_important_folders_table RENAME TO important_folders_table;")

        }

    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL("DROP TABLE IF EXISTS folders_table_new")
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `folders_table_new` (`folderName` TEXT NOT NULL, `infoForSaving` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `parentFolderID` INTEGER DEFAULT NULL, `childFolderIDs` TEXT DEFAULT NULL, `isFolderArchived` INTEGER NOT NULL DEFAULT 0, `isMarkedAsImportant` INTEGER NOT NULL DEFAULT 0)"
            )
            db.execSQL(
                "INSERT INTO folders_table_new (folderName, infoForSaving, id) " + "SELECT folderName, infoForSaving, id FROM folders_table"
            )
            db.execSQL("DROP TABLE folders_table")
            db.execSQL("ALTER TABLE folders_table_new RENAME TO folders_table")


            db.execSQL("DROP TABLE IF EXISTS links_table_new")
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `links_table_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `webURL` TEXT NOT NULL, `baseURL` TEXT NOT NULL, `imgURL` TEXT NOT NULL, `infoForSaving` TEXT NOT NULL, `isLinkedWithSavedLinks` INTEGER NOT NULL, `isLinkedWithFolders` INTEGER NOT NULL, `keyOfLinkedFolderV10` INTEGER DEFAULT NULL, `keyOfLinkedFolder` TEXT, `isLinkedWithImpFolder` INTEGER NOT NULL, `keyOfImpLinkedFolder` TEXT NOT NULL, `keyOfImpLinkedFolderV10` INTEGER DEFAULT NULL, `isLinkedWithArchivedFolder` INTEGER NOT NULL, `keyOfArchiveLinkedFolderV10` INTEGER DEFAULT NULL, `keyOfArchiveLinkedFolder` TEXT)"
            )
            db.execSQL(
                "INSERT INTO links_table_new (id, title, webURL, baseURL, imgURL, infoForSaving, " + "isLinkedWithSavedLinks, isLinkedWithFolders, keyOfLinkedFolder, " + "isLinkedWithImpFolder, keyOfImpLinkedFolder, " + "isLinkedWithArchivedFolder, keyOfArchiveLinkedFolder) " + "SELECT id, title, webURL, baseURL, imgURL, infoForSaving, " + "isLinkedWithSavedLinks, isLinkedWithFolders, keyOfLinkedFolder, " + "isLinkedWithImpFolder, keyOfImpLinkedFolder," + "isLinkedWithArchivedFolder, keyOfArchiveLinkedFolder " + "FROM links_table"
            )
            db.execSQL("DROP TABLE links_table")
            db.execSQL("ALTER TABLE links_table_new RENAME TO links_table")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `home_screen_list_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `position` INTEGER NOT NULL, `folderName` TEXT NOT NULL, `shouldSavedLinksTabVisible` INTEGER NOT NULL, `shouldImpLinksTabVisible` INTEGER NOT NULL)")
        }
    }
    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP TABLE IF EXISTS home_screen_list_table")
            db.execSQL("CREATE TABLE IF NOT EXISTS `shelf` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `shelfName` TEXT NOT NULL, `shelfIconName` TEXT NOT NULL, `folderIds` TEXT NOT NULL)")
            db.execSQL("CREATE TABLE IF NOT EXISTS `home_screen_list_table` (`primaryKey` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id` INTEGER NOT NULL, `position` INTEGER NOT NULL, `folderName` TEXT NOT NULL, `parentShelfID` INTEGER NOT NULL)")
        }
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): LocalDatabase {
        return Room.databaseBuilder(
            app, LocalDatabase::class.java, "linkora_db"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build()
    }

}