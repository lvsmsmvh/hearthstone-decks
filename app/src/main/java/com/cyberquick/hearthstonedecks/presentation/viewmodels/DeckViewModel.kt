package com.cyberquick.hearthstonedecks.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
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
    private val getCardsUseCase: GetCardsUseCase,
    private val isDeckFavoriteUseCase: IsDeckInFavoriteUseCase,
    private val addDeckToFavoriteUseCase: AddDeckToFavoriteUseCase,
    private val removeDeckFromFavoriteUseCase: RemoveDeckFromFavoriteUseCase,
) : BaseViewModel() {

    init {
        val sampleDeckCode = "AAECAaIHBKOgBNi2BOnQBL7wBA316APg8QPRoAT9rAS" +
                "EsgS3swTVtgTjuQSJ0gTj0wT03QSs7QTL7QQA"
    }

    val stateCards: LiveData<LoadingState<List<Card>>> = MutableLiveData(LoadingState.Idle)
    val stateDeck: LiveData<LoadingState<Deck>> = MutableLiveData(LoadingState.Idle)
    val stateDeckSaved: LiveData<SavedState> = MutableLiveData()
    val error: LiveData<String?> = MutableLiveData()

    fun loadDeck(deckPreview: DeckPreview) = viewModelScope.launch(Dispatchers.IO) {
        if (stateDeck.value.isLoading()) return@launch
        stateDeck.postValue(LoadingState.Loading)
        stateDeck.postValue(LoadingState.fromResult(getDeckUseCase(deckPreview)))
    }

    fun loadCards(deck: Deck) = viewModelScope.launch(Dispatchers.IO) {
        if (stateCards.value.isLoading()) return@launch
        stateCards.postValue(LoadingState.Loading)
        stateCards.postValue(LoadingState.fromResult(getCardsUseCase.invoke(deck)))
    }

    fun clickedOnSaveButton(deck: Deck, cards: List<Card>) = viewModelScope.launch(Dispatchers.IO) {
        val oldSavedState = stateDeckSaved.value ?: SavedState.NotSaved

        val newSavingState = when (oldSavedState) {
            SavedState.Saved -> removeDeckFromFavoriteUseCase(deck.deckPreview)
            SavedState.NotSaved -> addDeckToFavoriteUseCase(deck, cards)
        }

        when (newSavingState) {
            is Result.Success -> stateDeckSaved.postValue(SavedState.opposite(oldSavedState))
            is Result.Error -> {
                error.postValue(newSavingState.exception.message.toString())
                error.postValue(null)
            }
        }
    }

    fun updateIsDeckSaved(deckPreview: DeckPreview) = viewModelScope.launch(Dispatchers.IO) {
        stateDeckSaved.postValue(SavedState.fromResult(isDeckFavoriteUseCase(deckPreview)))
    }
}