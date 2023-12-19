package com.dicoding.frency.ui.wishlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.frency.ui.detail.DetailActivity
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.FranchiseCardBinding
import com.dicoding.frency.databinding.ItemWishlistBinding
import com.dicoding.frency.utils.formatNumber

class FranchiseWishlistListAdapter(private val franchiseList: List<FranchiseData>, private val context: Context) :
    RecyclerView.Adapter<FranchiseWishlistListAdapter.FranchiseViewHolder>() {

    inner class FranchiseViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(franchiseData: FranchiseData) {
            // Bind data ke elemen UI dalam item franschise_card menggunakan ViewBinding
            binding.tvNameFranchises.text = franchiseData.name
            val franchiseItem = franchiseData.franchiseTypes.map { it.price }

            val minPrice = franchiseItem.min()
            val maxPrice = franchiseItem.max()

            if (maxPrice != null) {
                binding.tvPriceFranchises.text = "Rp" + formatNumber(minPrice) + " - " + "Rp" + formatNumber(maxPrice)
            } else {
                binding.tvPriceFranchises.text = "Rp" + formatNumber(minPrice)
            }

            Glide.with(binding.root.context).load(franchiseData.images.firstOrNull()).into(binding.ivFranchise)

            binding.ivWaIcon.setOnClickListener {
                val phoneNumber = franchiseData.phoneNumber
                val url = "https://wa.me/62$phoneNumber"

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseViewHolder {
        val binding = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
