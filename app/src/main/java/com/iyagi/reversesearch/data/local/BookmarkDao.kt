package com.iyagi.reversesearch.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.iyagi.reversesearch.data.models.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: Int): Bookmark?

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks()

    @Query("SELECT COUNT(*) FROM bookmarks")
    fun getBookmarkCount(): Flow<Int>

    @Query("SELECT * FROM bookmarks WHERE imageUrl = :imageUrl")
    suspend fun getBookmarkByImageUrl(imageUrl: String): Bookmark?
}
