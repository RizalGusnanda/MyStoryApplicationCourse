package com.example.mystoryapplicationcourses.ui.auth.Register

import androidx.lifecycle.ViewModel
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor

class RegisterViewModel(private val pref: AuthRepositor) : ViewModel() {
    fun saveUser(name: String, email: String, password: String) = pref.register(name, email, password)
}