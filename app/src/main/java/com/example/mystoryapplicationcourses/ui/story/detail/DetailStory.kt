package com.example.mystoryapplicationcourses.ui.story.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.imageLoader
import coil.request.ImageRequest
import com.example.mystoryapplicationcourses.data.utils.Story
import com.example.mystoryapplicationcourses.databinding.DetailStoryBinding

class DetailStory : AppCompatActivity() {
    private lateinit var binding: DetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postponeEnterTransition()

        val story = retrieveStoryFromIntent()
        binding.story = story

        val request = ImageRequest.Builder(this)
            .data(story.photoUrl)
            .target(
                onSuccess = {
                    startPostponedEnterTransition()
                },
                onError = {
                    startPostponedEnterTransition()
                }
            )
            .build()

        application.imageLoader.enqueue(request)

        setupActionBar()
    }

    private fun retrieveStoryFromIntent(): Story {
        val id = intent.getStringExtra("id") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val photoUrl = intent.getStringExtra("photoUrl") ?: ""
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)
        return Story(id = id, name = name, description = description, photoUrl = photoUrl, createdAt = "", lati = lat , long = lon)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Story Apps Course"
        }
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
