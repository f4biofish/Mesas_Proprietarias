package com.librarys.ferreira.core.di

import android.content.Context
import androidx.room.Room
import com.librarys.ferreira.core.data.local.AppDatabase
import com.librarys.ferreira.core.data.local.dao.AccountDao
import com.librarys.ferreira.core.data.local.dao.TradesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountDao(db: AppDatabase): AccountDao {
        return db.accountDao()
    }

    @Provides
    @Singleton
    fun provideTradesDao(db: AppDatabase): TradesDao {
        return db.tradesDao()
    }
}