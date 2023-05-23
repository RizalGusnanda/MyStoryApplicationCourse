package com.example.mystoryapplicationcourses.ui.auth.Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapplicationcourses.data.utils.LoginResponse
import com.example.mystoryapplicationcourses.data.utils.Preference
import com.example.mystoryapplicationcourses.databinding.ActivityLoginBinding
import com.example.mystoryapplicationcourses.data.utils.respositor.Result
import com.example.mystoryapplicationcourses.ui.auth.Register.RegisterActivity
import com.example.mystoryapplicationcourses.ui.auth.ViewModelFactory
import com.example.mystoryapplicationcourses.ui.story.list.ListStoryActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(this)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)


        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if(!PatternsCompat.EMAIL_ADDRESS.matcher(s).matches()){
                    binding.emailEditText.error ="Email harus mengandung karakter @gmail.com"
                } else {
                    binding.emailEditText.error = null
                }
            }
        })



        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            if (!email.contains("@")) {
                binding.edLoginEmail.error = "Email harus mengandung karakter @gmail.com"
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.edLoginPassword.error = "Password harus memiliki 8 karakter"
                return@setOnClickListener
            }


            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masukkan email"

                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masukkan password"
                }
                else -> {
                    loginViewModel.login(email, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    processLogin(result.data)
                                    showLoading(false)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        OnBackPressed()
    }


    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
        } else {
            Preference.saveToken(data.loginResult.token, this)
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            pbLogin.isVisible = state
            edLoginEmail.isInvisible = state
            emailTextView.isInvisible = state
            passwordTextView.isInvisible = state
            messageTextView.isInvisible = state
            edLoginPassword.isInvisible = state
            btnLogin.isInvisible = state
            buttonRegister.isInvisible = state
            tvRegister.isInvisible = state
        }
    }


    private fun OnBackPressed() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}



