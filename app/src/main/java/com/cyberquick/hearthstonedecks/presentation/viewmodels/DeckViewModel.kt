package com.cyberquick.hearthstonedecks.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
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
) : BaseViewModel() {

    val stateDeck: LiveData<LoadingState<Deck>> = MutableLiveData()
    val stateDeckSaved: LiveData<SavedState> = MutableLiveData()
    val error: LiveData<String?> = MutableLiveData()

    fun loadDeck(deckPreview: DeckPreview) {
        if (isDeckLoaded(deckPreview)) return
        makeLoadingRequest(stateDeck) {
            return@makeLoadingRequest getDeckUseCase(deckPreview)
        }
    }

    fun clickedOnSaveButton(deck: Deck, cards: List<Card>) {
        viewModelScope.launch(createJob() + Dispatchers.IO) {
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
    }

    fun updateIsDeckSaved(deckPreview: DeckPreview) {
        viewModelScope.launch(createJob() + Dispatchers.IO) {
            stateDeckSaved.postValue(SavedState.fromResult(isDeckFavoriteUseCase(deckPreview)))
        }
    }

    private fun isDeckLoaded(deckPreview: DeckPreview): Boolean {
        return stateDeck.value.asLoaded()?.result?.deckPreview == deckPreview
    }

    override fun onCleared() {
        super.onCleared()
        stateDeck.setToDefault()
        stateDeckSaved.setToDefault()
        error.setToDefault()
    }
}