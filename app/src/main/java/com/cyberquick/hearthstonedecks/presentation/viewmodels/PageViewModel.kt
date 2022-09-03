package com.cyberquick.hearthstonedecks.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberquick.hearthstonedecks.domain.entities.Page
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePagesQuantityUseCase
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
    private val getOnlinePagesQuantityUseCase: GetPagesQuantityUseCase,
    private val getOnlinePageUseCase: GetPageUseCase,
) : BaseViewModel() {

    val amountOfPages: LiveData<LoadingState<Int>> = MutableLiveData()
    val page: LiveData<LoadingState<Page>> = MutableLiveData()
    private var loadingPageJob: Job? = null

    fun loadAmountOfPages() {
        if (amountOfPages.value.isLoaded()) return
        makeLoadingRequest(amountOfPages) {
            getOnlinePagesQuantityUseCase()
        }
    }

    fun loadPage(pageNumber: Int, force: Boolean = false) {
        if (pageIsLoaded(pageNumber) && !force) return
        loadingPageJob?.cancel()

        loadingPageJob = makeLoadingRequest(page, allowInterrupt = true) {
            getOnlinePageUseCase(pageNumber)
        }
    }

    fun updateCurrentPage() {
        page.value?.asLoaded()?.result?.number?.let { loadPage(it, force = true) }
    }

    private fun pageIsLoaded(pageNumber: Int) =
        page.value.asLoaded()?.result?.number == pageNumber
}

