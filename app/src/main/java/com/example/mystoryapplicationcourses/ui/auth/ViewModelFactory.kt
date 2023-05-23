package com.example.mystoryapplicationcourses.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapplicationcourses.data.utils.StoryRepository
import com.example.mystoryapplicationcourses.data.utils.network.ApiConfig
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor
import com.example.mystoryapplicationcourses.ui.auth.Login.LoginViewModel
import com.example.mystoryapplicationcourses.ui.auth.Register.RegisterViewModel
import com.example.mystoryapplicationcourses.ui.story.create.CreateStoryViewModel
import com.example.mystoryapplicationcourses.ui.story.list.ListStoryViewModel
import com.example.mystoryapplicationcourses.ui.story.map.MapViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private fun provideRepository(): AuthRepositor {
        val apiService = ApiConfig.getApiService(context)
        return AuthRepositor(apiService)
    }

    private fun providesRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService(context)
        return StoryRepository(apiService)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(provideRepository()) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java) -> {
                ListStoryViewModel(providesRepository()) as T
            }
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) -> {
                CreateStoryViewModel(providesRepository()) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(providesRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}