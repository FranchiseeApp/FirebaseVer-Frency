package com.dicoding.frency.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.data.entity.FranchiseType
import com.dicoding.frency.data.entity.User
import com.dicoding.frency.data.session.SessionManager
import com.dicoding.frency.databinding.FragmentHomeBinding
import com.dicoding.frency.ui.detail.DetailActivity
import com.dicoding.frency.ui.franchiseslist.FranchisesListActivity
import com.dicoding.frency.ui.login.LoginActivity
import com.dicoding.frency.utils.ZoomOutPageTransformer
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private val carouselHomeAdapter: CarouselHomeAdapter by lazy { CarouselHomeAdapter(::carouselItemClicked) }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var typeAdapter: FranchiseTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        val franchiseNames = listOf(
            FranchiseType("Stand"),
            FranchiseType("Store"),
            FranchiseType("Kios"),
            FranchiseType("Outlet"),
            FranchiseType("Resto"),
            FranchiseType("Mini market")
        )
        typeAdapter = FranchiseTypeAdapter(franchiseNames) { clickedItem ->
            val intent = Intent(requireContext(), FranchisesListActivity::class.java)
            intent.putExtra("typeFranchises", clickedItem.name)
            Log.d("sendData", "onCreate: ${clickedItem.name}")
            startActivity(intent)
        }

        binding.rvType.adapter = typeAdapter

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvType.layoutManager = layoutManager

        loadData()


        binding.btnSeeAllFranchises.setOnClickListener {
            val intent = Intent(requireContext(), FranchisesListActivity::class.java)
            intent.putExtra("allFranchises", true)
            startActivity(intent)
        }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(requireContext(), FranchisesListActivity::class.java)
                intent.putExtra("searchFranchise", query)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Di sini, Anda dapat melakukan sesuatu ketika teks pada SearchView berubah
                // Misalnya, perbarui tampilan berdasarkan teks yang dimasukkan pengguna
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val user: User? = sessionManager.getSession()

        if (user != null) {
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

                    val layoutManagerRecommendList = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    var recyclerRecommend = binding.rvRecommendation
                    recyclerRecommend.layoutManager = layoutManagerRecommendList
                    val adapterListRecommend = RecommendationListAdapter(franchiseList)
                    recyclerRecommend.adapter = adapterListRecommend


                    val layoutManager = GridLayoutManager(binding.root.context, 2)
                    var recycler = binding.rvFranchise
                    recycler.layoutManager = layoutManager
                    val limitedList = franchiseList.subList(0, 6)
                    val adapterList = FranchiseListAdapter(limitedList)
                    recycler.adapter = adapterList

                    carouselHomeAdapter.submitList(franchiseList)

                    with(binding) {
                        this?.carouselPager?.apply {
                            adapter = carouselHomeAdapter

                            val zoomOutPageTransformer = ZoomOutPageTransformer()
                            setPageTransformer { page, position ->
                                zoomOutPageTransformer.transformPage(page, position)
                            }

                            dotsIndicator.attachTo(this)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting documents: ", exception)
                    exception.message?.let { Log.e("Firestore", it) }
                }
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun carouselItemClicked(franchise: FranchiseData) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("franchiseId", franchise.documentId)
        startActivity(intent)
    }
}