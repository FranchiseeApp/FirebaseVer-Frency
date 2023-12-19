package com.dicoding.frency.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.ActivityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val franchiseId = intent.getStringExtra("franchiseId")

//        Log.d("DetailActivity", "Received franchiseId: $franchiseId")
        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("franchises").document(franchiseId!!)
        userDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val franchiseData = documentSnapshot.toObject(FranchiseData::class.java)
                    if (franchiseData != null) {
                        Glide.with(this)
                            .load(franchiseData.images[0])
                            .into(binding.bigImage)
                        binding.tvNameFranchises.text = franchiseData.name
                        binding.tvDescFranchises.text = franchiseData.description
//                        binding.btnWa.setOnClickListener{
//
//                        }
                    }
                } else {
// Dokumen tidak ditemukan di Firestore
                }
            }
            .addOnFailureListener { exception ->
// Handle kesalahan saat mengambil data dari Firestore
                Log.e("LoginActivity", "Error getting user document", exception)
            }
    }
}