package com.dicoding.frency.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.frency.ui.detail.DetailActivity
import com.dicoding.frency.data.entity.FranchiseData
import com.dicoding.frency.databinding.FranchiseCardBinding

//class FranchiseListAdapter : ListAdapter<FranchiseData, FranchiseListAdapter.MyViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = FranchiseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val franchiseItem = getItem(position) as FranchiseData
//        holder.bind(franchiseItem)
//    }
//
//    class MyViewHolder(private val binding: FranchiseCardBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: FranchiseData) {
//            binding.tvTitle.text = item.name
//            binding.tvRange.text = item.category
//            Glide.with(binding.root)
//                .load(item.images[0])
////                .diskCacheStrategy(DiskCacheStrategy.NONE )
////                .skipMemoryCache(true)
//                .into(binding.ivFranchise)
//
//
////            binding.root.setOnClickListener {
////                val context = binding.root.context
////                val intent = Intent(context, DetailUserActivity::class.java)
////                intent.putExtra(PARCEL_NAME, item)
////                context.startActivity(intent)
////            }
//        }
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FranchiseData>() {
//            override fun areItemsTheSame(oldItem: FranchiseData, newItem: FranchiseData): Boolean {
//                return oldItem.name == newItem.name
//            }
//
//            override fun areContentsTheSame(oldItem: FranchiseData, newItem: FranchiseData): Boolean {
//                return oldItem == newItem
//            }
//        }
//        const val PARCEL_NAME = "data"
//    }
//}

class FranchiseListAdapter(private val franchiseList: List<FranchiseData>) :
    RecyclerView.Adapter<FranchiseListAdapter.FranchiseViewHolder>() {

    inner class FranchiseViewHolder(private val binding: FranchiseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(franchiseData: FranchiseData) {
            // Bind data ke elemen UI dalam item franschise_card menggunakan ViewBinding
            binding.tvTitle.text = franchiseData.name
            binding.tvCategory.text = franchiseData.category
            Glide.with(binding.root.context).load(franchiseData.images.firstOrNull()).into(binding.ivFranchise)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseViewHolder {
        val binding = FranchiseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FranchiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FranchiseViewHolder, position: Int) {
        val franchiseData = franchiseList[position]
//        val firstImageUri = franchiseData.images.firstOrNull()
        holder.bind(franchiseData)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            // Mengirim ID dokumen yang dipilih ke halaman detail menggunakan Intent
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("franchiseId", franchiseData.documentId)
            Log.d("SendId", "franchiseId: ${franchiseData.documentId}")// Ganti "id" dengan field ID dari FranchiseData
            context.startActivity(intent)
        }

//        if (!firstImageUri.isNullOrEmpty()) {
//            Glide.with(holder.itemView)
//                .load(firstImageUri)
//                .into(holder.binding.ivFranchise)
//        } else {
//            // Handle case when there's no image available
//            // You can set a placeholder or hide the ImageView
//        }
    }

    override fun getItemCount(): Int {
        return franchiseList.size
    }
}
