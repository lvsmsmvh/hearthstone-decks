package com.cyberquick.hearthstonedecks.other.firebase

import com.cyberquick.hearthstonedecks.model.Deck
import com.cyberquick.hearthstonedecks.other.extensions.id
import com.cyberquick.hearthstonedecks.other.extensions.logNav
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseHelper {
    companion object {

        private val decksStorage get() = Firebase.database.reference
            .child("decks")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        fun saveDeckToFavorite(deck: Deck, callback: (successful: Boolean) -> Unit) {
            decksStorage
                .child(deck.id())
                .setValue(deck)
                .addOnCompleteListener {
                    callback(it.isSuccessful)
                }
                .addOnCanceledListener {
                    callback(false)
                }
        }

        fun removeFromFavorite(deck: Deck, callback: (successful: Boolean) -> Unit) {
            decksStorage
                .child(deck.id())
                .removeValue()
                .addOnCompleteListener {
                    callback(it.isSuccessful)
                }
                .addOnCanceledListener {
                    callback(false)
                }
        }

        fun getFavoriteDecks(callback: (list: List<Deck>?) -> Unit) {
            decksStorage
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

        fun isInFavoriteList(deck: Deck, callback: (exists: Boolean) -> Unit) {
            decksStorage
                .child(deck.id())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        callback(snapshot.exists())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }
}