package com.bestemorgul.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bestemorgul.data.ToDoDao
import com.bestemorgul.todoapp.model.ToDoModel

@Database(entities = [ToDoModel::class], version = 3, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun todoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null
        fun getDatabase(context: Context): ToDoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}