package com.cyberquick.hearthstonedecks.utils

import android.content.Context
import com.cyberquick.hearthstonedecks.App

class Preferences(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun getHasUserRatedApp(): Boolean {
        return prefs.getBoolean(KEY_HAS_USER_RATED_APP, false)
    }

    fun setUserRatedApp() {
        prefs.edit().putBoolean(KEY_HAS_USER_RATED_APP, true).apply()
    }

    fun getAmountOfTimesAppLaunch(): Int {
        return prefs.getInt(KEY_AMOUNT_OF_TIME_APP_LAUNCH, 0)
    }

    fun increaseAmountOfTimesAppLaunch() {
        val previous = getAmountOfTimesAppLaunch()
        prefs.edit().putInt(KEY_AMOUNT_OF_TIME_APP_LAUNCH, previous + 1).apply()
    }

    fun getAmountOfTimesRateAppAsked(): Int {
        return prefs.getInt(KEY_AMOUNT_OF_TIMES_RATE_APP_ASKED, 0)
    }

    fun increaseAmountOfTimesRateAppAsked() {
        val previous = getAmountOfTimesRateAppAsked()
        prefs.edit().putInt(KEY_AMOUNT_OF_TIMES_RATE_APP_ASKED, previous + 1).apply()
    }

    companion object {
        fun getInstance(context: Context): Preferences {
            return (context.applicationContext as App).preferences
        }

        private const val KEY_HAS_USER_RATED_APP = "has_rated"
        private const val KEY_AMOUNT_OF_TIME_APP_LAUNCH = "launch_times"
        private const val KEY_AMOUNT_OF_TIMES_RATE_APP_ASKED = "rate_ask_times"
    }
}