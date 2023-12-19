package com.dicoding.frency.ui.franchiseslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.frency.R
import com.dicoding.frency.data.entity.DummyData
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.data.entity.User
import com.dicoding.frency.data.session.SessionManager
import com.dicoding.frency.databinding.ActivityFranchisesListBinding
import com.dicoding.frency.ui.home.FranchiseListAdapter
import com.dicoding.frency.ui.home.RecommendationListAdapter
import com.dicoding.frency.ui.login.LoginActivity
import com.dicoding.frency.utils.ZoomOutPageTransformer
import com.google.firebase.firestore.FirebaseFirestore

class FranchisesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFranchisesListBinding
    private var allFranchises: Boolean? = null
    private var typeFranchises: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFranchisesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allFranchises = intent.getBooleanExtra("allFranchises", false)
        typeFranchises = intent.getStringExtra("typeFranchises")

        if (allFranchises == true) {
            loadData()
        }

        if (!typeFranchises.isNullOrEmpty()) { // Memeriksa apakah variabel tidak kosong atau null
            loadDataType(typeFranchises!!)
        }

        Log.d("GetData", "onCreate: $typeFranchises")




    }

    private fun loadData() {
        val db = FirebaseFirestore.getInstance()
        val franchisesCollection = db.collection("franchises")

        franchisesCollection
            .get()
            .addOnSuccessListener { documents ->
                val franchiseList = mutableListOf<FranchiseData>()

                for (document in documents) {
                    val franchiseData = document.toObject(FranchiseData::class.java)
                    Log.d("FranchiseData", "Data: $franchiseData")
                    franchiseList.add(franchiseData)
                }
                binding.tvResultCount.text = "Showing ${franchiseList.size} result "

                val layoutManager = GridLayoutManager(binding.root.context, 2)
                var recycler = binding.rvFranchises
                recycler.layoutManager = layoutManager
                val adapterList = FranchisesListAdapter(franchiseList)
                recycler.adapter = adapterList

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
                exception.message?.let { Log.e("Firestore", it) }
//                    binding.swipeRefreshLayout?.isRefreshing = false
            }

    }

    private fun loadDataType(type: String) {
        val db = FirebaseFirestore.getInstance()
        val franchisesCollection = db.collection("franchises")

        franchisesCollection
            .get()
            .addOnSuccessListener { documents ->
                val franchiseList = mutableListOf<FranchiseData>()

                for (document in documents) {
                    val franchiseData = document.toObject(FranchiseData::class.java)
                    franchiseList.add(franchiseData)
                }

                // Filter franchiseList untuk mendapatkan data dengan franchiseTypes berisi "Stand"
                val standFranchiseList = franchiseList.filter { franchiseData ->
                    franchiseData.franchiseTypes.any { franchiseType ->
                        franchiseType.type == type
                    }
                }

                binding.tvResultCount.text = "Showing ${standFranchiseList.size} result "

                val layoutManager = GridLayoutManager(binding.root.context, 2)
                var recycler = binding.rvFranchises
                recycler.layoutManager = layoutManager
                val adapterList = FranchisesListAdapter(standFranchiseList)
                recycler.adapter = adapterList

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
                exception.message?.let { Log.e("Firestore", it) }
            }
    }
}