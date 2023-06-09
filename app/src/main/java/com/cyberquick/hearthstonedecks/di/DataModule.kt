package com.cyberquick.hearthstonedecks.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.cyberquick.hearthstonedecks.data.db.DATABASE_NAME
import com.cyberquick.hearthstonedecks.data.db.RoomDB
import com.cyberquick.hearthstonedecks.data.repository.OnlineDecksImpl
import com.cyberquick.hearthstonedecks.data.repository.FavoriteDecksImpl
import com.cyberquick.hearthstonedecks.data.repository.SetsImpl
import com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone.BattleNetApi
import com.cyberquick.hearthstonedecks.data.server.battlenet.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.SetsRepository
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
    fun provideContext(appInstance: Application): Context = appInstance.applicationContext

    // database
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application) = Room
        .databaseBuilder(application, RoomDB::class.java, DATABASE_NAME)
//        .fallbackToDestructiveMigration()
        .build()


    // retrofit
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
    fun provideOAuthApi(): OAuthApi = RetrofitBuilder(
        OAuthApi::class.java, BLIZZARD_OAUTH_URL
    ).build()


    @Provides
    @Singleton
    fun provideCardsApi(): BattleNetApi = RetrofitBuilder(
        BattleNetApi::class.java, BLIZZARD_API_URL
    ).build()



    // other
//    @Provides
//    @Singleton
//    fun provideBattleNetApi(
//        oAuthApi: OAuthApi,
//        deckApi: DeckApi,
//        metadataApi: MetadataApi,
//    ) = BattleNetApi(oAuthApi, deckApi, metadataApi)

    @Provides
    fun provideDeckDao(appDatabase: RoomDB) = appDatabase.deckDao()

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
    fun provideSetsRepository(
        setsImpl: SetsImpl
    ): SetsRepository = setsImpl
}