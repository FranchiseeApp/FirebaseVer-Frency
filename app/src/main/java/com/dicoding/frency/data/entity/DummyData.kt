package com.dicoding.frency.data.entity

object DummyData {
    val dataDummy = mutableListOf<FranchiseData>()

    init {
        for (i in 1..10) {
            val dummyData = FranchiseData(
                "$i",
                "item $i",
                "Item $i address",
                "Item descriptionn $i",
                "Category $i",
                "'$i'+9090909+'$i'",
                listOf(
                    FranchiseItem("Type1"),
                    FranchiseItem("Type2"),
                    FranchiseItem("Type3")
                ), // Replace with actual types
                listOf("https://picsum.photos/720", "https://picsum.photos/720") // Replace with actual URLs
            )
            dataDummy.add(dummyData)
        }
    }
}
