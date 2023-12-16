//package com.dicoding.frency.data.local
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//
//@Dao
//interface WishlistDAO {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(wishlist: Wishlist)
//
//    @Update
//    suspend fun update(wishlist: Wishlist)
//
//    @Delete
//    suspend fun delete(wishlist: Wishlist)
//
//    @Query("SELECT * from Wishlist")
//    suspend fun getAllWishlist(): LiveData<List<Wishlist>>
//
//    @Query("SELECT EXISTS(SELECT * FROM Wishlist WHERE Wishlist.name = :name)")
//    suspend fun isFavorite(name: String): LiveData<Boolean>
//}