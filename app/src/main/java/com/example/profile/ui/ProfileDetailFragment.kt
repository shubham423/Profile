package com.example.profile.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.profile.R
import com.example.profile.data.models.Profile
import com.example.profile.databinding.FragmentProfileDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {
    private val RC_GALLERY = 100
    private var imageUri: Uri? = null
    private lateinit var binding: FragmentProfileDetailBinding
    private val viewModel:ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.selectedProfile!=null){
            val profile=viewModel.selectedProfile
            imageUri=Uri.parse(profile?.profilePhoto)
            binding.profileImg.setImageURI(Uri.parse(profile?.profilePhoto))
            binding.etFirstName.setText(profile?.firstName)
            binding.etLastName.setText(profile?.lastName)
        }
        binding.profileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/jpeg"
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    getString(R.string.chooser_title)
                ), RC_GALLERY)
        }

        binding.saveBtn.setOnClickListener {
            if (binding.etFirstName.text.isEmpty() || binding.etLastName.text.isEmpty()){
                Toast.makeText(requireContext(), "fill all fields", Toast.LENGTH_SHORT).show()
            }else {
                if (viewModel.selectedProfile != null) {
                    val profile = Profile(
                        binding.etFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        imageUri.toString(),
                        id= viewModel.selectedProfile!!.id
                    )
                    viewModel.updateProfile(profile)
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "updated successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val profile = Profile(
                        binding.etFirstName.text.toString(),
                        binding.etLastName.text.toString(),
                        imageUri.toString()
                    )
                    viewModel.insertProfile(profile)
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "saved successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_GALLERY) {
                imageUri = data?.data
                binding.profileImg.setImageURI(imageUri)
                val contentResolver = requireContext().contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                imageUri?.let { contentResolver.takePersistableUriPermission(it, takeFlags) }
            }
        }
    }
}