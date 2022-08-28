package com.cyberquick.hearthstonedecks.domain.entities

data class Card(
    val id: Int,
    val collectible: Int,
    val slug: String,
    val classId: Int,
    val multiClassIds: IntArray,
    val cardTypeId: Int,
    val cardSetId: Int,
    val rarityId: Int,
    val artistName: String,
    val health: Int,
    val attack: Int,
    val manaCost: Int,
    val name: String,
    val text: String,
    val image: String,
    val imageGold: String,
    val flavorText: String,
    val cropImage: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Card
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}