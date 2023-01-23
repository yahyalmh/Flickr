package com.example.data.common.database.di

import android.content.Context
import androidx.room.Room
import com.example.data.common.database.AppDatabase
import com.example.data.common.database.DbConfig.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBookmarkDao(database: AppDatabase) = database.bookmarkDao()

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: AppDatabase) = database.searchHistoryDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

}
