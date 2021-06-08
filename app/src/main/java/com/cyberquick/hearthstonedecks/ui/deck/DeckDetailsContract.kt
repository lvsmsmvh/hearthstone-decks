package com.cyberquick.hearthstonedecks.ui.deck

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.model.DeckDetails

interface DeckDetailsContract {
    interface View {
        fun showLoadingScreen()
        fun showError(errorString: String? = null)

        fun showDeckPreview(deck: Deck)
        fun showDeckDetails(deckDetails: DeckDetails)

        fun getActivityInstance(): AppCompatActivity

        fun setFavoriteIconIcon(res: Int)
        fun setFavoriteIconClickable(clickable: Boolean)

        fun outputMessage(message: String)
    }

    interface Presenter {
        fun onInit(deck: Deck)
        fun onFavoriteIconClick()
        fun onCopyButtonClick()
        fun configureFavoriteIcon()
    }
}