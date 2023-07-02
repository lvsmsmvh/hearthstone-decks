package com.cyberquick.hearthstonedecks.domain.entities

data class DecksFilter(
    val prompt: String,
    val heroes: Set<Hero>
) {
    companion object {
        val default = DecksFilter("", Hero.values().toSet())
    }

    fun isCustom() = this != default

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = prompt.hashCode()
        result = 31 * result + heroes.hashCode()
        return result
    }
}
