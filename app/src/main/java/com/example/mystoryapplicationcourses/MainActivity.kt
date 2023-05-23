package com.example.mystoryapplicationcourses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mystoryapplicationcourses.data.utils.Preference
import com.example.mystoryapplicationcourses.databinding.ActivityMainBinding
import com.example.mystoryapplicationcourses.ui.auth.Login.LoginActivity
import com.example.mystoryapplicationcourses.ui.auth.Register.RegisterActivity
import com.example.mystoryapplicationcourses.ui.story.list.ListStoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val DURATION: Long = 1500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        register()
        login()
        saveData()
    }

    private fun register() {
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveData() {
        val sharedPref = Preference.initPref(this, "onSignIn")
        val token = sharedPref.getString("token", "")

        var intent = Intent(this, LoginActivity::class.java)
        if (token != "") {
            intent = Intent(this, ListStoryActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, DURATION)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, DURATION)
        }
    }
    }

