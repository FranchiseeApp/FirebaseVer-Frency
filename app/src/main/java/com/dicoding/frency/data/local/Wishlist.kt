package com.dicoding.frency.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Wishlist (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "img_url")
    var imgUrl: String? = null,
): Parcelable