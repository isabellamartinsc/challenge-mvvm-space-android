package com.devpass.spaceapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.devpass.spaceapp.R
import com.devpass.spaceapp.data.api.response.RocketDetailResponse
import com.devpass.spaceapp.databinding.FragmentRocketBinding
import com.devpass.spaceapp.model.RocketDetail
import com.devpass.spaceapp.presentation.rocket_detail.RocketDetailsActivity
import com.devpass.spaceapp.presentation.rocket_detail.RocketDetailsUiState
import com.devpass.spaceapp.presentation.rocket_detail.RocketDetailsViewModel

class RocketFragment : Fragment() {

    private lateinit var binding: FragmentRocketBinding

    private val viewModel: RocketDetailsViewModel by viewModels()

    private var rocketId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRocketBinding.inflate(inflater, container, false)

        rocketId = arguments?.get(ARG_ROCKET).toString()

        Log.i("RocketFragment", rocketId)

        lifecycleScope.launchWhenResumed {
            viewModel.loadRocketDetails(rocketId)
        }

        with(binding) {
            cardView.setOnClickListener {
                val intent = Intent(requireContext(), RocketDetailsActivity::class.java).apply {
                    putExtra(ARG_ROCKET, rocketId)
                }
                startActivity(intent)
            }
        }

        observeUIState()

        return binding.root
    }

    private fun observeUIState() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is RocketDetailsUiState.Error -> TODO()
                RocketDetailsUiState.Loading -> TODO()
                is RocketDetailsUiState.Success -> {
                    binding.tvTextCardRocket.text = it.data?.name
                    binding.tvTittleCardRocket.text = it.data?.description
                    Glide.with(binding.root.context).load(it.data?.image).into(binding.ivImageCard)
                }
            }
        }
    }

    companion object {
        private const val ARG_ROCKET = "ARG_ROCKET"

        fun getInstance(rocketDetail: String?) = RocketFragment().apply {
            arguments = bundleOf(ARG_ROCKET to rocketDetail)
        }


    }


}