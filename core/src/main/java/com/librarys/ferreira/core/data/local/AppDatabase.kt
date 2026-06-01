package com.librarys.ferreira.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.librarys.ferreira.core.data.local.converters.Converters
import com.librarys.ferreira.core.data.local.dao.AccountDao
import com.librarys.ferreira.core.data.local.dao.TradesDao
import com.librarys.ferreira.core.data.local.entities.AccountEntity
import com.librarys.ferreira.core.data.local.entities.TradesEntity

@Database(entities = [AccountEntity::class, TradesEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun tradesDao() : TradesDao

    companion object {
        const val DATABASE_NAME = "mesas_proprietarias_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `trades` (
                        `id` TEXT NOT NULL, 
                        `date` INTEGER NOT NULL, 
                        `accountInfoId` TEXT NOT NULL, 
                        `accountNumber` TEXT NOT NULL, 
                        `symbolAtivo` TEXT NOT NULL, 
                        `contratos` INTEGER NOT NULL, 
                        `profit` REAL NOT NULL, 
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }
    }


}