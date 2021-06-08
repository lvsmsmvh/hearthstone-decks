package com.cyberquick.hearthstonedecks.ui.deck

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.other.api.HearthstoneApi
import com.cyberquick.hearthstonedecks.other.firebase.FirebaseHelper

class DeckDetailsPresenter(val view: DeckDetailsContract.View)
    : DeckDetailsContract.Presenter {

    private lateinit var currentDeck: Deck

    private var isDeckInFavorite: Boolean = false
    set(value) {
        field = value
        view.setFavoriteIconIcon(if (value) R.drawable.ic_star_filled else R.drawable.ic_star)
    }

    override fun onInit(deck: Deck) {
        currentDeck = deck

        view.showDeckPreview(deck)

        showDeckDetailsOrLoadThem()
    }

    private fun showDeckDetailsOrLoadThem() {
        currentDeck.deckDetails?.let {
            view.showDeckDetails(it)
            return
        }

        view.showLoadingScreen()

        HearthstoneApi.loadDeckDetails(view.getActivityInstance(), currentDeck) { deckDetails ->
            if (deckDetails == null)
                view.showError()
            else {
                currentDeck.deckDetails = deckDetails
                view.showDeckDetails(deckDetails)
            }
        }
    }

    override fun onCopyButtonClick() {
        (view.getActivityInstance().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("copy", currentDeck.deckDetails!!.code))

        view.outputMessage("Copied to clipboard!")
    }

    override fun configureFavoriteIcon() {
        view.setFavoriteIconClickable(false)

        FirebaseHelper.isInFavoriteList(currentDeck) { exists ->
            isDeckInFavorite = exists
            view.setFavoriteIconClickable(true)
        }
    }

    override fun onFavoriteIconClick() {
        view.setFavoriteIconClickable(false)

        if (isDeckInFavorite) {
            FirebaseHelper.removeFromFavorite(currentDeck) { successful ->
                proceedResultFromFirebase(
                    resultIsSuccessful = successful,
                    isInFavorite = false,
                    messageWhenSuccessful = "Removed from favorite"
                )
            }
        } else {
            FirebaseHelper.saveDeckToFavorite(currentDeck) { successful ->
                proceedResultFromFirebase(
                    resultIsSuccessful = successful,
                    isInFavorite = true,
                    messageWhenSuccessful = "Added to favorite"
                )
            }
        }
    }

    private fun proceedResultFromFirebase(
        resultIsSuccessful: Boolean,
        isInFavorite: Boolean,
        messageWhenSuccessful: String
    ) {
        if (resultIsSuccessful) {
            isDeckInFavorite = isInFavorite
            view.outputMessage(messageWhenSuccessful)
        } else view.outputMessage("Failed")

        view.setFavoriteIconClickable(true)
    }
}