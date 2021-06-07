package com.cyberquick.hearthstonedecks.other.firebase

import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.other.extensions.id
import com.cyberquick.hearthstonedecks.other.extensions.logNav
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseHelper {
    companion object {
        private fun decksStorage(uid: String) =
            Firebase.database.reference
                .child("decks")
                .child(uid)

        fun saveDeckToFavorite(deck: Deck, callback: (successful: Boolean) -> Unit) {
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                callback(false)
                return
            }

            decksStorage(user.uid)
                .child(deck.id())
                .setValue(deck)
                .addOnFailureListener {
                    logNav("firebase error " + it.message)
                }
                .addOnCompleteListener {
                    logNav("firebase complete")
                    callback(it.isSuccessful)
                }
                .addOnSuccessListener {
                    logNav("firebase successful")
                }
                .addOnCanceledListener {
                    logNav("firebase canceled")
                }
        }

        fun removeFromFavorite(deck: Deck, callback: (successful: Boolean) -> Unit) {
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                callback(false)
                return
            }

            decksStorage(user.uid)
                .child(deck.id())
                .removeValue()
                .addOnFailureListener {
                    logNav("firebase error " + it.message)
                }
                .addOnCompleteListener {
                    logNav("firebase complete")
                    callback(it.isSuccessful)
                }
                .addOnSuccessListener {
                    logNav("firebase successful")
                }
                .addOnCanceledListener {
                    logNav("firebase canceled")
                }
        }

        fun getFavoriteDecks(callback: (list: List<Deck>?) -> Unit) {
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                callback(null)
                return
            }

            decksStorage(user.uid)
                .get()
                .addOnSuccessListener {
                    val list = it.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Deck::class.java)
                    }
                    callback(list.filterNotNull())
                }
                .addOnFailureListener {
                    callback(null)
                }
        }
    }
}