package com.dicoding.frency.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.frency.data.entity.User
import com.dicoding.frency.data.session.SessionManager
import com.dicoding.frency.databinding.FragmentWishlistBinding
import com.dicoding.frency.ui.login.LoginActivity

class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

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
            binding.rvWishlist.visibility = View.VISIBLE
        } else {
            binding.tvHaveAcc.visibility = View.VISIBLE
            binding.tvLoginHere.visibility = View.VISIBLE
            binding.tvLoginHere.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }

    }
}