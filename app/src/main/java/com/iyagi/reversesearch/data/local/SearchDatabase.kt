package com.iyagi.reversesearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iyagi.reversesearch.data.models.SearchHistory
import com.iyagi.reversesearch.data.models.Bookmark

@Database(
    entities = [SearchHistory::class, Bookmark::class],
    version = 1,
    exportSchema = false
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        const val DATABASE_NAME = "iyagi_database"
    }
}
