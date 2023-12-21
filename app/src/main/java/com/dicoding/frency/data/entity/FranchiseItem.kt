package com.dicoding.frency.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FranchiseItem(
    var type: String = "",
    var facility: String = "",
    var price: String = ""
) : Parcelable

data class FranchiseData(
    val userId: String? = null,
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val category: String = "",
    val phoneNumber: String = "",
    val franchiseTypes: List<FranchiseItem> = emptyList(),
    val images: List<String> = emptyList(),
    var documentId: String = "",
)

data class FranchiseType(val name: String)
