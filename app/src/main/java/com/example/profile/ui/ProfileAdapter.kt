package com.example.profile.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.profile.data.models.Profile
import com.example.profile.databinding.ProfileItemBinding

class ProfileAdapter(
    private val list: List<Profile>,
    private val onEditClicked: (profile: Profile) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ProfileAdapterViewHolder>() {

    class ProfileAdapterViewHolder(private val binding: ProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile, onEditClicked: (profile: Profile) -> Unit) {
            binding.firstNameTxt.text=profile.firstName
            binding.lastNameTxt.text=profile.lastName
            binding.imageView.setImageURI(Uri.parse(profile.profilePhoto))
            binding.editIcon.setOnClickListener {
                onEditClicked(profile)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapterViewHolder {
        val binding = ProfileItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileAdapterViewHolder, position: Int) {
        holder.bind(list[position], onEditClicked)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}