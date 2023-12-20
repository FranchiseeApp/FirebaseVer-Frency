package com.dicoding.frency.ui.bottomsheet

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.frency.data.entity.FranchiseItem
import com.dicoding.frency.databinding.BottomSheetBinding
import com.dicoding.frency.databinding.BottomSheetOptionsBinding
import com.dicoding.frency.utils.formatNumber
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ModalBottomSheet(private val clickedItem: FranchiseItem) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTypeInp.text = clickedItem.type
        binding.tvFacilityInp.text = clickedItem.facility
        binding.tvPriceInp.text = "Rp "+ formatNumber(clickedItem.price)

        dialog?.setOnShowListener {
            (requireActivity() as? AppCompatActivity)?.findViewById<View>(R.id.content)?.alpha = 0.6f
        }

        // Set alpha ke 1.0 saat bottom sheet ditutup
        dialog?.setOnDismissListener {
            dismiss()
            (requireActivity() as? AppCompatActivity)?.findViewById<View>(R.id.content)?.alpha = 1.0f
        }
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
}

class ModalBottomSheetOptions(private val franchiseId: String) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetOptionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dialog?.setOnShowListener {
            (requireActivity() as? AppCompatActivity)?.findViewById<View>(R.id.content)?.alpha = 0.6f
        }

        // Set alpha ke 1.0 saat bottom sheet ditutup
        dialog?.setOnDismissListener {
            dismiss()
            (requireActivity() as? AppCompatActivity)?.findViewById<View>(R.id.content)?.alpha = 1.0f
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}