package com.iyagi.reversesearch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyagi.reversesearch.data.local.SearchHistoryDao
import com.iyagi.reversesearch.data.local.BookmarkDao
import com.iyagi.reversesearch.data.models.SearchHistory
import com.iyagi.reversesearch.data.models.Bookmark
import com.iyagi.reversesearch.data.models.ImageResult
import com.iyagi.reversesearch.data.remote.SearchEngines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.gson.Gson
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val historyDao: SearchHistoryDao,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Map<String, List<ImageResult>>>(emptyMap())
    val searchResults: StateFlow<Map<String, List<ImageResult>>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _searchHistory = MutableStateFlow<List<SearchHistory>>(emptyList())
    val searchHistory: StateFlow<List<SearchHistory>> = _searchHistory

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks

    init {
        loadHistory()
        loadBookmarks()
    }

    fun performSearch(imageUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val results = SearchEngines.searchAll(imageUrl)
                _searchResults.value = results

                // Save search history
                val gson = Gson()
                val searchHistory = SearchHistory(
                    imageUrl = imageUrl,
                    searchResults = gson.toJson(results),
                    searchEngines = results.keys.joinToString(", ")
                )
                historyDao.insertHistory(searchHistory)
                loadHistory()

            } catch (e: Exception) {
                _errorMessage.value = "검색 실패: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBookmark(imageUrl: String, title: String = "Untitled", sourceUrl: String? = null) {
        viewModelScope.launch {
            try {
                val bookmark = Bookmark(
                    imageUrl = imageUrl,
                    title = title,
                    sourceUrl = sourceUrl
                )
                bookmarkDao.insertBookmark(bookmark)
                loadBookmarks()
            } catch (e: Exception) {
                _errorMessage.value = "북마크 추가 실패: ${e.message}"
            }
        }
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            try {
                bookmarkDao.deleteBookmark(bookmark)
                loadBookmarks()
            } catch (e: Exception) {
                _errorMessage.value = "북마크 삭제 실패: ${e.message}"
            }
        }
    }

    fun deleteHistory(history: SearchHistory) {
        viewModelScope.launch {
            try {
                historyDao.deleteHistory(history)
                loadHistory()
            } catch (e: Exception) {
                _errorMessage.value = "기록 삭제 실패: ${e.message}"
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                historyDao.deleteAllHistory()
                loadHistory()
            } catch (e: Exception) {
                _errorMessage.value = "모든 기록 삭제 실패: ${e.message}"
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            historyDao.getAllHistory().collect { history ->
                _searchHistory.value = history
            }
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            bookmarkDao.getAllBookmarks().collect { bookmarks ->
                _bookmarks.value = bookmarks
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
