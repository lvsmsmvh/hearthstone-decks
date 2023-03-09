package com.cyberquick.hearthstonedecks.utils

import android.app.Activity
import android.content.Context
import com.cyberquick.hearthstonedecks.presentation.dialogs.DialogGiveUsFiveStars

class CustomAppReviewer(private val activity: Activity) {

    private val preferences = Preferences.getInstance(activity)

    fun ask() {
        DialogGiveUsFiveStars(
            activity = activity,
            onYesClick = {
                preferences.setUserRatedApp()
                activity.openGooglePlayStorePage()
            },
            onNoClick = {
            }
        ).show()

        preferences.increaseAmountOfTimesRateAppAsked()
    }

    companion object {
        fun shouldShow(context: Context): Boolean {
            val preferences = Preferences.getInstance(context)

            if (preferences.getHasUserRatedApp()) {
                return false
            }

            val amountOfLaunches = preferences.getAmountOfTimesAppLaunch()

            return when (preferences.getAmountOfTimesRateAppAsked()) {
                0 -> amountOfLaunches >= LAUNCHES_BEFORE_FIRST_ASK
                1 -> amountOfLaunches >= LAUNCHES_BEFORE_SECOND_ASK
                2 -> amountOfLaunches >= LAUNCHES_BEFORE_THIRD_ASK
                3 -> amountOfLaunches >= LAUNCHES_BEFORE_FOURTH_ASK
                4 -> amountOfLaunches >= LAUNCHES_BEFORE_FIFTH_ASK
                else -> false
            }
        }

        private const val LAUNCHES_BEFORE_FIRST_ASK = 3
        private const val LAUNCHES_BEFORE_SECOND_ASK = 6
        private const val LAUNCHES_BEFORE_THIRD_ASK = 12
        private const val LAUNCHES_BEFORE_FOURTH_ASK = 24
        private const val LAUNCHES_BEFORE_FIFTH_ASK = 48
    }
}