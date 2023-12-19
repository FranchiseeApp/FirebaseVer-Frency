package com.dicoding.frency.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Wishlist::class], version = 2)
abstract class WishlistDatabase: RoomDatabase() {

    abstract fun wishlistDao(): WishlistDAO

    companion object {
        @Volatile
        private var INSTANCE: WishlistDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): WishlistDatabase {
            if (INSTANCE == null) {
                synchronized(WishlistDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WishlistDatabase::class.java, "wishlist_database"
                    )
                        .build()
                }
            }
            return INSTANCE as WishlistDatabase
        }
    }
}