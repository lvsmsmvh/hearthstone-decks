package com.cyberquick.hearthstonedecks.di

import android.app.Application
import androidx.room.Room
import com.cyberquick.hearthstonedecks.data.db.DATABASE_NAME
import com.cyberquick.hearthstonedecks.data.db.RoomDB
import com.cyberquick.hearthstonedecks.data.repository.OnlineDecksImpl
import com.cyberquick.hearthstonedecks.data.repository.CacheDecksImpl
import com.cyberquick.hearthstonedecks.data.repository.CardsImpl
import com.cyberquick.hearthstonedecks.data.repository.FavoriteDecksImpl
import com.cyberquick.hearthstonedecks.data.server.blizzard.hearthstone.HearthstoneApi
import com.cyberquick.hearthstonedecks.data.server.blizzard.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.CacheDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.CardsRepository
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application) = Room
        .databaseBuilder(application, RoomDB::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDeckDao(appDatabase: RoomDB) = appDatabase.deckDao()

    @Provides
    fun provideCardPreviewDao(appDatabase: RoomDB) = appDatabase.cardPreviewDao()

    @Provides
    @Singleton
    fun provideLocalDataRepository(
        savedDecksImpl: CacheDecksImpl
    ): CacheDecksRepository = savedDecksImpl

    @Provides
    @Singleton
    fun provideServerDataRepository(
        onlineDecksImpl: OnlineDecksImpl
    ): OnlineDecksRepository = onlineDecksImpl

    @Provides
    @Singleton
    fun provideFavoriteDataRepository(
        favoriteDecksImpl: FavoriteDecksImpl
    ): FavoriteDecksRepository = favoriteDecksImpl

    @Provides
    @Singleton
    fun provideCardsRepository(
        cardsImpl: CardsImpl
    ): CardsRepository = cardsImpl

    companion object {
        private const val BLIZZARD_API_URL = "https://eu.api.blizzard.com/"
        private const val BLIZZARD_OAUTH_URL = "https://us.battle.net/"
    }

    private class RetrofitBuilder<API_TYPE>(
        private val apiClass: Class<API_TYPE>,
        private val baseUrl: String
    ) {
        fun build(): API_TYPE =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(apiClass)
    }

    @Provides
    @Singleton
    fun provideCardsApi(): HearthstoneApi = RetrofitBuilder(
        HearthstoneApi::class.java,
        BLIZZARD_API_URL
    ).build()

    @Provides
    @Singleton
    fun provideOAuthApi(): OAuthApi = RetrofitBuilder(
        OAuthApi::class.java,
        BLIZZARD_OAUTH_URL
    ).build()
}