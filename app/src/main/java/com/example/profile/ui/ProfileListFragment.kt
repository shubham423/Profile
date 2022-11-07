package com.example.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.profile.R
import com.example.profile.databinding.FragmentProfileListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileListFragment : Fragment() {
    private val viewModel:ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileListBinding
    private lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener{
            requireView().findNavController().navigate(R.id.action_profileListFragment_to_profileDetailFragment)
        }
        initObservers()
    }

    private fun initObservers() {
        viewModel.readPerson.observe(viewLifecycleOwner){
            adapter=ProfileAdapter(it){profile->
                val bundle = Bundle()
                bundle.putInt("id", profile.id)
                viewModel.selectedProfile=profile
                requireView().findNavController().navigate(R.id.action_profileListFragment_to_profileDetailFragment)

            }
            binding.rvProfileList.adapter=adapter
        }
    }
}