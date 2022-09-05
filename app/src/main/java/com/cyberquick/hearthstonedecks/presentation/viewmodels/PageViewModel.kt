package com.cyberquick.hearthstonedecks.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.cyberquick.hearthstonedecks.domain.entities.Page
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePagesQuantityUseCase
import com.cyberquick.hearthstonedecks.utils.transform
import com.cyberquick.hearthstonedecks.utils.transformWithDefault
import com.cyberquick.hearthstonedecks.utils.transformWithDoubleTrigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class OnlinePageViewModel @Inject constructor(
    getOnlinePagesQuantityUseCase: GetOnlinePagesQuantityUseCase,
    getOnlinePageUseCase: GetOnlinePageUseCase,
) : PageViewModel(getOnlinePagesQuantityUseCase, getOnlinePageUseCase)

@HiltViewModel
class FavoritePageViewModel @Inject constructor(
    getFavoritePagesQuantityUseCase: GetFavoritePagesQuantityUseCase,
    getFavoritePageUseCase: GetFavoritePageUseCase,
) : PageViewModel(getFavoritePagesQuantityUseCase, getFavoritePageUseCase)

open class PageViewModel(
    private val getPagesQuantityUseCase: GetPagesQuantityUseCase,
    private val getPageUseCase: GetPageUseCase,
) : BaseViewModel() {

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

    sealed class PositionOutput {
        object Loading : PositionOutput()
        data class Show(val current: Int, val total: Int) : PositionOutput()
    }

    data class AllowNavigation(val previous: Boolean, val next: Boolean) {
        companion object {
            fun blocked() = AllowNavigation(previous = false, next = false)
        }
    }

    private val currentPosition: LiveData<Int> = MutableLiveData(FIRST_PAGE_INDEX)
    val totalPagesAmountLoading: LiveData<LoadingState<Int>> = MutableLiveData()

    val pageLoading: LiveData<LoadingState<Page>> =
        transform(totalPagesAmountLoading) { totalPagesAmountLoading ->
            if (totalPagesAmountLoading.isLoaded()) updateCurrentPage(true)
        }

    val positionOutput: LiveData<PositionOutput> = transformWithDoubleTrigger(
        firstSource = currentPosition,
        secondSource = totalPagesAmountLoading
    ) { current, totalLiveData ->
        val total = totalLiveData?.asLoaded()?.result
        return@transformWithDoubleTrigger if (current != null && total != null) {
            PositionOutput.Show(current, total)
        } else PositionOutput.Loading
    }

    val allowNavigation: LiveData<AllowNavigation> = transformWithDefault(
        source = positionOutput,
        defaultValue = AllowNavigation.blocked(),
    ) { data ->
        return@transformWithDefault when (data) {
            is PositionOutput.Loading -> AllowNavigation.blocked()
            is PositionOutput.Show -> {
                AllowNavigation(data.current > 1, data.current < data.total)
            }
        }
    }

    private var loadingPageJob: Job? = null

    fun updateAmountOfPages(evenIfLoaded: Boolean) {
        if (totalPagesAmountLoading.value.isLoaded() && !evenIfLoaded) return
        makeLoadingRequest(totalPagesAmountLoading) {
            getPagesQuantityUseCase()
        }
    }

    fun updateCurrentPage(evenIfLoaded: Boolean) {
        Log.i("tag_page", "updateCurrentPage")
        val total = totalPagesAmountLoading.value.asLoaded()?.result ?: run {
            updateAmountOfPages(true)
            return
        }

        val current = currentPosition.value
        val nextToLoad = when {
            current == null -> 1
            current > total -> total
            else -> current
        }
        loadPage(nextToLoad, evenIfLoaded)
    }

    fun loadNextPage() {
        loadPage(currentPosition.value!! + 1)
    }

    fun loadPreviousPage() {
        loadPage(currentPosition.value!! - 1)
    }

    private fun loadPage(pageNumber: Int, evenIfLoaded: Boolean = false) {
        Log.i("tag_lv", "Load page $pageNumber")
        if (isPageLoaded(pageNumber) && !evenIfLoaded) return
        loadingPageJob?.cancel()
        currentPosition.postValue(pageNumber)
        loadingPageJob = makeLoadingRequest(pageLoading, allowInterrupt = true) {
            getPageUseCase(pageNumber)
        }
    }

    private fun isPageLoaded(pageNumber: Int) =
        pageLoading.value.asLoaded()?.result?.number == pageNumber
}

