package com.dicoding.frency.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.data.entity.User
import com.dicoding.frency.data.session.SessionManager
import com.dicoding.frency.databinding.FragmentWishlistBinding
import com.dicoding.frency.ui.login.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private val db = FirebaseFirestore.getInstance()

    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val user: User? = sessionManager.getSession()

        if (user != null) {
            userId = user.userId
            binding.rvWishlist.visibility = View.VISIBLE
            loadData(userId)
        } else {
            binding.tvHaveAcc.visibility = View.VISIBLE
            binding.tvLoginHere.visibility = View.VISIBLE
            binding.tvLoginHere.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            userId = null.toString()
        }

    }
    override fun onResume() {
        super.onResume()
        loadData(userId)
    }

    private fun loadData(userId: String) {
        binding.overlayLoading.visibility = View.VISIBLE
        val userDocument = db.collection("users").document(userId)
        userDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val favorites = documentSnapshot.get("favorites") as? ArrayList<String>
                    Log.d("myfavorites", "loadData: $favorites ")

                    // Jika daftar favorites tidak kosong, ambil data franchise
                    if (!favorites.isNullOrEmpty()) {
                        val franchiseDataList = mutableListOf<FranchiseData>()

                        for (franchiseId in favorites) {
                            val franchiseDoc = db.collection("franchises").document(franchiseId)
                            franchiseDoc.get()
                                .addOnSuccessListener { franchiseSnapshot ->
                                    if (franchiseSnapshot.exists()) {
                                        val franchiseData = franchiseSnapshot.toObject(FranchiseData::class.java)
                                        franchiseData?.let {
                                            franchiseDataList.add(it)
                                        }
                                        val layoutManagerRecommendList = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                                        val rvWishlist = binding.rvWishlist
                                        rvWishlist.layoutManager = layoutManagerRecommendList
                                        val adapterListRecommend = FranchiseWishlistListAdapter(franchiseDataList, requireContext())
                                        rvWishlist.adapter = adapterListRecommend

                                        val position = franchiseDataList.indexOfFirst { it.documentId == franchiseId }
                                        adapterListRecommend.notifyItemRemoved(position)
                                    }

                                }
                                .addOnFailureListener { franchiseException ->
                                    // Handle kesalahan saat mengambil data dari Firestore untuk franchisenya
                                    Log.e("DetailActivity", "Error getting franchise document", franchiseException)
                                }
                        }
                    } else {
                        binding.rvWishlist.visibility = View.GONE
                        binding.tvNoItem.visibility = View.VISIBLE
                    }

                    binding.overlayLoading.visibility = View.GONE
                } else {
                    binding.overlayLoading.visibility = View.GONE
                    // Dokumen user tidak ditemukan di Firestore
                }
            }
            .addOnFailureListener { userException ->
                binding.overlayLoading.visibility = View.GONE
                // Handle kesalahan saat mengambil data dari Firestore untuk user
                Log.e("DetailActivity", "Error getting user document", userException)
            }
    }
}