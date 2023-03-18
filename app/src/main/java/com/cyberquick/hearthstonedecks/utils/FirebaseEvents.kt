package com.cyberquick.hearthstonedecks.utils

import android.content.Context
import android.util.Log
import com.cyberquick.hearthstonedecks.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent


enum class Event {

    OPEN_APP,

    APPLY_FILTER,

    TOOLBAR_CLICK_MENU,
    TOOLBAR_CLICK_BACK,
    TOOLBAR_CLICK_NEXT_PAGE,
    TOOLBAR_CLICK_PREVIOUS_PAGE,
    TOOLBAR_CLICK_FILTERS,

    DRAWER_CLICK_DECKS_STANDARD,
    DRAWER_CLICK_DECKS_WILD,
    DRAWER_CLICK_DECKS_SAVED,
    DRAWER_CLICK_ABOUT_APP,
    DRAWER_CLICK_EXIT,

    DIALOG_EXIT_SHOW,
    DIALOG_EXIT_YES,
    DIALOG_EXIT_NO,

    DIALOG_RATE_SHOW,
    DIALOG_RATE_NO_BY_BUTTON,
    DIALOG_RATE_NO_BY_DISMISS,
    DIALOG_RATE_YES_BY_LOTTIE,
    DIALOG_RATE_YES_BY_BUTTON,

    DECK_VIEW,
    DECK_CLICK_SAVE_BUTTON,
    DECK_COPY_CODE,
    DECK_SHARE,

    CARDS_START_VIEWING,
    CARD_VIEW,
    ;
}

fun logFirebaseEvent(context: Context?, event: Event, additional: String? = null) {
    if (context == null) {
        return
    }

    var log = event.name.lowercase()
    additional?.let { log += "_" + it.lowercase() }

    Log.d("tag_event", "New firebase event $log")

    if (!BuildConfig.DEBUG) {
        FirebaseAnalytics.getInstance(context).logEvent(log) {}
    }
}