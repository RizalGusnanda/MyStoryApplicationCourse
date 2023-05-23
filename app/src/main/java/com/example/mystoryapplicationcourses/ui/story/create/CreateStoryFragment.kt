package com.example.mystoryapplicationcourses.ui.story.create

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.exifinterface.media.ExifInterface
import com.example.mystoryapplicationcourses.data.utils.PostStoryResponse
import com.example.mystoryapplicationcourses.databinding.UploadCameraBinding
import com.example.mystoryapplicationcourses.ui.story.setting.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.ui.auth.ViewModelFactory
import com.example.mystoryapplicationcourses.ui.story.list.ListStoryActivity

class CreateStoryFragment : AppCompatActivity() {
    private lateinit var binding: UploadCameraBinding
    private lateinit var createStoryViewModel: CreateStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UploadCameraBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Story Apps Course")

        createStoryViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CreateStoryViewModel::class.java)

        binding.cameraButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.uploadButton.setOnClickListener {
            uploadImage()
        }

        val fileUri = intent.getParcelableExtra<Uri>("selected_image")
        fileUri?.let { uri ->
            val isBackCamera = intent.getBooleanExtra("isBackCamera", false)
            getFile = uri.toFile()
            val rotation = getRotationFromImage(getFile!!.path)
            val rotatedBitmap = BitmapFactory.decodeFile(getFile!!.path).rotateBitmap(rotation)
            binding.previewImageView.setImageBitmap(rotatedBitmap)
        }
    }

    private var getFile: File? = null
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            val rotation = getRotationFromImage(getFile!!.path)
            val rotatedBitmap = BitmapFactory.decodeFile(getFile!!.path).rotateBitmap(rotation)
            binding.previewImageView.setImageBitmap(rotatedBitmap)
        }
    }

    private fun uploadImage() {
        getFile?.let { file ->
            val rotation = getRotationFromImage(file.path)
            val bitmap = BitmapFactory.decodeFile(file.path)
            val rotatedBitmap = bitmap.rotateBitmap(rotation)
            val compressedFile = reduceFileImage(rotatedBitmap)
            val descriptionText = binding.descriptionTextView.text
            if (descriptionText.isNotEmpty()) {
                val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    compressedFile.name,
                    requestImageFile
                )

                createStoryViewModel.postStory(imageMultipart, description).observe(this) {
                    if (it != null) {
                        when (it) {
                            is Result.Success -> {
                                showLoading(false)
                                processCreate(it.data)
                            }
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this@CreateStoryFragment, "Silakan masukkan deskripsi gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@CreateStoryFragment, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processCreate(data: PostStoryResponse) {
        if (data.error) {
            Toast.makeText(this@CreateStoryFragment, "Tambah Story Gagal", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@CreateStoryFragment,
                "Silakan Create Kembali Story Kamu !!",
                Toast.LENGTH_LONG
            ).show()
            setResult(Activity.RESULT_OK)
            finish()
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)

        }
    }

    private fun showLoading(state: Boolean) {
        binding.pbCreateStory.isVisible = state
        binding.uploadButton.isInvisible = state
        binding.cameraButton.isInvisible = state
        binding.galleryButton.isInvisible = state
        binding.viewDes.isInvisible = state
        binding.previewImageView.isInvisible = state
        binding.descriptionTextView.isInvisible = state
    }

    private fun getRotationFromImage(imagePath: String): Int {
        val exif = ExifInterface(imagePath)
        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun Bitmap.rotateBitmap(degrees: Int): Bitmap {
        if (degrees == 0) {
            return this
        }

        val matrix = Matrix()
        matrix.setRotate(degrees.toFloat())
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun reduceFileImage(bitmap: Bitmap): File {
        val file = File.createTempFile("compressed", ".jpg", cacheDir)
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        out.flush()
        out.close()
        return file
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}