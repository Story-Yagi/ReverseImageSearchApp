package com.iyagi.reversesearch.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUrl: String,
    val imagePath: String? = null,
    val searchResults: String, // JSON string
    val timestamp: Long = System.currentTimeMillis(),
    val searchEngines: String // Comma-separated engine names
)

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUrl: String,
    val imagePath: String? = null,
    val title: String = "Untitled",
    val sourceUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class SearchResult(
    val engine: String,
    val results: List<ImageResult>
)

data class ImageResult(
    val title: String,
    val url: String,
    val imageUrl: String,
    val description: String = ""
)

data class SearchRequest(
    val imageUri: String,
    val engines: List<String> = listOf("google", "yandex", "twitter", "tineye", "bing", "baidu")
)
