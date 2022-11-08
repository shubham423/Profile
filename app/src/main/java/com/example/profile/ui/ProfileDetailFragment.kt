package com.example.profile.ui

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.profile.data.models.Profile
import com.example.profile.databinding.FragmentProfileDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
            chooseImage(requireActivity())
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
            if (requestCode == 0) {
                binding.profileImg.setImageBitmap(data?.extras?.get("data") as Bitmap?)
                (data?.extras?.get("data") as Bitmap?)?.let { saveToInternalStorage(it) }
                imageUri =
                    (data?.extras?.get("data") as Bitmap?)?.let {
                        getImageUri(requireContext(),
                            it
                        )
                    }

            }else if (requestCode==1){
                imageUri =data?.data!!
                binding.profileImg.setImageURI(imageUri)

                val contentResolver = requireContext().contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                imageUri?.let { contentResolver.takePersistableUriPermission(it, takeFlags) }
            }
        }
    }


    // function to let's the user to choose image from camera or gallery
    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        )
        val builder = AlertDialog.Builder(context)
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                val pickPhoto = Intent(
                    Intent.ACTION_OPEN_DOCUMENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val cw = ContextWrapper(requireContext())
        // path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("imageDir", MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}