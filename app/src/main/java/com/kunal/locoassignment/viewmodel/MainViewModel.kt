package com.kunal.locoassignment.viewmodel

import android.widget.AbsListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kunal.locoassignment.model.ResultDetail
import com.kunal.locoassignment.model.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val searchRepository = SearchRepository()

    private val _searchResultLiveData: MutableLiveData<List<ResultDetail>?> by lazy { MutableLiveData<List<ResultDetail>?>() }
    val searchResultLiveData: LiveData<List<ResultDetail>?> = _searchResultLiveData

    private val _progressLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val progressLiveData: LiveData<Boolean> = _progressLiveData

    private var pageNo: Int = 1
    private var currentList: MutableList<ResultDetail> =
        searchResultLiveData.value?.toMutableList() ?: mutableListOf()
    private var moreResultToFetched = true
    internal val options = arrayOf("Random", "Year", "Rating")
    internal var selectedOption = options[0]


    fun getResult(
        searchQuery: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _progressLiveData.postValue(true)
            val response = searchRepository.getResult(searchQuery, pageNo)
            moreResultToFetched = response.body()?.response == "True"
            if (response.isSuccessful) {
                val newSearchList: List<ResultDetail> = response.body()?.search ?: listOf()
                newSearchList.addRatingToResult()
                currentList.addAll(newSearchList)

                when (selectedOption) {
                    options[1] -> sortListByYear()
                    options[2] -> sortListByRating()
                }
                _searchResultLiveData.postValue(currentList)
            } else {
                _searchResultLiveData.postValue(currentList)
            }
            _progressLiveData.postValue(false)
        }
    }

    private fun List<ResultDetail>?.addRatingToResult() {
        this?.forEach { result ->
            result.rating = Random.nextInt(1, 11)
        }
    }

    private var isScrolling = false
    private var currentItem: Int = 0
    private var totalItems: Int = 0
    private var scrollOutItems: Int = 0
    private var searchQuery: String = ""

    fun getEndlessScrollListener(layoutManager: LayoutManager) =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager.childCount
                totalItems = layoutManager.itemCount
                scrollOutItems =
                    (layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                if ((currentItem + scrollOutItems >= totalItems) && isScrolling && moreResultToFetched) {
                    isScrolling = false
                    pageNo++
                    getResult(searchQuery)
                }
            }
        }

    fun clearOldData() {
        pageNo = 1
        currentList.clear()
    }

    fun setSearchQuery(enteredQuery: String) {
        searchQuery = enteredQuery
    }

    fun sortListByYear() {
        viewModelScope.launch {
            _progressLiveData.value = true
            _searchResultLiveData.postValue(currentList.sortedBy {
                it.year.toIntOrNull() ?: Int.MAX_VALUE
            })
            _progressLiveData.value = false
        }
    }

    fun sortListByRating() {
        viewModelScope.launch {
            _progressLiveData.value = true
            _searchResultLiveData.postValue(currentList.sortedByDescending {
                it.rating
            })
            _progressLiveData.value = false
        }
    }

    fun sortListByRandom() {
        viewModelScope.launch {
            _progressLiveData.value = true
            _searchResultLiveData.postValue(currentList.shuffled(Random(System.currentTimeMillis())))
            _progressLiveData.value = false
        }
    }
}