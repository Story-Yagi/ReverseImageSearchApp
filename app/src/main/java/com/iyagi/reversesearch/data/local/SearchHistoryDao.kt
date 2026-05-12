package com.iyagi.reversesearch.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.iyagi.reversesearch.data.models.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insertHistory(history: SearchHistory)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<SearchHistory>>

    @Query("SELECT * FROM search_history WHERE id = :id")
    suspend fun getHistoryById(id: Int): SearchHistory?

    @Delete
    suspend fun deleteHistory(history: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun deleteAllHistory()

    @Query("SELECT COUNT(*) FROM search_history")
    fun getHistoryCount(): Flow<Int>
}
