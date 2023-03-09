package com.cyberquick.hearthstonedecks

import android.app.Application
import com.cyberquick.hearthstonedecks.utils.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    lateinit var preferences: Preferences
        private set

    override fun onCreate() {
        super.onCreate()

        preferences = Preferences(this)
    }
}