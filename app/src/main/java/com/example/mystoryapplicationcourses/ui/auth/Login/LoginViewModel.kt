package com.example.mystoryapplicationcourses.ui.auth.Login

import androidx.lifecycle.ViewModel
import com.example.mystoryapplicationcourses.data.utils.respositor.AuthRepositor

class LoginViewModel(private val pref: AuthRepositor) : ViewModel() {

    fun login(email: String, password: String) = pref.loginUser(email, password)

}