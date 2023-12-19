package com.dicoding.frency.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.frency.R
import com.dicoding.frency.data.entity.FranchiseType
import com.dicoding.frency.databinding.ItemTypeDetailBinding

class FranchiseTypeAdapter(private val items: List<FranchiseType>) :
    RecyclerView.Adapter<FranchiseTypeAdapter.FranchiseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseViewHolder {
        val binding = ItemTypeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FranchiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FranchiseViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class FranchiseViewHolder(private val binding: ItemTypeDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(franchiseTypeItem: FranchiseType) {
            binding.tvNameFranchises.text = franchiseTypeItem.name
        }
    }
}