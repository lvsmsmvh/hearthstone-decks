package com.cyberquick.hearthstonedecks.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyberquick.hearthstonedecks.domain.entities.Page
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.online.GetOnlinePagesQuantityUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePageUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.GetFavoritePagesQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun loadAmountOfPages() = viewModelScope.launch(Dispatchers.IO) {
        if (amountOfPages.value.isLoadingOrLoaded()) return@launch
        amountOfPages.postValue(LoadingState.Loading)
        amountOfPages.postValue(LoadingState.fromResult(getOnlinePagesQuantityUseCase()))
    }

    fun loadPage(pageNumber: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page.value.asLoaded()?.result?.number == pageNumber) return@launch
        if (page.value.isLoading()) return@launch
        page.postValue(LoadingState.Loading)
        page.postValue(LoadingState.fromResult(getOnlinePageUseCase(pageNumber)))
    }
}

