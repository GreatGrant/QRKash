package com.gralliams.qrkash.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransactionItem::class], version = 1, exportSchema = false)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
}
