package com.dicoding.frency.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.frency.R
import com.dicoding.frency.data.entity.DummyData
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.FragmentHomeBinding
import com.dicoding.frency.ui.adapter.CarouselHomeAdapter
import com.dicoding.frency.ui.adapter.FranchiseListAdapter
import com.dicoding.frency.utils.ZoomOutPageTransformer
import com.dicoding.frency.utils.showMessage

class HomeFragment : Fragment() {

    private val carouselHomeAdapter: CarouselHomeAdapter by lazy { CarouselHomeAdapter(::carouselItemClicked) }

//    private var _binding: FragmentHomeBinding? = null
//
//    private val binding get() = _binding!!

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val layoutManager = GridLayoutManager(binding.root.context, 2)
        var recycler = binding.rvFranchise
        recycler.layoutManager = layoutManager
        val adapterList = FranchiseListAdapter()
        adapterList.submitList(DummyData.dataDummy)
        recycler.adapter = adapterList

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

        return root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private fun carouselItemClicked(franchise: FranchiseData) {
        getString(R.string.on_click_handler).showMessage(requireContext())
    }
}