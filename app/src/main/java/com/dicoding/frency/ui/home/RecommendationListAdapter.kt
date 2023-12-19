package com.dicoding.frency.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.RecomendationCardBinding
import com.dicoding.frency.ui.detail.DetailActivity

class RecommendationListAdapter(private val franchiseList: List<FranchiseData>) :
    RecyclerView.Adapter<RecommendationListAdapter.FranchiseViewHolder>() {

    inner class FranchiseViewHolder(private val binding: RecomendationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(franchiseData: FranchiseData) {
            // Bind data ke elemen UI dalam item franschise_card menggunakan ViewBinding


            Glide.with(binding.root.context).load(franchiseData.images.firstOrNull()).into(binding.ivRecommend)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseViewHolder {
        val binding = RecomendationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FranchiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FranchiseViewHolder, position: Int) {
        val franchiseData = franchiseList[position]
        holder.bind(franchiseData)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            // Mengirim ID dokumen yang dipilih ke halaman detail menggunakan Intent
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("franchiseId", franchiseData.documentId)
            Log.d("SendId", "franchiseId: ${franchiseData.documentId}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return franchiseList.size
    }
}