package com.librarys.ferreira.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.librarys.ferreira.core.data.local.converters.Converters
import com.librarys.ferreira.core.data.local.dao.AccountDao
import com.librarys.ferreira.core.data.local.entities.AccountEntity

@Database(entities = [AccountEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object {
        const val DATABASE_NAME = "mesas_proprietarias_db"
    }


}