package com.dicoding.frency.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.ActivityDetailBinding
import com.dicoding.frency.ui.bottomsheet.ModalBottomSheet
import com.dicoding.frency.ui.bottomsheet.ModalBottomSheetOptions
import com.dicoding.frency.ui.listimage.ListImageActivity
import com.dicoding.frency.utils.formatNumber
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: FranchiseItemAdapter

    private var franchiseId: String? = null
    private var modalBottomSheet: ModalBottomSheetOptions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        franchiseId = intent.getStringExtra("franchiseId")
        modalBottomSheet = franchiseId?.let { ModalBottomSheetOptions(it) }

        val toolbar = binding.toolbarDetail
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

//        Log.d("DetailActivity", "Received franchiseId: $franchiseId")

        loadData()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.dicoding.frency.R.menu.menu_detail, menu)
        return true
    }
//    override fun onResume() {
//        super.onResume()
//        modalBottomSheet?.dismiss()
//    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.dicoding.frency.R.id.action_more -> {
                modalBottomSheet?.show(supportFragmentManager, ModalBottomSheet.TAG)
                true
            }
            // Handle item lainnya jika ada
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadData() {
        binding.overlayLoading.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("franchises").document(franchiseId!!)
        userDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val franchiseData = documentSnapshot.toObject(FranchiseData::class.java)
                    if (franchiseData != null) {
                        Log.d("detailData", "$franchiseData")


                        val imagesFranchise = franchiseData.images

                        Glide.with(this).load(imagesFranchise[0]).into(binding.bigImage)
                        Glide.with(this).load(imagesFranchise[1]).into(binding.smallImage1)
                        Glide.with(this).load(imagesFranchise[2]).into(binding.smallImage2)
                        Glide.with(this).load(imagesFranchise[3]).into(binding.smallImage3)

                        binding.btnLoadMore.setOnClickListener {
                            val intent = Intent(this@DetailActivity, ListImageActivity::class.java)
                            intent.putExtra("franchiseId", franchiseData.documentId)

                            startActivity(intent)
                        }


                        Log.d("imagesF", "onCreate: ${imagesFranchise.size}")

                        if (imagesFranchise.size <= 4) {
                            binding.btnLoadMore.visibility = View.GONE
                        } else {
                            binding.btnLoadMore.visibility = View.VISIBLE
                            val updateText = imagesFranchise.size - 4
                            binding.btnLoadMore.text = "+" + updateText
                        }

                        // == NAME FRANCHISE
                        binding.tvNameFranchises.text = franchiseData.name


                        // == TYPE FRANCHISE
                        val franchiseTypes = franchiseData.franchiseTypes
//                        Log.d("rvApp", "onCreate: $franchiseTypes")
                        adapter = FranchiseItemAdapter(franchiseTypes) { clickedItem ->

                            // Ketika item diklik, tampilkan modal bottom sheet dan tampilkan informasi yang diperlukan
                            val modalBottomSheet = ModalBottomSheet(clickedItem)
                            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)

                        }
                        binding.rvTypeFranchise.adapter = adapter

                        val imageLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        binding.rvTypeFranchise.layoutManager = imageLayoutManager


                        // == PRICE
                        val prices = franchiseTypes.map { it.price }

                        if (prices.size == 1) {
                            binding.tvPriceFranchises.text = "Rp" + formatNumber(prices[0])
                        } else {
                            val minPrice = prices.minOrNull()
                            val maxPrice = prices.maxOrNull()

                            if (minPrice != null && maxPrice != null) {
//                                binding.tvPriceFranchises.text = "$minPrice - $maxPrice"
                                binding.tvPriceFranchises.text = "Rp" + formatNumber(minPrice) + " - " + "Rp" + formatNumber(maxPrice)
                            } else {
                                // Handle jika tidak ada harga atau hanya ada satu harga
                                binding.tvPriceFranchises.text = "Price not available"
                            }
                        }


                        // == DESKRIPSI
                        binding.tvDescFranchises.text = franchiseData.description

                        // == BUTTON WA
                        binding.btnWa.setOnClickListener {
                            val phoneNumber = franchiseData.phoneNumber
                            val url = "https://wa.me/62$phoneNumber"

                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        }
                        binding.overlayLoading.visibility = View.GONE
                    }
                } else {
                    binding.overlayLoading.visibility = View.GONE
                    // Dokumen tidak ditemukan di Firestore
                }
            }
            .addOnFailureListener { exception ->
                binding.overlayLoading.visibility = View.GONE
                // Handle kesalahan saat mengambil data dari Firestore
                Log.e("LoginActivity", "Error getting user document", exception)
            }
    }
}