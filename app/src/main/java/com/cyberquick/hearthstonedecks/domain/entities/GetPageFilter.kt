package com.cyberquick.hearthstonedecks.domain.entities

data class GetPageFilter(
    val prompt: String,
    val heroes: Set<Hero>
) {
    companion object {
        val default = GetPageFilter("", Hero.values().toSet())
    }
}
