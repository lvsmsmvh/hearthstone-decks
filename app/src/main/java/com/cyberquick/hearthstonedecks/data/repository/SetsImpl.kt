package com.cyberquick.hearthstonedecks.data.repository

import android.content.Context
import android.util.Log
import com.cyberquick.hearthstonedecks.R
import com.cyberquick.hearthstonedecks.data.server.battlenet.BattleNetRepository
import com.cyberquick.hearthstonedecks.domain.entities.DataAboutSet
import com.cyberquick.hearthstonedecks.domain.entities.Expansion
import com.cyberquick.hearthstonedecks.domain.entities.ExpansionYear
import com.cyberquick.hearthstonedecks.domain.repositories.SetsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Scanner
import javax.inject.Inject

class SetsImpl @Inject constructor(
    private val context: Context,
    private val battleNetApi: BattleNetRepository,
) : SetsRepository {

    private var setsFromLocal: List<Expansion> = emptyList()
    private var setsFromOnline: List<Expansion> = emptyList()

    private var setGroupsFromLocal: List<ExpansionYear> = emptyList()
    private var setGroupsFromOnline: List<ExpansionYear> = emptyList()

    override fun refreshSets() {
        CoroutineScope(Dispatchers.IO).launch {
            refreshLocal()
            refreshOnline()
        }
    }

    private fun refreshLocal() {
        if (setsFromLocal.isEmpty()) {
            setsFromLocal = getListOfObjectFromJson(R.raw.sets)
        }

        if (setGroupsFromLocal.isEmpty()) {
            setGroupsFromLocal = getListOfObjectFromJson(R.raw.set_groups)
        }
    }

    private suspend fun refreshOnline() {
        val resultSets = battleNetApi.retrieveSets()
        resultSets.asSuccess()?.let {
            setsFromOnline = it.data
            Log.d("tag_sets", "setsFromOnline() - good")
        }
        resultSets.asError()?.let {
            Log.d("tag_sets", "setsFromOnline() - bad")
            Log.d("tag_sets", it.exception.message.toString())
        }

        val resultSetGroups = battleNetApi.retrieveSetGroups()
        resultSetGroups.asSuccess()?.let {
            setGroupsFromOnline = it.data
            Log.d("tag_sets", "setGroupsFromOnline() - good")
        }
        resultSetGroups.asError()?.let {
            Log.d("tag_sets", "setGroupsFromOnline() - bad")
            Log.d("tag_sets", it.exception.message.toString())
        }
    }

    override fun getSetName(setId: Int): String {
        setsFromLocal.find { it.id == setId }?.let { return it.name }
        setsFromOnline.find { it.id == setId }?.let { return it.name }
        return "Unknown"
    }

    override fun getDataAboutSet(setId: Int): DataAboutSet {
        val unknown = "Unknown"

        val set = setsFromLocal.find { it.id == setId }
            ?: setsFromOnline.find { it.id == setId }
            ?: return DataAboutSet(
                setName = unknown,
                year = null
            )

        val setGroup = setGroupsFromLocal.find { it.cardSets.contains(set.slug) }
            ?: setGroupsFromOnline.find { it.cardSets.contains(set.slug) }

        val year = when (setGroup?.year) {
            null -> null
            else -> setGroup.year + " " + setGroup.name
        }

        return DataAboutSet(
            setName = set.name,
            year = year
        )
    }

    private inline fun <reified T> getListOfObjectFromJson(resourceId: Int): List<T> {
        val inputStream = context.resources.openRawResource(resourceId)
        val jsonString = Scanner(inputStream).useDelimiter("\\A").next()
        val tagGroupType = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(jsonString, tagGroupType)
    }

//    private fun getJsonFromResource(resourceId: Int): String {
//        val inputStream = context.resources.openRawResource(resourceId)
//        return Scanner(inputStream).useDelimiter("\\A").next()
//    }
//
//    private inline fun <reified T> getListOfObjectFromJson(jsonString: String): List<T> {
//        val tagGroupType = object : TypeToken<List<T>>() {}.type
//        return Gson().fromJson(jsonString, tagGroupType)
//    }
}