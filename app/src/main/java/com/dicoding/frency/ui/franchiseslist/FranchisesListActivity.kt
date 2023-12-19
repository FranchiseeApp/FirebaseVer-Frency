package com.dicoding.frency.ui.franchiseslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.ActivityFranchisesListBinding
import com.google.firebase.firestore.FirebaseFirestore

class FranchisesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFranchisesListBinding
    private var allFranchises: Boolean? = null
    private var typeFranchises: String? = null
    private var searchFranchise: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("GetData", "onCreate is called")
        binding = ActivityFranchisesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allFranchises = intent.getBooleanExtra("allFranchises", false)
        typeFranchises = intent.getStringExtra("typeFranchises")
        searchFranchise = intent.getStringExtra("searchFranchise")

        if (allFranchises == true) {
            loadData()
        }

        if (!typeFranchises.isNullOrEmpty()) {
            loadDataType(typeFranchises!!)
        }

        if (!searchFranchise.isNullOrEmpty()) {
            loadDataSearch(searchFranchise!!)
        }

        Log.d("GetData2", "onCreate: $typeFranchises")
//        Log.d("GetData", "onCreate: $searchFranchise")


        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    if (searchFranchise != query) {
                        // Jika parameter pencarian berbeda dari pencarian sebelumnya
                        // Membuat intent baru hanya jika pencarian sebelumnya berbeda
                        val intent = Intent(this@FranchisesListActivity, FranchisesListActivity::class.java)
                        intent.putExtra("searchFranchise", query)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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

    private fun loadDataSearch(search: String) {
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

                val filteredFranchiseList = franchiseList.filter { franchise ->
                    // Ubah kedua string menjadi huruf kecil agar menjadi case insensitive
                    val franchiseName = franchise.name.toLowerCase()
                    val searchQuery = search.toLowerCase().trim()

                    // Filter berdasarkan nama yang mengandung string pencarian
                    franchiseName.contains(searchQuery)
                }

                binding.tvResultCount.text = "Showing ${filteredFranchiseList.size} result "

                val layoutManager = GridLayoutManager(binding.root.context, 2)
                var recycler = binding.rvFranchises
                recycler.layoutManager = layoutManager
                val adapterList = FranchisesListAdapter(filteredFranchiseList)
                recycler.adapter = adapterList

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
                exception.message?.let { Log.e("Firestore", it) }
            }
    }
}