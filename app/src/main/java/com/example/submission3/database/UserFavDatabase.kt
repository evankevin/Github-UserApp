package com.example.submission3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFav::class], version = 1)
abstract class UserFavDatabase : RoomDatabase() {
    abstract fun UserFavDao(): UserFavDao
    companion object {
        @Volatile
        private var INSTANCE: UserFavDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): UserFavDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserFavDatabase::class.java, "user_database")
                        .build()
                }
            }
            return INSTANCE as UserFavDatabase
        }
    }
}