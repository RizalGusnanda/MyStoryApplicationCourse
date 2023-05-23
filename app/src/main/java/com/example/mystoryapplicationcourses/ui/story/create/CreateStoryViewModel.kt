package com.example.mystoryapplicationcourses.ui.story.create

import androidx.lifecycle.ViewModel
import com.example.mystoryapplicationcourses.data.utils.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postStory(file: MultipartBody.Part, description: RequestBody) = storyRepository.postStory(file, description)
}