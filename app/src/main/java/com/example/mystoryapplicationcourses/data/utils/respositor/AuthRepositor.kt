package com.example.mystoryapplicationcourses.data.utils.respositor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mystoryapplicationcourses.data.utils.LoginResponse
import com.example.mystoryapplicationcourses.data.utils.PostStoryResponse
import com.example.mystoryapplicationcourses.data.utils.RegisterResponse
import com.example.mystoryapplicationcourses.data.utils.Story
import com.example.mystoryapplicationcourses.data.utils.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepositor(private val apinetwork: ApiService) {
    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData(
        Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apinetwork.register(name, email, password).execute()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositor", "register: ${e.message}", e)
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = withContext(Dispatchers.IO) {
                apinetwork.login(email, password).execute()
            }
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositor", "login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }



}