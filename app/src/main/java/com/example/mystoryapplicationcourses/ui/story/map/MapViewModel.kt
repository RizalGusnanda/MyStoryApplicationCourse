package com.example.mystoryapplicationcourses.ui.story.map

import androidx.lifecycle.ViewModel
import com.example.mystoryapplicationcourses.data.utils.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}