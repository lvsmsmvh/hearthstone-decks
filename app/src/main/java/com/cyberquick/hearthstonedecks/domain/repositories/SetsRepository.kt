package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.entities.DataAboutSet

interface SetsRepository {
    fun refreshSets()
    fun getSetName(setId: Int): String
    fun getDataAboutSet(setId: Int): DataAboutSet
}