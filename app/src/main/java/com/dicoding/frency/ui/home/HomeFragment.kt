package com.dicoding.frency.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.frency.R
import com.dicoding.frency.data.entity.DummyData
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.data.entity.User
import com.dicoding.frency.data.session.SessionManager
import com.dicoding.frency.databinding.FragmentHomeBinding
import com.dicoding.frency.ui.login.LoginActivity
import com.dicoding.frency.utils.ZoomOutPageTransformer
import com.dicoding.frency.utils.showMessage
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private val carouselHomeAdapter: CarouselHomeAdapter by lazy { CarouselHomeAdapter(::carouselItemClicked) }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

//        val swipeRefreshLayout = binding.swipeRefreshLayout
//
//        // Tambahkan listener untuk refresh
//        swipeRefreshLayout.setOnRefreshListener {
//            // Panggil fungsi untuk memuat ulang data
//            loadData()
//        }

        // Memuat data pertama kali ketika fragment dimuat
        loadData()

    }

    override fun onResume() {
        super.onResume()
        // Memuat data setiap kali fragment di-resume
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


                    val layoutManager = GridLayoutManager(binding.root.context, 2)
                    var recycler = binding.rvFranchise
                    recycler.layoutManager = layoutManager
                    val adapterList = FranchiseListAdapter(franchiseList)
                    recycler.adapter = adapterList

//                    carouselHomeAdapter.submitList(franchiseList)
                    carouselHomeAdapter.submitList(DummyData.dataDummy)

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

//                    binding.swipeRefreshLayout?.isRefreshing = false
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting documents: ", exception)
                    exception.message?.let { Log.e("Firestore", it) }
//                    binding.swipeRefreshLayout?.isRefreshing = false
                }
        } else {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun carouselItemClicked(franchise: FranchiseData) {
        getString(R.string.on_click_handler).showMessage(requireContext())
    }
}