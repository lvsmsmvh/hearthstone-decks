package com.cyberquick.hearthstonedecks.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.usecases.cached.CacheDeckUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.common.GetCardsUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.common.GetDeckUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.AddDeckToFavoriteUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.IsDeckInFavoriteUseCase
import com.cyberquick.hearthstonedecks.domain.usecases.favorite.RemoveDeckFromFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val getDeckUseCase: GetDeckUseCase,
    private val isDeckFavoriteUseCase: IsDeckInFavoriteUseCase,
    private val addDeckToFavoriteUseCase: AddDeckToFavoriteUseCase,
    private val removeDeckFromFavoriteUseCase: RemoveDeckFromFavoriteUseCase,
    private val cacheDeckUseCase: CacheDeckUseCase,
    private val getCardsUseCase: GetCardsUseCase,
) : BaseViewModel() {

    init {
        Log.i("tag_retrofit", "DeckViewModel init()")
        val sampleDeckCode = "AAECAaIHBKOgBNi2BOnQBL7wBA316APg8QPRoAT9rAS" +
                "EsgS3swTVtgTjuQSJ0gTj0wT03QSs7QTL7QQA"
    }

    val stateCardsLoading: LiveData<LoadingState<List<Card>>> = MutableLiveData(LoadingState.Idle)
    val stateDeckLoading: LiveData<LoadingState<Deck>> = MutableLiveData(LoadingState.Idle)
    val stateDeckSaving: LiveData<SavedState> = MutableLiveData()

    fun loadDeck(deckPreview: DeckPreview) = viewModelScope.launch(Dispatchers.IO) {
        if (stateDeckLoading.value?.canBeLoaded() == false) return@launch
        stateDeckLoading.postValue(LoadingState.Loading)
        val result = getDeckUseCase(deckPreview)
        if (result is Result.Success) {
            cacheDeckUseCase(result.data)
        }

        stateDeckLoading.postValue(LoadingState.fromResult(result))
    }

    fun loadCards(deck: Deck) = viewModelScope.launch {
        if (stateCardsLoading.value?.canBeLoaded() == false) return@launch
        stateCardsLoading.postValue(LoadingState.Loading)
        stateCardsLoading.postValue(LoadingState.fromResult(getCardsUseCase.invoke(deck)))
    }

    fun clickedOnSaveButton(deck: Deck) = viewModelScope.launch(Dispatchers.IO) {
        when (stateDeckSaving.value) {
            SavedState.NotSaved -> {
                val saveDeckResult = when (val result = addDeckToFavoriteUseCase(deck)) {
                    is Result.Success -> SavedState.Saved
                    is Result.Error -> SavedState.Failed(result.exception.message.toString())
                }
                stateDeckSaving.postValue(saveDeckResult)
            }
            SavedState.Saved -> {
                val removeDeckResult = when (val result = removeDeckFromFavoriteUseCase(deck)) {
                    is Result.Success -> SavedState.NotSaved
                    is Result.Error -> SavedState.Failed(result.exception.message.toString())
                }
                stateDeckSaving.postValue(removeDeckResult)
            }
            is SavedState.Failed -> updateIsDeckSaved(deck.deckPreview)
            null -> {}
        }
    }

    fun updateIsDeckSaved(deckPreview: DeckPreview) = viewModelScope.launch(Dispatchers.IO) {
        stateDeckSaving.postValue(SavedState.fromResult(isDeckFavoriteUseCase(deckPreview)))
    }
}