package com.cyberquick.hearthstonedecks.domain.entities

data class SetGroup(
    val slug: String,             // Wolf
    val year: String,                // 2023
    val cardSets: List<String>,   // "fractured-in-alterac-valley", "united-in-stormwind"
    val name: String,           // Year of the Wolf
)